package com.smartsorovnoma.domain.model

data class Choice(
    val id: String,
    val questionId: String,
    val text: String,
    val value: Int
)
