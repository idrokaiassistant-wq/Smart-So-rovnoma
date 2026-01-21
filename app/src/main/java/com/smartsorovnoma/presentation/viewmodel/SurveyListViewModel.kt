package com.smartsorovnoma.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartsorovnoma.data.repository.SurveyRepository
import com.smartsorovnoma.domain.model.Survey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

data class SurveyListUiState(
    val surveys: List<Survey> = emptyList(),
    val filteredSurveys: List<Survey> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = true,
    val error: String? = null,
    val isRefreshing: Boolean = false
)

class SurveyListViewModel(
    private val repository: SurveyRepository = SurveyRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(SurveyListUiState())
    val uiState: StateFlow<SurveyListUiState> = _uiState.asStateFlow()

    init {
        fetchActiveSurveys()
    }
    
    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        filterSurveys()
    }
    
    private fun filterSurveys() {
        val query = _uiState.value.searchQuery.lowercase()
        val filtered = if (query.isBlank()) {
            _uiState.value.surveys
        } else {
            _uiState.value.surveys.filter { survey ->
                survey.title.lowercase().contains(query) ||
                survey.description.lowercase().contains(query)
            }
        }
        _uiState.value = _uiState.value.copy(filteredSurveys = filtered)
    }

    private fun fetchActiveSurveys() {
        viewModelScope.launch {
            repository.getActiveSurveys()
                .onStart {
                    _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                }
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isRefreshing = false,
                        error = e.message ?: "Xatolik yuz berdi"
                    )
                }
                .collect { surveyList ->
                    _uiState.value = _uiState.value.copy(
                        surveys = surveyList,
                        filteredSurveys = surveyList,
                        isLoading = false,
                        isRefreshing = false,
                        error = null
                    )
                    filterSurveys()
                }
        }
    }
    
    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            repository.getActiveSurveys()
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        error = e.message ?: "Xatolik yuz berdi"
                    )
                }
                .collect { surveyList ->
                    _uiState.value = _uiState.value.copy(
                        surveys = surveyList,
                        filteredSurveys = surveyList,
                        isRefreshing = false,
                        error = null
                    )
                    filterSurveys()
                }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
