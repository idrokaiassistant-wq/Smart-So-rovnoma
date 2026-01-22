package com.smartsorovnoma.admin.di

import com.smartsorovnoma.admin.data.repository.FirestoreSurveyRepository
import com.smartsorovnoma.admin.data.repository.MockSurveyRepository
import com.smartsorovnoma.admin.data.repository.SurveyRepository

object RepositoryProvider {
    private var isDemoMode = false
    
    private val firestoreRepository by lazy { FirestoreSurveyRepository() }
    private val mockRepository by lazy { MockSurveyRepository() }

    fun enableDemoMode(enable: Boolean) {
        isDemoMode = enable
    }

    fun getSurveyRepository(): SurveyRepository {
        return if (isDemoMode) mockRepository else firestoreRepository
    }
}
