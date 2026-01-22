package com.smartsorovnoma.admin.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartsorovnoma.admin.data.model.Survey
import com.smartsorovnoma.admin.data.repository.SurveyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import com.smartsorovnoma.admin.di.RepositoryProvider

sealed class DashboardUiState {
    object Loading : DashboardUiState()
    data class Success(val surveys: List<Survey>) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}

class DashboardViewModel : ViewModel() {
    private val repository = RepositoryProvider.getSurveyRepository()

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadSurveys()
    }

    fun loadSurveys() {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            val result = repository.getSurveys()
            if (result.isSuccess) {
                _uiState.value = DashboardUiState.Success(result.getOrDefault(emptyList()))
            } else {
                _uiState.value = DashboardUiState.Error(result.exceptionOrNull()?.message ?: "Failed to load surveys")
            }
        }
    }

    fun togglePublish(survey: Survey) {
        viewModelScope.launch {
            repository.togglePublish(survey.id, !survey.isPublished)
            loadSurveys() // Refresh
        }
    }
    
    fun deleteSurvey(surveyId: String) {
        viewModelScope.launch {
            repository.deleteSurvey(surveyId)
            loadSurveys()
        }
    }
}
