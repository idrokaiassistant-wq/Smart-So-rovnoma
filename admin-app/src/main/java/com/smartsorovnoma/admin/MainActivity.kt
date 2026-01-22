package com.smartsorovnoma.admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.smartsorovnoma.admin.presentation.navigation.NavGraph
import com.smartsorovnoma.admin.presentation.viewmodel.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smartsorovnoma.admin.presentation.viewmodel.AuthState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val colorScheme = lightColorScheme(
                primary = Color(0xFF4F46E5),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFE0E7FF),
                onPrimaryContainer = Color(0xFF1E1B4B),
                secondary = Color(0xFF0F172A),
                onSecondary = Color.White,
                tertiary = Color(0xFF0EA5E9),
                onTertiary = Color.White,
                background = Color(0xFFF8FAFC),
                onBackground = Color(0xFF0F172A),
                surface = Color(0xFFFFFFFF),
                onSurface = Color(0xFF0F172A),
                surfaceVariant = Color(0xFFF1F5F9),
                onSurfaceVariant = Color(0xFF475569),
                outline = Color(0xFFCBD5F5),
                error = Color(0xFFDC2626),
                onError = Color.White
            )

            MaterialTheme(colorScheme = colorScheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val authViewModel: AuthViewModel = viewModel()
                    val authState by authViewModel.authState.collectAsState()

                    // Determine start destination based on auth state if we wanted to skip login
                    // But NavGraph defaults to Login. LoginScreen checks authState and navigates if already authenticated.
                    // However, LoginScreen's LaunchedEffect might trigger navigation too late or redundantly.
                    // A better approach is to check here or let LoginScreen handle it.
                    // Given the simple NavGraph, let's just use NavGraph. 
                    // Note: If user is already logged in, LoginScreen will see Authenticated state and call onLoginSuccess.
                    
                    NavGraph(navController = navController)
                }
            }
        }
    }
}
