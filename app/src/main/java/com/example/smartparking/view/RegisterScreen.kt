package com.example.smartparking.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.smartparking.model.RegisterRequest
import com.example.smartparking.viewmodel.AuthUiState
import com.example.smartparking.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, vm: AuthViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var cccd by remember { mutableStateOf("") }
    var licensePlate by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    val state by vm.uiState.collectAsState()


    LaunchedEffect(state) {
        if (state is AuthUiState.Success) {
            navController.navigate("login") {
                popUpTo("register") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(32.dp))

        // 🔹 Tiêu đề hiển thị ở trên cùng
        Text(
            text = "ĐĂNG KÝ",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF485DD9),
            textDecoration = TextDecoration.Underline
        )

        // 🔹 Thêm khoảng cách để tách ra khỏi Box
        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 Form nằm trong Box màu xanh đậm
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE8F0FE), shape = RoundedCornerShape(24.dp))
                .padding(horizontal = 24.dp, vertical = 28.dp),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CustomOutlinedTextField_(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    placeholder = "Email của bạn"
                )

                Spacer(Modifier.height(10.dp))
                CustomOutlinedTextField_(
                    value = name,
                    onValueChange = { name = it },
                    label = "Họ Tên",
                    placeholder = "Họ Tên của bạn"
                )

                Spacer(Modifier.height(10.dp))
                CustomOutlinedTextField_(
                    value = cccd,
                    onValueChange = { cccd = it },
                    label = "CCCD",
                    placeholder = "CCCD của bạn",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(Modifier.height(10.dp))
                CustomOutlinedTextField_(
                    value = licensePlate,
                    onValueChange = { licensePlate = it },
                    label = "Biển số xe",
                    placeholder = "Biển số xe của bạn"
                )

                Spacer(Modifier.height(10.dp))
                CustomOutlinedTextField_(
                    value = password,
                    onValueChange = { password = it },
                    label = "Mật khẩu",
                    isPassword = true,
                    showPassword = showPassword,
                    onTogglePassword = { showPassword = !showPassword }
                )

                Spacer(Modifier.height(10.dp))
                CustomOutlinedTextField_(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Nhập lại mật khẩu",
                    isPassword = true,
                    showPassword = showConfirmPassword,
                    onTogglePassword = { showConfirmPassword = !showConfirmPassword }
                )

                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = {
                        vm.register(
                            RegisterRequest(
                                email = email,
                                fullName = name,  // Đổi từ name -> fullName
                                cccd = cccd,
                                licensePlate = licensePlate,
                                password = password,
                                confirmPassword = confirmPassword  // Thêm field này
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(
                        "ĐĂNG KÝ",
                        color = Color(0xFF4D5EAE),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,

                    )
                }

                Spacer(Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Đã có tài khoản? ", color = Color.Black)
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text(
                            "Đăng nhập ngay",
                            color = Color.Black,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))
//                when (state) {
//                    is AuthUiState.Loading -> CircularProgressIndicator(color = Color.White)
//                    is AuthUiState.Error -> Text(
//                        (state as AuthUiState.Error).message,
//                        color = MaterialTheme.colorScheme.error
//                    )
//                    is AuthUiState.Success -> {
//                        Text("Đăng ký thành công!", color = Color.White)
//                        navController.navigate("login") {
//                            popUpTo("login") { inclusive = true }
//                        }
//                    }
//                    is AuthUiState.Success -> Text("Đăng ký thành công!", color = Color.Green)
//
//                    else -> {}
//                }
                when (state) {
                    is AuthUiState.Loading -> CircularProgressIndicator(color = Color.White)
                    is AuthUiState.Error -> Text(
                        (state as AuthUiState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                    is AuthUiState.Success -> Text("Đăng ký thành công!", color = Color.Green)
                    else -> {}
                }

            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlinedTextField_(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isPassword: Boolean = false,
    showPassword: Boolean = false,
    onTogglePassword: (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = keyboardOptions,
        singleLine = true,
        visualTransformation = if (isPassword && !showPassword)
            PasswordVisualTransformation()
        else
            VisualTransformation.None,
        trailingIcon = if (isPassword) {
            {
                val icon =
                    if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility
                IconButton(onClick = { onTogglePassword?.invoke() }) {
                    Icon(icon, contentDescription = null, tint = Color.Black)
                }
            }
        } else null,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            cursorColor = Color.Black,
            focusedLabelColor = Color.Black,
            unfocusedLabelColor = Color.Black,
            focusedPlaceholderColor = Color.Black,
            unfocusedPlaceholderColor = Color.Black
        )
    )
}
