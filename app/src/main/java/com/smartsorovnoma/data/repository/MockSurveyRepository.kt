package com.smartsorovnoma.data.repository

import com.smartsorovnoma.domain.model.Choice
import com.smartsorovnoma.domain.model.Question
import com.smartsorovnoma.domain.model.QuestionType
import com.smartsorovnoma.domain.model.Survey
import com.smartsorovnoma.domain.model.SurveyDetail
import com.smartsorovnoma.domain.repository.SurveyRepository

class MockSurveyRepository : SurveyRepository {
    
    private val surveys = listOf(
        Survey(
            id = "1",
            title = "Mijozlar qoniqish so'rovnomasi",
            description = "Xizmat sifatini baholash uchun qisqa so'rovnoma",
            isActive = true
        ),
        Survey(
            id = "2",
            title = "Hodimlar fikr-mulohazasi",
            description = "Ish joyidagi muhit haqida anonim so'rovnoma",
            isActive = true
        ),
        Survey(
            id = "3",
            title = "Mahsulot tadqiqoti",
            description = "Yangi mahsulot haqida fikringizni bildiring",
            isActive = true
        ),
        Survey(
            id = "4",
            title = "Tadbir qatnashchilar fikri",
            description = "Konferensiya haqida so'rovnoma",
            isActive = true
        ),
        Survey(
            id = "5",
            title = "Ta'lim sifati baholash",
            description = "O'quv kursini baholang",
            isActive = false
        )
    )
    
    private val questionsMap = mapOf(
        "1" to listOf(
            Question(
                id = "q1_1",
                surveyId = "1",
                order = 1,
                type = QuestionType.TEXT,
                text = "Ismingizni kiriting",
                required = true
            ),
            Question(
                id = "q1_2",
                surveyId = "1",
                order = 2,
                type = QuestionType.RATING,
                text = "Xizmat sifatini 1 dan 5 gacha baholang",
                required = true
            ),
            Question(
                id = "q1_3",
                surveyId = "1",
                order = 3,
                type = QuestionType.SINGLE_CHOICE,
                text = "Bizni qayerdan topdingiz?",
                required = true,
                choices = listOf(
                    Choice("c1_1", "q1_3", "Internet", 1),
                    Choice("c1_2", "q1_3", "Do'stlar tavsiyasi", 2),
                    Choice("c1_3", "q1_3", "Reklama", 3),
                    Choice("c1_4", "q1_3", "Boshqa", 4)
                )
            ),
            Question(
                id = "q1_4",
                surveyId = "1",
                order = 4,
                type = QuestionType.MULTI_CHOICE,
                text = "Qaysi xizmatlardan foydalandingiz?",
                required = false,
                choices = listOf(
                    Choice("c2_1", "q1_4", "Konsultatsiya", 1),
                    Choice("c2_2", "q1_4", "Texnik yordam", 2),
                    Choice("c2_3", "q1_4", "O'rnatish", 3),
                    Choice("c2_4", "q1_4", "Ta'mirlash", 4)
                )
            ),
            Question(
                id = "q1_5",
                surveyId = "1",
                order = 5,
                type = QuestionType.NUMBER,
                text = "Necha marta xizmatimizdan foydalangansiz?",
                required = true
            )
        ),
        "2" to listOf(
            Question(
                id = "q2_1",
                surveyId = "2",
                order = 1,
                type = QuestionType.TEXT,
                text = "Qaysi bo'limda ishlaysiz?",
                required = true
            ),
            Question(
                id = "q2_2",
                surveyId = "2",
                order = 2,
                type = QuestionType.RATING,
                text = "Ish joyidagi muhitni qanday baholaysiz?",
                required = true
            ),
            Question(
                id = "q2_3",
                surveyId = "2",
                order = 3,
                type = QuestionType.SINGLE_CHOICE,
                text = "Rahbariyat bilan muloqot qanday?",
                required = true,
                choices = listOf(
                    Choice("c3_1", "q2_3", "Juda yaxshi", 1),
                    Choice("c3_2", "q2_3", "Yaxshi", 2),
                    Choice("c3_3", "q2_3", "O'rtacha", 3),
                    Choice("c3_4", "q2_3", "Yomon", 4)
                )
            ),
            Question(
                id = "q2_4",
                surveyId = "2",
                order = 4,
                type = QuestionType.TEXT,
                text = "Yaxshilash uchun takliflaringiz bormi?",
                required = false
            )
        ),
        "3" to listOf(
            Question(
                id = "q3_1",
                surveyId = "3",
                order = 1,
                type = QuestionType.TEXT,
                text = "Mahsulotimiz nomini bilasizmi?",
                required = true
            ),
            Question(
                id = "q3_2",
                surveyId = "3",
                order = 2,
                type = QuestionType.RATING,
                text = "Mahsulot sifatini baholang",
                required = true
            ),
            Question(
                id = "q3_3",
                surveyId = "3",
                order = 3,
                type = QuestionType.MULTI_CHOICE,
                text = "Qaysi xususiyatlar yoqdi?",
                required = true,
                choices = listOf(
                    Choice("c4_1", "q3_3", "Dizayn", 1),
                    Choice("c4_2", "q3_3", "Narx", 2),
                    Choice("c4_3", "q3_3", "Sifat", 3),
                    Choice("c4_4", "q3_3", "Funksionallik", 4),
                    Choice("c4_5", "q3_3", "Qulaylik", 5)
                )
            ),
            Question(
                id = "q3_4",
                surveyId = "3",
                order = 4,
                type = QuestionType.TEXT,
                text = "Qo'shimcha fikr-mulohazalaringiz",
                required = false
            )
        ),
        "4" to listOf(
            Question(
                id = "q4_1",
                surveyId = "4",
                order = 1,
                type = QuestionType.RATING,
                text = "Tadbirni umumiy baholang",
                required = true
            ),
            Question(
                id = "q4_2",
                surveyId = "4",
                order = 2,
                type = QuestionType.SINGLE_CHOICE,
                text = "Eng yoqqan qism qaysi?",
                required = true,
                choices = listOf(
                    Choice("c5_1", "q4_2", "Ma'ruzalar", 1),
                    Choice("c5_2", "q4_2", "Networking", 2),
                    Choice("c5_3", "q4_2", "Workshop", 3),
                    Choice("c5_4", "q4_2", "Sovg'alar", 4)
                )
            ),
            Question(
                id = "q4_3",
                surveyId = "4",
                order = 3,
                type = QuestionType.TEXT,
                text = "Keyingi tadbirlar uchun takliflar",
                required = false
            )
        )
    )
    
    override fun getSurveys(): List<Survey> {
        return surveys.filter { it.isActive }
    }
    
    override fun getSurveyById(id: String): Survey? {
        return surveys.find { it.id == id }
    }
    
    override fun getSurveyDetail(surveyId: String): SurveyDetail? {
        val survey = getSurveyById(surveyId) ?: return null
        val questions = questionsMap[surveyId] ?: emptyList()
        return SurveyDetail(survey, questions)
    }
}
