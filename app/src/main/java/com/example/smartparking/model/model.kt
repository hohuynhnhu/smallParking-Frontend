//// model.kt
//package com.example.smartparking.model
//
//import com.google.gson.annotations.SerializedName
//
//// ===== Request Models =====
//data class RegisterRequest(
//    val email: String,
//    @SerializedName("full_name")
//    val fullName: String,
//    val cccd: String,
//    @SerializedName("license_plate")
//    val licensePlate: String,
//    val password: String,
//    @SerializedName("confirm_password")
//    val confirmPassword: String
//)
//
//data class LoginRequest(
//    val email: String,
//    val password: String
//)
//
//// ===== Response Models =====
//data class AuthResponse(
//    val success: Boolean,
//    val message: String,
//    val data: UserData?
//)
//
//data class UserData(
//    @SerializedName("user_id")
//    val userId: String,
//    val email: String,
//    @SerializedName("full_name")
//    val fullName: String,
//    val cccd: String,
//    @SerializedName("license_plate")
//    val licensePlate: String
//)
//
//// ===== User List Response =====
//data class UsersResponse(
//    val success: Boolean,
//    val count: Int,
//    val data: List<UserData>
//)
// model.kt
package com.example.smartparking.model

import com.google.gson.annotations.SerializedName
data class UpdateRoleRequest(
    val role: String  // "user" hoặc "admin"
)
// ===== Request Models =====
data class RegisterRequest(
    val email: String,
    @SerializedName("full_name")
    val fullName: String,
    val cccd: String,
    @SerializedName("license_plate")
    val licensePlate: String,
    val password: String,
    @SerializedName("confirm_password")
    val confirmPassword: String
) {
    // Constructor helper để debug
    init {
        require(email.isNotBlank()) { "Email không được trống" }
        require(fullName.isNotBlank()) { "Full name không được trống" }
        require(cccd.isNotBlank()) { "CCCD không được trống" }
        require(licensePlate.isNotBlank()) { "License plate không được trống" }
        require(password.isNotBlank()) { "Password không được trống" }
        require(confirmPassword.isNotBlank()) { "Confirm password không được trống" }
    }
}

data class LoginRequest(
    val email: String,
    val password: String
)
object UserRole {
    const val USER = "user"
    const val GUARD = "guard"
}
// ===== Response Models =====
data class AuthResponse(
    val success: Boolean,
    val message: String,
    val data: UserData?
)

data class UserData(
    @SerializedName("user_id")
    val userId: String,
    val email: String,
    @SerializedName("full_name")
    val fullName: String,
    val cccd: String,
    @SerializedName("license_plate")
    val licensePlate: String,
    val role: String = "user"  // Thêm role, mặc định "user"
) {
    // Helper functions
    fun isGuard(): Boolean = role == "guard"
    fun isUser(): Boolean = role == "user"
}

// ===== User List Response =====
data class UsersResponse(
    val success: Boolean,
    val count: Int,
    val data: List<UserData>
)