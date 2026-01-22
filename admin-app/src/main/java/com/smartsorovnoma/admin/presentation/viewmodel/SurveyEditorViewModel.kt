package com.smartsorovnoma.admin.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartsorovnoma.admin.data.model.Question
import com.smartsorovnoma.admin.data.model.Survey
import com.smartsorovnoma.admin.data.repository.SurveyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import com.smartsorovnoma.admin.di.RepositoryProvider

sealed class EditorUiState {
    object Loading : EditorUiState()
    data class Content(val survey: Survey) : EditorUiState()
    data class Error(val message: String) : EditorUiState()
    object Saved : EditorUiState()
}

class SurveyEditorViewModel : ViewModel() {
    private val repository = RepositoryProvider.getSurveyRepository()

    private val _uiState = MutableStateFlow<EditorUiState>(EditorUiState.Loading)
    val uiState: StateFlow<EditorUiState> = _uiState.asStateFlow()

    fun loadSurvey(surveyId: String?) {
        viewModelScope.launch {
            if (surveyId.isNullOrEmpty()) {
                _uiState.value = EditorUiState.Content(Survey())
            } else {
                _uiState.value = EditorUiState.Loading
                val result = repository.getSurveyWithQuestions(surveyId)
                if (result.isSuccess) {
                    _uiState.value = EditorUiState.Content(result.getOrThrow())
                } else {
                    _uiState.value = EditorUiState.Error(result.exceptionOrNull()?.message ?: "Failed to load")
                }
            }
        }
    }

    fun updateTitle(title: String) {
        val currentState = _uiState.value
        if (currentState is EditorUiState.Content) {
            _uiState.value = currentState.copy(survey = currentState.survey.copy(title = title))
        }
    }

    fun updateDescription(description: String) {
        val currentState = _uiState.value
        if (currentState is EditorUiState.Content) {
            _uiState.value = currentState.copy(survey = currentState.survey.copy(description = description))
        }
    }

    fun addQuestion(question: Question) {
        val currentState = _uiState.value
        if (currentState is EditorUiState.Content) {
            val newQuestions = currentState.survey.questions.toMutableList().apply { add(question) }
            _uiState.value = currentState.copy(survey = currentState.survey.copy(questions = newQuestions))
        }
    }

    fun removeQuestion(question: Question) {
        val currentState = _uiState.value
        if (currentState is EditorUiState.Content) {
            val newQuestions = currentState.survey.questions.toMutableList().apply { remove(question) }
            _uiState.value = currentState.copy(survey = currentState.survey.copy(questions = newQuestions))
        }
    }
    
    fun updateQuestion(index: Int, question: Question) {
        val currentState = _uiState.value
        if (currentState is EditorUiState.Content) {
            val newQuestions = currentState.survey.questions.toMutableList().apply { 
                set(index, question)
            }
            _uiState.value = currentState.copy(survey = currentState.survey.copy(questions = newQuestions))
        }
    }

    fun saveSurvey() {
        val currentState = _uiState.value
        if (currentState is EditorUiState.Content) {
            viewModelScope.launch {
                val result = if (currentState.survey.id.isEmpty()) {
                    repository.createSurvey(currentState.survey)
                } else {
                    repository.updateSurvey(currentState.survey)
                }

                if (result.isSuccess) {
                    _uiState.value = EditorUiState.Saved
                } else {
                    _uiState.value = EditorUiState.Error(result.exceptionOrNull()?.message ?: "Failed to save")
                    // Revert to content after error?
                    // Keep error state for UI to show snackbar, then manual reset or use channel.
                }
            }
        }
    }
}
