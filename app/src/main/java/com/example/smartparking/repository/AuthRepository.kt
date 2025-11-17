////// repository/AuthRepository.kt
////package com.example.smartparking.repository
////
////import android.content.Context
////import android.util.Log
////import androidx.datastore.preferences.core.edit
////import androidx.datastore.preferences.core.stringPreferencesKey
////import androidx.datastore.preferences.preferencesDataStore
////import com.example.smartparking.api.AuthService
////import com.example.smartparking.model.*
////import kotlinx.coroutines.flow.Flow
////import kotlinx.coroutines.flow.map
////
////private val Context.dataStore by preferencesDataStore(name = "auth_prefs")
////
////class AuthRepository(private val context: Context) {
////
////    private val api = AuthService.instance
////
////    private val USER_ID_KEY = stringPreferencesKey("user_id")
////    private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
////    private val USER_NAME_KEY = stringPreferencesKey("user_name")
////    private val USER_CCCD_KEY = stringPreferencesKey("user_cccd")
////    private val USER_LICENSE_PLATE_KEY = stringPreferencesKey("user_license_plate")
////
////    /**
////     * Đăng ký user mới
////     */
////    suspend fun register(request: RegisterRequest): Result<AuthResponse> {
////        return try {
////            Log.d("AuthRepository", "Đang gửi request đăng ký: $request")
////
////            val response = api.register(request)
////
////            Log.d("AuthRepository", "=== REGISTER REQUEST ===")
////            Log.d("AuthRepository", "Email: ${request.email}")
////            Log.d("AuthRepository", "Full Name: ${request.fullName}")
////            Log.d("AuthRepository", "CCCD: ${request.cccd}")
////            Log.d("AuthRepository", "License Plate: ${request.licensePlate}")
////            Log.d("AuthRepository", "Password length: ${request.password.length}")
////            Log.d("AuthRepository", "Confirm Password length: ${request.confirmPassword.length}")
////
////
////            if (response.isSuccessful && response.body() != null) {
////                val authResponse = response.body()!!
////
////                if (authResponse.success && authResponse.data != null) {
////                    // Lưu thông tin user
////                    saveUserData(authResponse.data)
////                    Log.d("AuthRepository", "Đăng ký thành công!")
////                    Result.success(authResponse)
////                } else {
////                    Log.e("AuthRepository", "Đăng ký thất bại: ${authResponse.message}")
////                    Result.failure(Exception(authResponse.message))
////                }
////            } else {
////                val errorMsg = when (response.code()) {
////                    400 -> "Dữ liệu không hợp lệ"
////                    409 -> "Email, CCCD hoặc biển số xe đã tồn tại"
////                    500 -> "Lỗi server"
////                    else -> "Lỗi không xác định (${response.code()})"
////                }
////                Log.e("AuthRepository", "HTTP Error: $errorMsg")
////                Result.failure(Exception(errorMsg))
////            }
////        } catch (e: Exception) {
////            Log.e("AuthRepository", "Exception: ${e.message}", e)
////            Result.failure(Exception("Lỗi kết nối: ${e.message}"))
////        }
////    }
////
////    /**
////     * Đăng nhập
////     */
////    suspend fun login(request: LoginRequest): Result<AuthResponse> {
////        return try {
////            Log.d("AuthRepository", "Đang gửi request đăng nhập: ${request.email}")
////
////            val response = api.login(request)
////
////            Log.d("AuthRepository", "Response code: ${response.code()}")
////
////            if (response.isSuccessful && response.body() != null) {
////                val authResponse = response.body()!!
////
////                if (authResponse.success && authResponse.data != null) {
////                    // Lưu thông tin user
////                    saveUserData(authResponse.data)
////                    Log.d("AuthRepository", "Đăng nhập thành công!")
////                    Result.success(authResponse)
////                } else {
////                    Log.e("AuthRepository", "Đăng nhập thất bại: ${authResponse.message}")
////                    Result.failure(Exception(authResponse.message))
////                }
////            } else {
////                val errorMsg = when (response.code()) {
////                    400 -> "Email hoặc mật khẩu không được để trống"
////                    401 -> "Email hoặc mật khẩu không đúng"
////                    403 -> "Tài khoản đã bị vô hiệu hóa"
////                    500 -> "Lỗi server"
////                    else -> "Lỗi không xác định (${response.code()})"
////                }
////                Log.e("AuthRepository", "HTTP Error: $errorMsg")
////                Result.failure(Exception(errorMsg))
////            }
////        } catch (e: Exception) {
////            Log.e("AuthRepository", "Exception: ${e.message}", e)
////            Result.failure(Exception("Lỗi kết nối: ${e.message}"))
////        }
////    }
////
////    /**
////     * Lấy danh sách tất cả users
////     */
////    suspend fun getAllUsers(): Result<UsersResponse> {
////        return try {
////            val response = api.getAllUsers()
////
////            if (response.isSuccessful && response.body() != null) {
////                Result.success(response.body()!!)
////            } else {
////                Result.failure(Exception("Không thể lấy danh sách users"))
////            }
////        } catch (e: Exception) {
////            Result.failure(e)
////        }
////    }
////
////    /**
////     * Lưu thông tin user vào DataStore
////     */
////    private suspend fun saveUserData(userData: UserData) {
////        context.dataStore.edit { prefs ->
////            prefs[USER_ID_KEY] = userData.userId
////            prefs[USER_EMAIL_KEY] = userData.email
////            prefs[USER_NAME_KEY] = userData.fullName
////            prefs[USER_CCCD_KEY] = userData.cccd
////            prefs[USER_LICENSE_PLATE_KEY] = userData.licensePlate
////        }
////        Log.d("AuthRepository", "Đã lưu thông tin user vào DataStore")
////    }
////
////    /**
////     * Lấy thông tin user đã lưu
////     */
////    val userData: Flow<UserData?> = context.dataStore.data.map { prefs ->
////        val userId = prefs[USER_ID_KEY]
////        val email = prefs[USER_EMAIL_KEY]
////        val name = prefs[USER_NAME_KEY]
////        val cccd = prefs[USER_CCCD_KEY]
////        val licensePlate = prefs[USER_LICENSE_PLATE_KEY]
////
////        if (userId != null && email != null && name != null && cccd != null && licensePlate != null) {
////            UserData(userId, email, name, cccd, licensePlate)
////        } else {
////            null
////        }
////    }
////
////    /**
////     * Kiểm tra user đã đăng nhập chưa
////     */
////    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { prefs ->
////        prefs[USER_ID_KEY] != null
////    }
////
////    /**
////     * Đăng xuất
////     */
////    suspend fun logout() {
////        context.dataStore.edit { it.clear() }
////        Log.d("AuthRepository", "Đã đăng xuất")
////    }
////}
//// repository/AuthRepository.kt
//package com.example.smartparking.repository
//
//import android.content.Context
//import android.util.Log
//import androidx.datastore.preferences.core.edit
//import androidx.datastore.preferences.core.stringPreferencesKey
//import androidx.datastore.preferences.preferencesDataStore
//import com.example.smartparking.api.AuthService
//import com.example.smartparking.model.*
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.map
//
//private val Context.dataStore by preferencesDataStore(name = "auth_prefs")
//
//class AuthRepository(private val context: Context) {
//
//    private val api = AuthService.instance
//
//    private val USER_ID_KEY = stringPreferencesKey("user_id")
//    private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
//    private val USER_NAME_KEY = stringPreferencesKey("user_name")
//    private val USER_CCCD_KEY = stringPreferencesKey("user_cccd")
//    private val USER_LICENSE_PLATE_KEY = stringPreferencesKey("user_license_plate")
//    private val USER_ROLE_KEY = stringPreferencesKey("user_role")  // Thêm role
//
//    /**
//     * Đăng ký user mới
//     */
//    suspend fun register(request: RegisterRequest): Result<AuthResponse> {
//        return try {
//            Log.d("AuthRepository", "Đang gửi request đăng ký: $request")
//
//            val response = api.register(request)
//
//            Log.d("AuthRepository", "Response code: ${response.code()}")
//            Log.d("AuthRepository", "Response body: ${response.body()}")
//            Log.d("AuthRepository", "Response error: ${response.errorBody()?.string()}")
//
//            if (response.isSuccessful && response.body() != null) {
//                val authResponse = response.body()!!
//
//                if (authResponse.success && authResponse.data != null) {
//                    // Lưu thông tin user
//                    saveUserData(authResponse.data)
//                    Log.d("AuthRepository", "Đăng ký thành công!")
//                    Result.success(authResponse)
//                } else {
//                    Log.e("AuthRepository", "Đăng ký thất bại: ${authResponse.message}")
//                    Result.failure(Exception(authResponse.message))
//                }
//            } else {
//                val errorMsg = when (response.code()) {
//                    400 -> "Dữ liệu không hợp lệ"
//                    409 -> "Email, CCCD hoặc biển số xe đã tồn tại"
//                    500 -> "Lỗi server"
//                    else -> "Lỗi không xác định (${response.code()})"
//                }
//                Log.e("AuthRepository", "HTTP Error: $errorMsg")
//                Result.failure(Exception(errorMsg))
//            }
//        } catch (e: Exception) {
//            Log.e("AuthRepository", "Exception: ${e.message}", e)
//            Result.failure(Exception("Lỗi kết nối: ${e.message}"))
//        }
//    }
//
//    /**
//     * Đăng nhập
//     */
//    suspend fun login(request: LoginRequest): Result<AuthResponse> {
//        return try {
//            Log.d("AuthRepository", "Đang gửi request đăng nhập: ${request.email}")
//
//            val response = api.login(request)
//
//            Log.d("AuthRepository", "Response code: ${response.code()}")
//
//            if (response.isSuccessful && response.body() != null) {
//                val authResponse = response.body()!!
//
//                if (authResponse.success && authResponse.data != null) {
//                    // Lưu thông tin user
//                    saveUserData(authResponse.data)
//                    Log.d("AuthRepository", "Đăng nhập thành công!")
//                    Result.success(authResponse)
//                } else {
//                    Log.e("AuthRepository", "Đăng nhập thất bại: ${authResponse.message}")
//                    Result.failure(Exception(authResponse.message))
//                }
//            } else {
//                val errorMsg = when (response.code()) {
//                    400 -> "Email hoặc mật khẩu không được để trống"
//                    401 -> "Email hoặc mật khẩu không đúng"
//                    403 -> "Tài khoản đã bị vô hiệu hóa"
//                    500 -> "Lỗi server"
//                    else -> "Lỗi không xác định (${response.code()})"
//                }
//                Log.e("AuthRepository", "HTTP Error: $errorMsg")
//                Result.failure(Exception(errorMsg))
//            }
//        } catch (e: Exception) {
//            Log.e("AuthRepository", "Exception: ${e.message}", e)
//            Result.failure(Exception("Lỗi kết nối: ${e.message}"))
//        }
//    }
//
//    /**
//     * Lấy danh sách tất cả users
//     */
//    suspend fun getAllUsers(): Result<UsersResponse> {
//        return try {
//            val response = api.getAllUsers()
//
//            if (response.isSuccessful && response.body() != null) {
//                Result.success(response.body()!!)
//            } else {
//                Result.failure(Exception("Không thể lấy danh sách users"))
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//    /**
//     * Lưu thông tin user vào DataStore
//     */
//    private suspend fun saveUserData(userData: UserData) {
//        context.dataStore.edit { prefs ->
//            prefs[USER_ID_KEY] = userData.userId
//            prefs[USER_EMAIL_KEY] = userData.email
//            prefs[USER_NAME_KEY] = userData.fullName
//            prefs[USER_CCCD_KEY] = userData.cccd
//            prefs[USER_LICENSE_PLATE_KEY] = userData.licensePlate
//            prefs[USER_ROLE_KEY] = userData.role  // Lưu role
//        }
//        Log.d("AuthRepository", "Đã lưu thông tin user vào DataStore, role: ${userData.role}")
//    }
//
//    /**
//     * Lấy thông tin user đã lưu
//     */
//    val userData: Flow<UserData?> = context.dataStore.data.map { prefs ->
//        val userId = prefs[USER_ID_KEY]
//        val email = prefs[USER_EMAIL_KEY]
//        val name = prefs[USER_NAME_KEY]
//        val cccd = prefs[USER_CCCD_KEY]
//        val licensePlate = prefs[USER_LICENSE_PLATE_KEY]
//        val role = prefs[USER_ROLE_KEY] ?: "user"  // Lấy role, mặc định "user"
//
//        if (userId != null && email != null && name != null && cccd != null && licensePlate != null) {
//            UserData(userId, email, name, cccd, licensePlate, role)
//        } else {
//            null
//        }
//    }
//
//    /**
//     * Lấy role của user hiện tại
//     */
//    val userRole: Flow<String> = context.dataStore.data.map { prefs ->
//        prefs[USER_ROLE_KEY] ?: "user"
//    }
//
//    /**
//     * Kiểm tra user đã đăng nhập chưa
//     */
//    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { prefs ->
//        prefs[USER_ID_KEY] != null
//    }
//
//    /**
//     * Đăng xuất
//     */
//    suspend fun logout() {
//        context.dataStore.edit { it.clear() }
//        Log.d("AuthRepository", "Đã đăng xuất")
//    }
//}
// repository/AuthRepository.kt
package com.example.smartparking.repository

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.smartparking.api.AuthService
import com.example.smartparking.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

