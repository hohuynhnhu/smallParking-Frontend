package com.example.smartparking.repository

import com.example.smartparking.api.AuthService
import com.example.smartparking.model.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthRepository {

    private val api = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8000") // địa chỉ FastAPI khi chạy local
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AuthService::class.java)

    suspend fun login(email: String, password: String): ApiResponse<User> {
        return api.login(LoginRequest(email, password))
    }

    suspend fun register(request: RegisterRequest): ApiResponse<User> {
        return api.register(request)
    }
}
