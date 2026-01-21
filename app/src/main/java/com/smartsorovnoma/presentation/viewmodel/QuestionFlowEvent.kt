package com.smartsorovnoma.presentation.viewmodel

/**
 * QuestionFlow uchun UI events
 */
sealed class QuestionFlowEvent {
    object GoNext : QuestionFlowEvent()
    object GoBack : QuestionFlowEvent()
    data class UpdateAnswer(val questionId: String, val answer: AnswerValue) : QuestionFlowEvent()
    object NavigateToReview : QuestionFlowEvent()
}

/**
 * Navigation effect (one-time events)
 */
sealed class QuestionFlowEffect {
    object NavigateToReview : QuestionFlowEffect()
    object NavigateBack : QuestionFlowEffect()
}
