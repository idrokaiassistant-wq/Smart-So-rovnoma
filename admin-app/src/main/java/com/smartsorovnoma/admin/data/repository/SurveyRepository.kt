package com.smartsorovnoma.admin.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.smartsorovnoma.admin.data.model.Question
import com.smartsorovnoma.admin.data.model.Survey
import kotlinx.coroutines.tasks.await

interface SurveyRepository {
    suspend fun getSurveys(): Result<List<Survey>>
    suspend fun getSurveyWithQuestions(surveyId: String): Result<Survey>
    suspend fun createSurvey(survey: Survey): Result<String>
    suspend fun updateSurvey(survey: Survey): Result<Unit>
    suspend fun togglePublish(surveyId: String, isPublished: Boolean): Result<Unit>
    suspend fun deleteSurvey(surveyId: String): Result<Unit>
}

class FirestoreSurveyRepository : SurveyRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val surveysCollection = firestore.collection("surveys")

    override suspend fun getSurveys(): Result<List<Survey>> {
        return try {
            val snapshot = surveysCollection
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            val surveys = snapshot.toObjects(Survey::class.java)
            Result.success(surveys)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSurveyWithQuestions(surveyId: String): Result<Survey> {
        return try {
            val surveyDoc = surveysCollection.document(surveyId).get().await()
            val survey = surveyDoc.toObject(Survey::class.java)
                ?: return Result.failure(Exception("So'rovnoma topilmadi"))

            val questionsSnapshot = surveysCollection.document(surveyId)
                .collection("questions")
                .orderBy("order")
                .get()
                .await()
            val questions = questionsSnapshot.toObjects(Question::class.java)

            Result.success(survey.copy(questions = questions))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createSurvey(survey: Survey): Result<String> {
        return try {
            val docRef = surveysCollection.document()
            val surveyId = docRef.id
            val newSurvey = survey.copy(id = surveyId)
            
            docRef.set(newSurvey.copy(questions = emptyList())).await()

            val batch = firestore.batch()
            survey.questions.forEachIndexed { index, question ->
                val qRef = docRef.collection("questions").document()
                val qWithId = question.copy(id = qRef.id, order = index)
                batch.set(qRef, qWithId)
            }
            batch.commit().await()

            Result.success(surveyId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateSurvey(survey: Survey): Result<Unit> {
        return try {
            val docRef = surveysCollection.document(survey.id)
            docRef.set(survey.copy(questions = emptyList())).await()

            val batch = firestore.batch()
            val existingQuestions = docRef.collection("questions").get().await()
             existingQuestions.documents.forEach { doc ->
                 batch.delete(doc.reference)
             }
             
             survey.questions.forEachIndexed { index, question ->
                 val qRef = if (question.id.isNotEmpty()) docRef.collection("questions").document(question.id) else docRef.collection("questions").document()
                 val qWithId = question.copy(id = qRef.id, order = index)
                 batch.set(qRef, qWithId)
             }
             
             batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun togglePublish(surveyId: String, isPublished: Boolean): Result<Unit> {
        return try {
            surveysCollection.document(surveyId)
                .update("isPublished", isPublished)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteSurvey(surveyId: String): Result<Unit> {
         return try {
             val questions = surveysCollection.document(surveyId).collection("questions").get().await()
             val batch = firestore.batch()
             questions.forEach { batch.delete(it.reference) }
             batch.delete(surveysCollection.document(surveyId))
             batch.commit().await()
             Result.success(Unit)
         } catch (e: Exception) {
             Result.failure(e)
         }
    }
}
