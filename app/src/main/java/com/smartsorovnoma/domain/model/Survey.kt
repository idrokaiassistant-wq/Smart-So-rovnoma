package com.smartsorovnoma.domain.model

data class Survey(
    val id: String,
    val title: String,
    val description: String,
    val isActive: Boolean,
    val questions: List<Question> = emptyList()
)
