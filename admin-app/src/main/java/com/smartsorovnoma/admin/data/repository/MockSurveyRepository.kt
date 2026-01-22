package com.smartsorovnoma.admin.data.repository

import com.smartsorovnoma.admin.data.model.Question
import com.smartsorovnoma.admin.data.model.QuestionType
import com.smartsorovnoma.admin.data.model.Survey
import java.util.UUID

class MockSurveyRepository : SurveyRepository {
    private val surveys = mutableListOf<Survey>(
        Survey(
            id = "mock_1",
            title = "Demo so'rovnoma 1",
            description = "Admin interfeysini sinash uchun demo so'rovnoma.",
            isPublished = true,
            questions = listOf(
                Question("q1", "Sevimli rangingiz qaysi?", QuestionType.SINGLE_CHOICE, listOf("Qizil", "Ko'k", "Yashil"), 0),
                Question("q2", "Tajribangizni tasvirlab bering", QuestionType.TEXT, emptyList(), 1)
            )
        ),
        Survey(
            id = "mock_2",
            title = "Qoralama so'rovnoma",
            description = "Bu so'rovnoma hali nashr qilinmagan.",
            isPublished = false,
            questions = listOf(
                Question("q3", "Mos keladiganlarni tanlang", QuestionType.MULTIPLE_CHOICE, listOf("Variant A", "Variant B", "Variant C"), 0)
            )
        )
    )

    override suspend fun getSurveys(): Result<List<Survey>> {
        return Result.success(surveys.toList())
    }

    override suspend fun getSurveyWithQuestions(surveyId: String): Result<Survey> {
        val survey = surveys.find { it.id == surveyId }
        return if (survey != null) Result.success(survey) else Result.failure(Exception("Topilmadi"))
    }

    override suspend fun createSurvey(survey: Survey): Result<String> {
        val newId = UUID.randomUUID().toString()
        val newSurvey = survey.copy(id = newId)
        surveys.add(0, newSurvey)
        return Result.success(newId)
    }

    override suspend fun updateSurvey(survey: Survey): Result<Unit> {
        val index = surveys.indexOfFirst { it.id == survey.id }
        if (index != -1) {
            surveys[index] = survey
            return Result.success(Unit)
        }
        return Result.failure(Exception("Topilmadi"))
    }

    override suspend fun togglePublish(surveyId: String, isPublished: Boolean): Result<Unit> {
        val index = surveys.indexOfFirst { it.id == surveyId }
        if (index != -1) {
            surveys[index] = surveys[index].copy(isPublished = isPublished)
            return Result.success(Unit)
        }
        return Result.failure(Exception("Topilmadi"))
    }

    override suspend fun deleteSurvey(surveyId: String): Result<Unit> {
        surveys.removeIf { it.id == surveyId }
        return Result.success(Unit)
    }
}
