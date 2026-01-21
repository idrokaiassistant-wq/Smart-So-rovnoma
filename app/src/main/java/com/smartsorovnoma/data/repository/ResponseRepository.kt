package com.smartsorovnoma.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.smartsorovnoma.presentation.viewmodel.AnswerValue
import kotlinx.coroutines.tasks.await

/**
 * Repository for handling survey responses
 * Provides better error handling and abstraction
 */
class ResponseRepository {
    
    private val db = FirebaseFirestore.getInstance()
    
    /**
     * Submit survey response with error handling
     */
    suspend fun submitResponse(
        surveyId: String,
        answers: Map<String, AnswerValue>
    ): Result<String> {
        return try {
            // Validate inputs
            if (surveyId.isBlank()) {
                return Result.failure(Exception("Survey ID bo'sh bo'lishi mumkin emas"))
            }
            
            if (answers.isEmpty()) {
                return Result.failure(Exception("Kamida bitta javob kerak"))
            }
            
            // Serialize answers
            val serializedAnswers = answers.mapValues { (_, answer) ->
                serializeAnswer(answer)
            }
            
            val responseData = mapOf(
                "surveyId" to surveyId,
                "answers" to serializedAnswers,
                "timestamp" to System.currentTimeMillis(),
                "submittedAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
            )
            
            // Save to Firestore
            val docRef = db.collection("responses").add(responseData).await()
            
            Log.d("ResponseRepository", "Response submitted: ${docRef.id}")
            
            // Update response count
            updateResponseCount(surveyId)
            
            Result.success(docRef.id)
            
        } catch (e: Exception) {
            Log.e("ResponseRepository", "Error submitting response", e)
            Result.failure(
                Exception(
                    when {
                        e.message?.contains("PERMISSION_DENIED") == true -> 
                            "Ruxsat yo'q. Internet ulanishini tekshiring."
                        e.message?.contains("UNAVAILABLE") == true -> 
                            "Server mavjud emas. Keyinroq urinib ko'ring."
                        e.message?.contains("NETWORK") == true -> 
                            "Internet ulanishida xatolik."
                        else -> e.message ?: "Javoblarni yuborishda xatolik"
                    }
                )
            )
        }
    }
    
    private fun serializeAnswer(answer: AnswerValue): Map<String, Any?> {
        return when (answer) {
            is AnswerValue.Text -> mapOf("type" to "TEXT", "value" to answer.value)
            is AnswerValue.Number -> mapOf("type" to "NUMBER", "value" to answer.value)
            is AnswerValue.SingleChoice -> mapOf("type" to "SINGLE_CHOICE", "choiceId" to answer.choiceId)
            is AnswerValue.MultiChoice -> mapOf("type" to "MULTI_CHOICE", "choiceIds" to answer.choiceIds.toList())
            is AnswerValue.Rating -> mapOf("type" to "RATING", "value" to answer.value)
            is AnswerValue.Date -> mapOf("type" to "DATE", "value" to answer.value)
        }
    }
    
    private suspend fun updateResponseCount(surveyId: String) {
        try {
            val surveyRef = db.collection("surveys").document(surveyId)
            db.runTransaction { transaction ->
                val snapshot = transaction.get(surveyRef)
                val currentCount = snapshot.getLong("responseCount") ?: 0
                transaction.update(surveyRef, "responseCount", currentCount + 1)
            }.await()
        } catch (e: Exception) {
            Log.w("ResponseRepository", "Failed to update response count", e)
            // Non-critical, don't fail the submission
        }
    }
}
