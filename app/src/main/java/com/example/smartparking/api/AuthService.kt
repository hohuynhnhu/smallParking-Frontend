//// api/AuthService.kt
//package com.example.smartparking.api
//
//import com.example.smartparking.model.*
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Response
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.http.*
//import java.util.concurrent.TimeUnit
//
//interface AuthService {
//
//    @POST("api/auth/register")
//    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
//
//    @POST("api/auth/login")
//    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
//
//    @GET("api/auth/users")
//    suspend fun getAllUsers(): Response<UsersResponse>
//
//    companion object {
//        // 🔥 QUAN TRỌNG: Thay đổi BASE_URL theo môi trường của bạn
//
//        // Nếu dùng emulator Android Studio:
//        private const val BASE_URL = "http://10.0.2.2:5000/"
//
//        // Nếu dùng thiết bị thật, thay bằng IP máy tính:
//        // private const val BASE_URL = "http://192.168.1.XXX:5000/"
//        // Xem IP bằng lệnh: ipconfig (Windows) hoặc ifconfig (Mac/Linux)
//
//        private val loggingInterceptor = HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        }
//
//        private val okHttpClient = OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .connectTimeout(30, TimeUnit.SECONDS)
//            .readTimeout(30, TimeUnit.SECONDS)
//            .writeTimeout(30, TimeUnit.SECONDS)
//            .build()
//
//        val instance: AuthService by lazy {
//            Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//                .create(AuthService::class.java)
//        }
//    }
//}
// api/AuthService.kt
package com.example.smartparking.api

import com.example.smartparking.model.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface AuthService {

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @GET("api/auth/users")
    suspend fun getAllUsers(): Response<UsersResponse>

    @PUT("api/auth/users/{userId}/role")
    suspend fun updateRole(
        @Path("userId") userId: String,
        @Body request: UpdateRoleRequest
    ): Response<AuthResponse>

    @GET("api/auth/users/role/{role}")
    suspend fun getUsersByRole(@Path("role") role: String): Response<UsersResponse>

    companion object {
        // 🔥 QUAN TRỌNG: Thay đổi BASE_URL theo môi trường của bạn

        // Nếu dùng emulator Android Studio:
        private const val BASE_URL = "http://10.0.2.2:5000/"

        // Nếu dùng thiết bị thật, thay bằng IP máy tính:
        // private const val BASE_URL = "http://192.168.1.XXX:5000/"
        // Xem IP bằng lệnh: ipconfig (Windows) hoặc ifconfig (Mac/Linux)

        private val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        // Cấu hình Gson để xử lý null values
        private val gson = com.google.gson.GsonBuilder()
            .setLenient()
            .serializeNulls()
            .create()

        val instance: AuthService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(AuthService::class.java)
        }
    }
}