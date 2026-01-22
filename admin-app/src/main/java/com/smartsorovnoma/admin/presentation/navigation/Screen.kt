package com.smartsorovnoma.admin.presentation.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Dashboard : Screen("dashboard")
    object SurveyEditor : Screen("survey_editor?surveyId={surveyId}") {
        fun createRoute(surveyId: String?) = "survey_editor?surveyId=${surveyId ?: ""}"
    }
}
