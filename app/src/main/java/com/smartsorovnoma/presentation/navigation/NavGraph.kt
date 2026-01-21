package com.smartsorovnoma.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.smartsorovnoma.presentation.screen.QuestionFlowScreen
import com.smartsorovnoma.presentation.screen.ReviewScreen
import com.smartsorovnoma.presentation.screen.SuccessScreen
import com.smartsorovnoma.presentation.screen.SurveyDetailScreen
import com.smartsorovnoma.presentation.screen.MainScreen
import com.smartsorovnoma.presentation.viewmodel.QuestionFlowViewModel
import com.smartsorovnoma.presentation.viewmodel.ThemeViewModel

object Routes {
    const val MAIN = "main"
    const val SURVEY_DETAIL = "survey_detail/{surveyId}"
    const val QUESTION_FLOW = "question_flow/{surveyId}"
    const val REVIEW = "review/{surveyId}"
    const val SUCCESS = "success"
    
    fun surveyDetail(surveyId: String) = "survey_detail/$surveyId"
    fun questionFlow(surveyId: String) = "question_flow/$surveyId"
    fun review(surveyId: String) = "review/$surveyId"
}

@Composable
fun NavGraph(themeViewModel: ThemeViewModel) {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Routes.MAIN
    ) {
        // Main screen with bottom navigation
        composable(Routes.MAIN) {
            MainScreen(
                onSurveyClick = { surveyId ->
                    navController.navigate(Routes.surveyDetail(surveyId))
                },
                themeViewModel = themeViewModel
            )
        }
        
        // Survey detail screen
        composable(
            route = Routes.SURVEY_DETAIL,
            arguments = listOf(
                navArgument("surveyId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val surveyId = backStackEntry.arguments?.getString("surveyId") ?: ""
            SurveyDetailScreen(
                surveyId = surveyId,
                onBackClick = { navController.popBackStack() },
                onStartClick = { 
                    navController.navigate(Routes.questionFlow(surveyId))
                }
            )
        }
        
        // Question flow screen
        composable(
            route = Routes.QUESTION_FLOW,
            arguments = listOf(
                navArgument("surveyId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val surveyId = backStackEntry.arguments?.getString("surveyId") ?: ""
            QuestionFlowScreen(
                surveyId = surveyId,
                onBackClick = { navController.popBackStack() },
                onReviewClick = {
                    navController.navigate(Routes.review(surveyId))
                }
            )
        }
        
        // Review screen
        composable(
            route = Routes.REVIEW,
            arguments = listOf(
                navArgument("surveyId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val surveyId = backStackEntry.arguments?.getString("surveyId") ?: ""
            val questionFlowEntry = remember(surveyId) {
                navController.getBackStackEntry(Routes.QUESTION_FLOW)
            }
            val sharedViewModel: QuestionFlowViewModel = viewModel(
                viewModelStoreOwner = questionFlowEntry,
                factory = QuestionFlowViewModel.Factory(
                    androidx.compose.ui.platform.LocalContext.current.applicationContext as android.app.Application,
                    surveyId
                )
            )
            ReviewScreen(
                surveyId = surveyId,
                onBackClick = { navController.popBackStack() },
                onSubmitClick = {
                    navController.navigate(Routes.SUCCESS) {
                        popUpTo(Routes.MAIN) { inclusive = false }
                    }
                },
                viewModel = sharedViewModel
            )
        }
        
        // Success screen
        composable(Routes.SUCCESS) {
            SuccessScreen(
                onHomeClick = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.MAIN) { inclusive = true }
                    }
                }
            )
        }
    }
}
