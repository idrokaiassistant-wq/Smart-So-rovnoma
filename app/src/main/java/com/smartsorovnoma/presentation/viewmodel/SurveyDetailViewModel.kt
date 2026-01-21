package com.smartsorovnoma.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.smartsorovnoma.data.repository.SurveyRepository
import com.smartsorovnoma.domain.model.Survey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SurveyDetailViewModel(
    private val surveyId: String,
    private val repository: SurveyRepository = SurveyRepository()
) : ViewModel() {

    private val _survey = MutableStateFlow<Survey?>(null)
    val survey: StateFlow<Survey?> = _survey.asStateFlow()

    init {
        loadSurvey(surveyId)
    }

    private fun loadSurvey(surveyId: String) {
        viewModelScope.launch {
            val result = repository.getSurveyById(surveyId)
            _survey.value = result
        }
    }

    class Factory(private val surveyId: String) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SurveyDetailViewModel(surveyId) as T
        }
    }
}
