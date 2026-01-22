package com.smartsorovnoma.data.service

import com.smartsorovnoma.data.repository.ResponseRepository
import com.smartsorovnoma.presentation.viewmodel.AnswerValue
import kotlinx.coroutines.delay

/**
 * So'rovnoma javoblarini yuborish xizmati
 * ResponseRepository'ni wrap qiladi va retry logic qo'shadi
 */
class ResponseSubmissionService(
    private val repository: ResponseRepository = ResponseRepository()
) {
    
    companion object {
        private const val MAX_RETRIES = 3
        private const val INITIAL_RETRY_DELAY_MS = 1000L
    }
    
    /**
     * Javoblarni Firebase'ga yuborish (automatic retry bilan)
     * 
     * @param surveyId So'rovnoma ID
     * @param answers Barcha javoblar map'i (questionId -> AnswerValue)
     * @return SubmitResult
     */
    suspend fun submitResponses(
        surveyId: String,
        answers: Map<String, AnswerValue>
    ): SubmitResult {
        return submitWithRetry(surveyId, answers, attempt = 0)
    }
    
    /**
     * Retry logic bilan submission
     */
    private suspend fun submitWithRetry(
        surveyId: String,
        answers: Map<String, AnswerValue>,
        attempt: Int
    ): SubmitResult {
        val result = repository.submitResponse(surveyId, answers)
        
        return if (result.isSuccess) {
            SubmitResult.Success(result.getOrNull() ?: "")
        } else {
            val error = result.exceptionOrNull()
            
            // Network errors - retry
            if (isNetworkError(error) && attempt < MAX_RETRIES) {
                val delayMs = INITIAL_RETRY_DELAY_MS * (attempt + 1)
                delay(delayMs)
                return submitWithRetry(surveyId, answers, attempt + 1)
            }
            
            // Final error message with attempt info
            val errorMsg = when {
                error?.message?.contains("NETWORK", ignoreCase = true) == true -> 
                    "Internet ulanishida xatolik. Iltimos, internetni tekshiring."
                error?.message?.contains("PERMISSION", ignoreCase = true) == true ->
                    "Yo'q, siz bu so'rovnomaga javob bera olmaysiz."
                error?.message?.contains("NOT_FOUND", ignoreCase = true) == true ->
                    "So'rovnoma topilmadi. Iltimos, qayta urinib ko'ring."
                else -> "Javoblarni yuborishda xatolik${if (attempt > 0) " ($attempt ta urinishdan keyin)" else ""}"
            }
            
            SubmitResult.Error(errorMsg, canRetry = attempt < MAX_RETRIES)
        }
    }
    
    /**
     * Network xatosi ekanligini tekshirish
     */
    private fun isNetworkError(error: Throwable?): Boolean {
        return error != null && (
            error.message?.contains("network", ignoreCase = true) == true ||
            error.message?.contains("connection", ignoreCase = true) == true ||
            error.message?.contains("timeout", ignoreCase = true) == true ||
            error is java.net.SocketException ||
            error is java.net.UnknownHostException
        )
    }
}

/**
 * Yuborish natijasi
 */
sealed class SubmitResult {
    data class Success(val responseId: String) : SubmitResult()
    data class Error(
        val message: String,
        val canRetry: Boolean = false,
        val originalError: Throwable? = null
    ) : SubmitResult()
}
