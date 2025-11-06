package com.example.smartparking.api

import com.example.smartparking.model.*
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/auth/register")
    suspend fun register(@Body request: RegisterRequest): ApiResponse<User>

    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<User>
}
