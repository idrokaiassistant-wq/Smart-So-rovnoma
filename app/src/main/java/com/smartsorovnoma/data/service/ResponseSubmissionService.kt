package com.smartsorovnoma.data.service

import com.smartsorovnoma.data.repository.ResponseRepository
import com.smartsorovnoma.presentation.viewmodel.AnswerValue

/**
 * So'rovnoma javoblarini yuborish xizmati
 * ResponseRepository'ni wrap qiladi
 */
class ResponseSubmissionService(
    private val repository: ResponseRepository = ResponseRepository()
) {
    
    /**
     * Javoblarni Firebase'ga yuborish
     * 
     * @param surveyId So'rovnoma ID
     * @param answers Barcha javoblar map'i (questionId -> AnswerValue)
     * @return SubmitResult
     */
    suspend fun submitResponses(
        surveyId: String,
        answers: Map<String, AnswerValue>
    ): SubmitResult {
        val result = repository.submitResponse(surveyId, answers)
        
        return if (result.isSuccess) {
            SubmitResult.Success(result.getOrNull() ?: "")
        } else {
            SubmitResult.Error(result.exceptionOrNull()?.message ?: "Javoblarni yuborishda xatolik")
        }
    }
}

/**
 * Yuborish natijasi
 */
sealed class SubmitResult {
    data class Success(val responseId: String) : SubmitResult()
    data class Error(val message: String) : SubmitResult()
}
