package com.example.smartparking.model


data class User(
    val id: String,
    val email: String,
    val name: String,
    val cccd: String,
    val licensePlate: String
)

data class RegisterRequest(
    val email: String,
    val name: String,
    val cccd: String,
    val licensePlate: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?
)
