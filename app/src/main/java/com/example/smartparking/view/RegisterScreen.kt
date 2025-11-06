package com.example.smartparking.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.smartparking.model.RegisterRequest
import com.example.smartparking.viewmodel.AuthUiState
import com.example.smartparking.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(navController: NavController, vm: AuthViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var cccd by remember { mutableStateOf("") }
    var licensePlate by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val state by vm.uiState.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Đăng ký", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Họ và tên") })
        OutlinedTextField(value = cccd, onValueChange = { cccd = it }, label = { Text("CCCD") })
        OutlinedTextField(value = licensePlate, onValueChange = { licensePlate = it }, label = { Text("Biển số xe") })
        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Mật khẩu") }, visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                vm.register(RegisterRequest(email, name, cccd, licensePlate, password))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Đăng ký")
        }

        Spacer(Modifier.height(8.dp))
        TextButton(onClick = { navController.popBackStack() }) { Text("Đã có tài khoản? Đăng nhập") }

        when (state) {
            is AuthUiState.Loading -> CircularProgressIndicator()
            is AuthUiState.Error -> Text((state as AuthUiState.Error).message, color = MaterialTheme.colorScheme.error)
            is AuthUiState.Success -> {
                Text("Đăng ký thành công!")
                navController.navigate("login") { popUpTo("login") { inclusive = true } }
            }
            else -> {}
        }
    }
}
