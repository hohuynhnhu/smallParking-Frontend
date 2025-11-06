package com.example.smartparking.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.smartparking.viewmodel.AuthUiState
import com.example.smartparking.viewmodel.AuthViewModel

@Composable
fun LoginScreen(navController: NavController, vm: AuthViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val state by vm.uiState.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Đăng nhập", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mật khẩu") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(16.dp))

        Button(onClick = { vm.login(email, password) }, modifier = Modifier.fillMaxWidth()) {
            Text("Đăng nhập")
        }

        Spacer(Modifier.height(8.dp))
        TextButton(onClick = { navController.navigate("register") }) {
            Text("Chưa có tài khoản? Đăng ký")
        }

        when (state) {
            is AuthUiState.Loading -> CircularProgressIndicator()
            is AuthUiState.Error -> Text((state as AuthUiState.Error).message, color = MaterialTheme.colorScheme.error)
            is AuthUiState.Success -> {
                Text("Đăng nhập thành công!")
                // TODO: navController.navigate("dashboard")
            }
            else -> {}
        }
    }
}