class AuthRepository(private val context: Context) {

    private val api = AuthService.instance

    private val USER_ID_KEY = stringPreferencesKey("user_id")
    private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
    private val USER_NAME_KEY = stringPreferencesKey("user_name")
    private val USER_CCCD_KEY = stringPreferencesKey("user_cccd")
    private val USER_LICENSE_PLATE_KEY = stringPreferencesKey("user_license_plate")
    private val USER_ROLE_KEY = stringPreferencesKey("user_role")  // Thêm role

    /**
     * Đăng ký user mới
     */
    suspend fun register(request: RegisterRequest): Result<AuthResponse> {
        return try {
            Log.d("AuthRepository", "Đang gửi request đăng ký: $request")

            val response = api.register(request)

            Log.d("AuthRepository", "Response code: ${response.code()}")
            Log.d("AuthRepository", "Response body: ${response.body()}")
            Log.d("AuthRepository", "Response error: ${response.errorBody()?.string()}")

            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!

                if (authResponse.success && authResponse.data != null) {
                    // Lưu thông tin user
                    saveUserData(authResponse.data)
                    Log.d("AuthRepository", "Đăng ký thành công!")
                    Result.success(authResponse)
                } else {
                    Log.e("AuthRepository", "Đăng ký thất bại: ${authResponse.message}")
                    Result.failure(Exception(authResponse.message))
                }
            } else {
                val errorMsg = when (response.code()) {
                    400 -> "Dữ liệu không hợp lệ"
                    409 -> "Email, CCCD hoặc biển số xe đã tồn tại"
                    500 -> "Lỗi server"
                    else -> "Lỗi không xác định (${response.code()})"
                }
                Log.e("AuthRepository", "HTTP Error: $errorMsg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Exception: ${e.message}", e)
            Result.failure(Exception("Lỗi kết nối: ${e.message}"))
        }
    }

