package com.example.smartparking

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartparking.ui.theme.SmartParkingTheme
import com.example.smartparking.view.*
import com.example.smartparking.viewmodel.AuthUiState
import com.example.smartparking.viewmodel.AuthViewModel
import com.example.smartparking.model.UserRole
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
//        FirebaseApp.initializeApp(this)
        setContent {
            SmartParkingTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.uiState.collectAsState()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                navController = navController,
                vm = authViewModel
            )
        }

        composable("register") {
            RegisterScreen(
                navController = navController,
                vm = authViewModel
            )
        }

        composable("dashboard_user") {
            ParkingStatusScreen(navController = navController,
                vm = authViewModel)
        }

        composable("dashboard_guard") {
            DashboardGuardScreen(
                navController = navController,
                vm = authViewModel
            )
        }

    }

    // Quan sát authState để điều hướng
    LaunchedEffect(authState) {
        Log.d("MainActivity", "Auth State: $authState")

        if (authState is AuthUiState.Success) {
            val userData = (authState as AuthUiState.Success).userData

            Log.d("MainActivity", "Login Success - User: ${userData.email}, Role: ${userData.role}")

            // Điều hướng dựa trên role
            val destination = when (userData.role) {
                UserRole.GUARD -> {
                    Log.d("MainActivity", "Navigating to dashboard_guard")
                    "dashboard_guard"
                }
                UserRole.USER -> {
                    Log.d("MainActivity", "Navigating to dashboard_user")
                    "dashboard_user"
                }
                else -> {
                    Log.d("MainActivity", "Unknown role: ${userData.role}, navigating to dashboard_user")
                    "login"
                }
            }

            navController.navigate(destination) {
                popUpTo("login") { inclusive = true }
            }

            authViewModel.resetState()
        }

    }
}