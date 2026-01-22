package com.smartsorovnoma.admin.data.model

import com.google.firebase.Timestamp

enum class QuestionType {
    SINGLE_CHOICE,
    MULTIPLE_CHOICE,
    TEXT
}

data class Question(
    val id: String = "",
    val text: String = "",
    val type: QuestionType = QuestionType.SINGLE_CHOICE,
    val options: List<String> = emptyList(),
    val order: Int = 0
)

data class Survey(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val isPublished: Boolean = false,
    val createdAt: Timestamp = Timestamp.now(),
    val questions: List<Question> = emptyList() // Depending on structure, questions might be a subcollection
)
