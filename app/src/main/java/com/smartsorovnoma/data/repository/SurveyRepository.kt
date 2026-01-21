package com.smartsorovnoma.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.smartsorovnoma.domain.model.Question
import com.smartsorovnoma.domain.model.QuestionType
import com.smartsorovnoma.domain.model.Survey
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class SurveyRepository {
    private val db = FirebaseFirestore.getInstance()
    private val surveysCollection = db.collection("surveys")

    private fun parseQuestionType(typeValue: String?): QuestionType {
        return runCatching { QuestionType.valueOf(typeValue ?: "TEXT") }
            .getOrElse {
                Log.w("SurveyRepository", "Unknown question type: $typeValue, defaulting to TEXT")
                QuestionType.TEXT
            }
    }

    fun getActiveSurveys(): Flow<List<Survey>> = callbackFlow {
        Log.d("SurveyRepository", "Setting up surveys listener...")
        // Simplified query - filter isActive client-side to avoid composite index requirement
        val listener = surveysCollection
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("SurveyRepository", "Error fetching surveys", error)
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    Log.d("SurveyRepository", "Got ${snapshot.documents.size} documents")
                    val surveys = snapshot.documents.mapNotNull { doc ->
                        try {
                            val id = doc.id
                            val title = doc.getString("title") ?: ""
                            val description = doc.getString("description") ?: ""
                            val isActive = doc.getBoolean("isActive") ?: false

                            // Parse Questions
                            val questionsList = (doc.get("questions") as? List<Map<String, Any>>)?.mapIndexedNotNull { index, qMap ->
                                try {
                                    val qId = qMap["id"] as? String ?: ""
                                    // Map options (List<String>) to List<Choice>
                                    val optionsStrings = (qMap["options"] as? List<String>) ?: emptyList()
                                    val choicesList = optionsStrings.mapIndexed { idx, optText ->
                                        com.smartsorovnoma.domain.model.Choice(
                                            id = "${qId}_opt_$idx",
                                            questionId = qId,
                                            text = optText,
                                            value = idx
                                        )
                                    }

                                    Question(
                                        id = qId,
                                        surveyId = id, // Set parent survey ID
                                        order = index, // Set order based on list index
                                        text = qMap["text"] as? String ?: "",
                                        type = parseQuestionType(qMap["type"] as? String),
                                        choices = choicesList,
                                        required = qMap["required"] as? Boolean ?: false
                                    )
                                } catch (e: Exception) {
                                    Log.e("SurveyRepository", "Error parsing question", e)
                                    null
                                }
                            } ?: emptyList()

                            Survey(
                                id = id,
                                title = title,
                                description = description,
                                isActive = isActive,
                                questions = questionsList
                            )
                        } catch (e: Exception) {
                            Log.e("SurveyRepository", "Error parsing survey ${doc.id}", e)
                            null
                        }
                    }.filter { it.isActive } // Filter active surveys client-side

                    Log.d("SurveyRepository", "Sending ${surveys.size} active surveys")
                    trySend(surveys)
                }
            }

        awaitClose { listener.remove() }
    }

    suspend fun getSurveyById(surveyId: String): Survey? {
        return try {
            val doc = surveysCollection.document(surveyId).get().await()
            if (doc.exists()) {
                val questionsList = (doc.get("questions") as? List<Map<String, Any>>)?.mapIndexedNotNull { index, qMap ->
                    try {
                        val qId = qMap["id"] as? String ?: ""
                        val optionsStrings = (qMap["options"] as? List<String>) ?: emptyList()
                        val choicesList = optionsStrings.mapIndexed { idx, optText ->
                            com.smartsorovnoma.domain.model.Choice(
                                id = "${qId}_opt_$idx",
                                questionId = qId,
                                text = optText,
                                value = idx
                            )
                        }

                        Question(
                            id = qId,
                            surveyId = doc.id,
                            order = index,
                            text = qMap["text"] as? String ?: "",
                            type = parseQuestionType(qMap["type"] as? String),
                            choices = choicesList,
                            required = qMap["required"] as? Boolean ?: false
                        )
                    } catch (e: Exception) {
                        Log.e("SurveyRepository", "Error parsing question", e)
                        null
                    }
                } ?: emptyList()

                Survey(
                    id = doc.id,
                    title = doc.getString("title") ?: "",
                    description = doc.getString("description") ?: "",
                    isActive = doc.getBoolean("isActive") ?: false,
                    questions = questionsList
                )
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("SurveyRepository", "Error generating survey by id", e)
            null
        }
    }
}
