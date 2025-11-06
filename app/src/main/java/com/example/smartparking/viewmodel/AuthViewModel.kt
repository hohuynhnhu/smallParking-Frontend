package com.example.smartparking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartparking.model.*
import com.example.smartparking.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repo = AuthRepository()

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val res = repo.login(email, password)
                if (res.success) _uiState.value = AuthUiState.Success(res.data)
                else _uiState.value = AuthUiState.Error(res.message)
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: "Lỗi không xác định")
            }
        }
    }

    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val res = repo.register(request)
                if (res.success) _uiState.value = AuthUiState.Success(res.data)
                else _uiState.value = AuthUiState.Error(res.message)
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: "Lỗi không xác định")
            }
        }
    }

    fun resetState() { _uiState.value = AuthUiState.Idle }
}

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val user: User?) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}
