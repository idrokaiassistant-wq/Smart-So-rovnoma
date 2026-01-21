package com.smartsorovnoma.domain.model

data class Question(
    val id: String,
    val surveyId: String,
    val order: Int,
    val type: QuestionType,
    val text: String,
    val required: Boolean,
    val choices: List<Choice>? = null
)
