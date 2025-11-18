
package com.example.smartparking.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartparking.model.*
import com.example.smartparking.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository(application.applicationContext)

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    val userData: StateFlow<UserData?> = repository.userData
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    fun register(request: RegisterRequest) {
        // Validate input trước khi gửi
        if (!validateRegisterInput(request)) {
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            Log.d("AuthViewModel", "Bắt đầu đăng ký với email: ${request.email}")

            val result = repository.register(request)

            result.onSuccess { response ->
                Log.d("AuthViewModel", "Đăng ký thành công: ${response.message}")
                _uiState.value = AuthUiState.Success(response.data!!, response.message)
            }.onFailure { error ->
                Log.e("AuthViewModel", "Đăng ký thất bại: ${error.message}")
                _uiState.value = AuthUiState.Error(error.message ?: "Đăng ký thất bại")
            }
        }
    }

    /**
     * Đăng nhập
     */
    fun login(email: String, password: String) {
        // Validate input
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = AuthUiState.Error("Email và mật khẩu không được để trống")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            Log.d("AuthViewModel", "Bắt đầu đăng nhập với email: $email")

            val request = LoginRequest(email, password)
            val result = repository.login(request)

            result.onSuccess { response ->
                Log.d("AuthViewModel", "Đăng nhập thành công: ${response.message}")
                _uiState.value = AuthUiState.Success(response.data!!, response.message)
            }.onFailure { error ->
                Log.e("AuthViewModel", "Đăng nhập thất bại: ${error.message}")
                _uiState.value = AuthUiState.Error(error.message ?: "Đăng nhập thất bại")
            }
        }
    }

    /**
     * Validate dữ liệu đăng ký
     */
    private fun validateRegisterInput(request: RegisterRequest): Boolean {
        when {
            request.email.isBlank() -> {
                _uiState.value = AuthUiState.Error("Email không được để trống")
                return false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(request.email).matches() -> {
                _uiState.value = AuthUiState.Error("Email không đúng định dạng")
                return false
            }
            request.fullName.isBlank() -> {
                _uiState.value = AuthUiState.Error("Họ tên không được để trống")
                return false
            }
            request.fullName.length < 2 -> {
                _uiState.value = AuthUiState.Error("Họ tên phải có ít nhất 2 ký tự")
                return false
            }
            request.cccd.isBlank() -> {
                _uiState.value = AuthUiState.Error("CCCD không được để trống")
                return false
            }
            request.cccd.length != 12 -> {
                _uiState.value = AuthUiState.Error("CCCD phải có đúng 12 số")
                return false
            }
            !request.cccd.all { it.isDigit() } -> {
                _uiState.value = AuthUiState.Error("CCCD chỉ được chứa số")
                return false
            }
            request.licensePlate.isBlank() -> {
                _uiState.value = AuthUiState.Error("Biển số xe không được để trống")
                return false
            }
            request.password.isBlank() -> {
                _uiState.value = AuthUiState.Error("Mật khẩu không được để trống")
                return false
            }
            request.password.length < 6 -> {
                _uiState.value = AuthUiState.Error("Mật khẩu phải có ít nhất 6 ký tự")
                return false
            }
            !request.password.any { it.isLetter() } -> {
                _uiState.value = AuthUiState.Error("Mật khẩu phải có ít nhất 1 chữ cái")
                return false
            }
            !request.password.any { it.isDigit() } -> {
                _uiState.value = AuthUiState.Error("Mật khẩu phải có ít nhất 1 số")
                return false
            }
            request.password != request.confirmPassword -> {
                _uiState.value = AuthUiState.Error("Mật khẩu xác nhận không khớp")
                return false
            }
        }
        return true
    }

    /**
     * Reset state về Idle
     */
    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }

    /**
     * Đăng xuất
     */
    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _uiState.value = AuthUiState.Idle
        }
    }

    // 🔥 Helper functions để dễ sử dụng

    /**
     * Lấy tên đầy đủ của user
     */
    fun getUserFullName(): String {
        return userData.value?.fullName ?: "Guest"
    }

    /**
     * Lấy tên gọi (tên cuối)
     */
    fun getUserFirstName(): String {
        return userData.value?.fullName?.split(" ")?.lastOrNull() ?: "Guest"
    }

    /**
     * Lấy email của user
     */
    fun getUserEmail(): String {
        return userData.value?.email ?: ""
    }

    /**
     * Lấy biển số xe
     */
    fun getLicensePlate(): String {
        return userData.value?.licensePlate ?: "Chưa có"
    }

    /**
     * Lấy role của user
     */
    fun getUserRole(): String {
        return userData.value?.role ?: "user"
    }

    /**
     * Kiểm tra user có phải admin không
     */
    fun isAdmin(): Boolean {
        return userData.value?.role == "admin"
    }

    /**
     * Kiểm tra user đã đăng nhập chưa
     */
    fun isLoggedIn(): Boolean {
        return userData.value != null
    }
}

/**
 * UI State cho Authentication
 */
sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val userData: UserData, val message: String) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}