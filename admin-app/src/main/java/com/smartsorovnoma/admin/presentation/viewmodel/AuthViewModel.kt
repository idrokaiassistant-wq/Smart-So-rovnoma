package com.smartsorovnoma.admin.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartsorovnoma.admin.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import com.smartsorovnoma.admin.di.RepositoryProvider

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    object Authenticated : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkCurrentAuth()
    }

    private fun checkCurrentAuth() {
        if (repository.currentUser != null) {
            checkAdminStatus()
        }
    }

    fun enableDemoMode() {
        RepositoryProvider.enableDemoMode(true)
        _authState.value = AuthState.Authenticated
    }

    fun checkAdminStatus() {
        RepositoryProvider.enableDemoMode(false) // Reset to normal if real check requested
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.checkAdminStatus()
            if (result.isSuccess) {
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value = AuthState.Error(
                    result.exceptionOrNull()?.message ?: "Noma'lum xatolik"
                )
                repository.signOut() // Sign out if not admin
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.signInWithGoogle(idToken)
            if (result.isSuccess) {
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value = AuthState.Error(
                    result.exceptionOrNull()?.message ?: "Autentifikatsiya muvaffaqiyatsiz tugadi"
                )
                repository.signOut()
            }
        }
    }

    fun signOut() {
        repository.signOut()
        _authState.value = AuthState.Initial
    }
}
