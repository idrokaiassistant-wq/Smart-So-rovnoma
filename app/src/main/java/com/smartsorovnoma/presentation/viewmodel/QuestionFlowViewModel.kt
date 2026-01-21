package com.smartsorovnoma.presentation.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.smartsorovnoma.data.repository.SurveyRepository
import com.smartsorovnoma.data.service.ResponseSubmissionService
import com.smartsorovnoma.data.service.SubmitResult
import com.smartsorovnoma.domain.model.Question
import com.smartsorovnoma.domain.model.Survey
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import androidx.lifecycle.ViewModel

data class QuestionFlowUiState(
    val survey: Survey? = null,
    val questions: List<Question> = emptyList(),
    val currentIndex: Int = 0,
    val answers: Map<String, AnswerValue> = emptyMap(),
    val currentError: String? = null,
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val submitError: String? = null,
    val cooldownSeconds: Int = 0
)

sealed class AnswerValue {
    data class Text(val value: String) : AnswerValue()
    data class Number(val value: Int?) : AnswerValue()
    data class SingleChoice(val choiceId: String) : AnswerValue()
    data class MultiChoice(val choiceIds: Set<String>) : AnswerValue()
    data class Rating(val value: Int) : AnswerValue()
    data class Date(val value: Long) : AnswerValue()
}

class QuestionFlowViewModel(
    application: Application,
    private val surveyId: String,
    private val repository: SurveyRepository = SurveyRepository(),
    private val responseService: ResponseSubmissionService = ResponseSubmissionService()
) : AndroidViewModel(application) {
    
    private val _uiState = MutableStateFlow(QuestionFlowUiState(isLoading = true))
    val uiState: StateFlow<QuestionFlowUiState> = _uiState.asStateFlow()
    
    private val _submitSuccess = MutableStateFlow(false)
    val submitSuccess: StateFlow<Boolean> = _submitSuccess.asStateFlow()
    
    // Anti-Spam
    private val COOLDOWN_MS = 60_000L // 1 minute cooldown
    private val PREFS_NAME = "smart_sorovnoma_prefs"
    private val KEY_LAST_SUBMIT = "last_submit_time"
    
    init {
        loadQuestions(surveyId)
        checkCooldown()
    }
    
    private fun checkCooldown() {
        val prefs = getApplication<Application>().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val lastSubmitTime = prefs.getLong(KEY_LAST_SUBMIT, 0)
        val currentTime = System.currentTimeMillis()
        val timePassed = currentTime - lastSubmitTime
        
        if (timePassed < COOLDOWN_MS) {
            val remainingSeconds = ((COOLDOWN_MS - timePassed) / 1000).toInt()
            startCooldownTimer(remainingSeconds)
        }
    }
    
    private fun startCooldownTimer(initialSeconds: Int) {
        viewModelScope.launch {
            for (i in initialSeconds downTo 0) {
                _uiState.update { it.copy(cooldownSeconds = i) }
                if (i > 0) delay(1000)
            }
        }
    }
    
    private fun loadQuestions(surveyId: String) {
        viewModelScope.launch {
            try {
                val survey = repository.getSurveyById(surveyId)
                _uiState.update {
                    it.copy(
                        survey = survey,
                        questions = survey?.questions ?: emptyList(),
                        isLoading = false,
                        currentError = if (survey == null) "So'rovnoma topilmadi" else null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        survey = null,
                        questions = emptyList(),
                        isLoading = false,
                        currentError = e.message ?: "Savollarni yuklashda xatolik"
                    )
                }
            }
        }
    }

    fun reload() {
        _uiState.update {
            it.copy(
                isLoading = true,
                currentError = null
            )
        }
        loadQuestions(surveyId)
    }
    
    fun getCurrentQuestion(): Question? {
        val state = _uiState.value
        return if (state.currentIndex in state.questions.indices) {
            state.questions[state.currentIndex]
        } else null
    }
    
    fun updateAnswer(questionId: String, answer: AnswerValue) {
        _uiState.update { state ->
            state.copy(
                answers = state.answers + (questionId to answer),
                currentError = null
            )
        }
    }
    
    fun goNext(): Boolean {
        val state = _uiState.value
        val currentQuestion = getCurrentQuestion() ?: return false
        
        // Validation
        if (currentQuestion.required) {
            val answer = state.answers[currentQuestion.id]
            if (!isAnswerValid(answer)) {
                _uiState.update { it.copy(currentError = "Bu savol majburiy") }
                return false
            }
        }
        
        // Move to next
        if (state.currentIndex < state.questions.size - 1) {
            _uiState.update { 
                it.copy(
                    currentIndex = it.currentIndex + 1,
                    currentError = null
                )
            }
            return false
        }
        
        // Last question - ready for review
        return true
    }
    
    fun goBack() {
        _uiState.update { state ->
            if (state.currentIndex > 0) {
                state.copy(
                    currentIndex = state.currentIndex - 1,
                    currentError = null
                )
            } else state
        }
    }
    
    fun isLastQuestion(): Boolean {
        val state = _uiState.value
        return state.currentIndex == state.questions.size - 1
    }
    
    private fun isAnswerValid(answer: AnswerValue?): Boolean {
        return when (answer) {
            is AnswerValue.Text -> answer.value.isNotBlank()
            is AnswerValue.Number -> answer.value != null
            is AnswerValue.SingleChoice -> answer.choiceId.isNotBlank()
            is AnswerValue.MultiChoice -> answer.choiceIds.isNotEmpty()
            is AnswerValue.Rating -> answer.value in 1..5
            is AnswerValue.Date -> answer.value > 0
            null -> false
        }
    }
    
    /**
     * Javoblarni Firebase'ga yuborish
     */
    suspend fun submitResponses(): Boolean {
        // Anti-Spam Check
        if (_uiState.value.cooldownSeconds > 0) {
            _uiState.update { 
                it.copy(
                    isSubmitting = false, 
                    submitError = "Iltimos, qayta yuborishdan oldin ${_uiState.value.cooldownSeconds} soniya kuting."
                ) 
            }
            return false
        }

        _uiState.update { it.copy(isSubmitting = true, submitError = null) }
        
        return try {
            val result = responseService.submitResponses(surveyId, _uiState.value.answers)
            
            when (result) {
                is SubmitResult.Success -> {
                    // Save timestamp
                    val currentTime = System.currentTimeMillis()
                    val prefs = getApplication<Application>().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                    prefs.edit().putLong(KEY_LAST_SUBMIT, currentTime).apply()
                    
                    startCooldownTimer(60) // Start timer for UI
                    
                    _uiState.update { it.copy(isSubmitting = false) }
                    _submitSuccess.value = true
                    true
                }
                is SubmitResult.Error -> {
                    _uiState.update { 
                        it.copy(
                            isSubmitting = false, 
                            submitError = result.message
                        ) 
                    }
                    false
                }
            }
        } catch (e: Exception) {
            _uiState.update { 
                it.copy(
                    isSubmitting = false, 
                    submitError = e.message ?: "Javoblarni yuborishda xatolik"
                ) 
            }
            false
        }
    }
    
    fun clearSubmitError() {
        _uiState.update { it.copy(submitError = null) }
    }
    
    class Factory(private val application: Application, private val surveyId: String) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            return QuestionFlowViewModel(application, surveyId) as T
        }
    }
}
