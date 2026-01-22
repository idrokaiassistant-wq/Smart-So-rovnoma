package com.smartsorovnoma.admin.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.smartsorovnoma.admin.presentation.screen.DashboardScreen
import com.smartsorovnoma.admin.presentation.screen.LoginScreen
import com.smartsorovnoma.admin.presentation.screen.SurveyEditorScreen
import com.smartsorovnoma.admin.presentation.viewmodel.AuthViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel() // Shared ViewModel for Auth state if needed, but LoginScreen uses its own.
    // Ideally AuthViewModel should be hoisted or singleton to persist state.
    // For this simple app, we can check auth state in MainActivity and decide start destination.
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Dashboard.route) {
            val dashboardViewModel: AuthViewModel = viewModel()
            DashboardScreen(
                onNavigateToEditor = { surveyId ->
                    navController.navigate(Screen.SurveyEditor.createRoute(surveyId ?: ""))
                },
                onLogout = {
                    dashboardViewModel.signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = Screen.SurveyEditor.route,
            arguments = listOf(navArgument("surveyId") { type = NavType.StringType; nullable = true; defaultValue = "" })
        ) { backStackEntry ->
            val surveyId = backStackEntry.arguments?.getString("surveyId")?.takeIf { it.isNotEmpty() }
            SurveyEditorScreen(
                surveyId = surveyId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