    /**
     * Đăng nhập
     */
    suspend fun login(request: LoginRequest): Result<AuthResponse> {
        return try {
            Log.d("AuthRepository", "Đang gửi request đăng nhập: ${request.email}")

            val response = api.login(request)

            Log.d("AuthRepository", "Response code: ${response.code()}")

            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!

                if (authResponse.success && authResponse.data != null) {
                    // Lưu thông tin user
                    saveUserData(authResponse.data)
                    Log.d("AuthRepository", "Đăng nhập thành công!")
                    Result.success(authResponse)
                } else {
                    Log.e("AuthRepository", "Đăng nhập thất bại: ${authResponse.message}")
                    Result.failure(Exception(authResponse.message))
                }
            } else {
                val errorMsg = when (response.code()) {
                    400 -> "Email hoặc mật khẩu không được để trống"
                    401 -> "Email hoặc mật khẩu không đúng"
                    403 -> "Tài khoản đã bị vô hiệu hóa"
                    500 -> "Lỗi server"
                    else -> "Lỗi không xác định (${response.code()})"
                }
                Log.e("AuthRepository", "HTTP Error: $errorMsg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Exception: ${e.message}", e)
            Result.failure(Exception("Lỗi kết nối: ${e.message}"))
        }
    }

    /**
     * Lấy danh sách tất cả users
     */
    suspend fun getAllUsers(): Result<UsersResponse> {
        return try {
            val response = api.getAllUsers()

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Không thể lấy danh sách users"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Lưu thông tin user vào DataStore
     */
    private suspend fun saveUserData(userData: UserData) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID_KEY] = userData.userId
            prefs[USER_EMAIL_KEY] = userData.email
            prefs[USER_NAME_KEY] = userData.fullName
            prefs[USER_CCCD_KEY] = userData.cccd
            prefs[USER_LICENSE_PLATE_KEY] = userData.licensePlate
        }
        Log.d("AuthRepository", "Đã lưu thông tin user vào DataStore")
    }

    /**
     * Lấy thông tin user đã lưu
     */
    val userData: Flow<UserData?> = context.dataStore.data.map { prefs ->
        val userId = prefs[USER_ID_KEY]
        val email = prefs[USER_EMAIL_KEY]
        val name = prefs[USER_NAME_KEY]
        val cccd = prefs[USER_CCCD_KEY]
        val licensePlate = prefs[USER_LICENSE_PLATE_KEY]

        if (userId != null && email != null && name != null && cccd != null && licensePlate != null) {
            UserData(userId, email, name, cccd, licensePlate)
        } else {
            null
        }
    }
    /**
     * Cập nhật role của user (chỉ dành cho admin hoặc dev)
     */
    suspend fun updateUserRole(userId: String, newRole: String): Result<AuthResponse> {
        return try {
            val request = UpdateRoleRequest(newRole)
            val response = api.updateRole(userId, request)

            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!

                // Nếu đang update role của chính mình, cập nhật DataStore
                val currentUserId = context.dataStore.data.map { it[USER_ID_KEY] }.first()
                if (currentUserId == userId && authResponse.data != null) {
                    saveUserData(authResponse.data)
                }

                Result.success(authResponse)
            } else {
                Result.failure(Exception("Cập nhật role thất bại"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    /**
     * Kiểm tra user đã đăng nhập chưa
     */
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[USER_ID_KEY] != null
    }

    /**
     * Đăng xuất
     */

    suspend fun logout() {
        context.dataStore.edit { it.clear() }
        Log.d("AuthRepository", "Đã đăng xuất")
    }
}