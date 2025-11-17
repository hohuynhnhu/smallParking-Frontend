//package com.example.smartparking.view
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Visibility
//import androidx.compose.material.icons.filled.VisibilityOff
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.text.input.VisualTransformation
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextDecoration
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import com.example.smartparking.viewmodel.AuthUiState
//import com.example.smartparking.viewmodel.AuthViewModel
//
//@Composable
//fun LoginScreen(navController: NavController, vm: AuthViewModel = viewModel()) {
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var showPassword by remember { mutableStateOf(false) }
//
//    val state by vm.uiState.collectAsState()
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center,
//            modifier = Modifier.padding(horizontal = 24.dp)
//        ) {
//            Text(
//                text = "ĐĂNG NHẬP",
//                fontSize = 26.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color(0xFF3F51B5),
//                textAlign = TextAlign.Center,
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//            Divider(
//                color = Color(0xFF3F51B5),
//                thickness = 2.dp,
//                modifier = Modifier
//                    .width(140.dp)
//                    .padding(bottom = 32.dp)
//            )
//
//            Card(
//                shape = RoundedCornerShape(24.dp),
//                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F0FE)),
//                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Column(
//                    modifier = Modifier.padding(24.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
////                    Text("Email", modifier = Modifier.align(Alignment.Start))
//                    CustomOutlinedTextField(
//                        value = email,
//                        onValueChange = { email = it },
//                        label = "Email"
//                    )
//
//                    Spacer(Modifier.height(8.dp))
////                    Text("Mật khẩu", modifier = Modifier.align(Alignment.Start))
//                    CustomOutlinedTextField(
//                        value = password,
//                        onValueChange = { password = it },
//                        label = "Mật khẩu",
//                        isPassword = true,
//                        showPassword = showPassword,
//                        onTogglePassword = { showPassword = !showPassword }
//                    )
//
//                    Spacer(Modifier.height(20.dp))
//
//                    Button(
//                        onClick = { vm.login(email, password) },
//                        shape = RoundedCornerShape(50),
//                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5)),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(48.dp),
//                        elevation = ButtonDefaults.elevatedButtonElevation(6.dp)
//                    ) {
//                        Text("ĐĂNG KÝ", color = Color.White)
//                    }
//                }
//            }
//
//            Spacer(Modifier.height(16.dp))
//
//            TextButton(onClick = { navController.navigate("register") }) {
//                Text("Chưa đăng ký? ", color = Color.Black)
//                Text(
//                    "Đăng ký ngay",
//                    color = Color.Black,
//                    textDecoration = TextDecoration.Underline,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun CustomOutlinedTextField(
//    value: String,
//    onValueChange: (String) -> Unit,
//    label: String,
//    isPassword: Boolean = false,
//    showPassword: Boolean = false,
//    onTogglePassword: (() -> Unit)? = null,
//    modifier: Modifier = Modifier
//) {
//    OutlinedTextField(
//        value = value,
//        onValueChange = onValueChange,
//        label = { Text(label) },
//        placeholder = { if (isPassword) Text("") else Text("Nhập $label") },
//        visualTransformation = if (isPassword && !showPassword) PasswordVisualTransformation() else VisualTransformation.None,
//        trailingIcon = {
//            if (isPassword) {
//                val icon = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff
//                IconButton(onClick = { onTogglePassword?.invoke() }) {
//                    Icon(icon, contentDescription = if (showPassword) "Ẩn mật khẩu" else "Hiện mật khẩu")
//                }
//            }
//        },
//        singleLine = true,
//        modifier = modifier.fillMaxWidth()
//    )
//}
package com.example.smartparking.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.smartparking.viewmodel.AuthUiState
import com.example.smartparking.viewmodel.AuthViewModel

@Composable
fun LoginScreen(navController: NavController, vm: AuthViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    val state by vm.uiState.collectAsState()

    // Hiển thị thông báo khi đăng nhập thành công
    LaunchedEffect(state) {
        if (state is AuthUiState.Success) {
            // Navigation được xử lý trong MainActivity
            // Không cần navigate ở đây nữa
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            // Logo hoặc Icon (optional)
            Text(
                text = "🚗",
                fontSize = 64.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "ĐĂNG NHẬP",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3F51B5),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Divider(
                color = Color(0xFF3F51B5),
                thickness = 2.dp,
                modifier = Modifier
                    .width(140.dp)
                    .padding(bottom = 32.dp)
            )

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F0FE)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Email Field
                    CustomOutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            // Reset error khi user bắt đầu nhập
                            if (state is AuthUiState.Error) {
                                vm.resetState()
                            }
                        },
                        label = "Email",
                        placeholder = "example@gmail.com",
                        enabled = state !is AuthUiState.Loading
                    )

                    Spacer(Modifier.height(16.dp))

                    // Password Field
                    CustomOutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            if (state is AuthUiState.Error) {
                                vm.resetState()
                            }
                        },
                        label = "Mật khẩu",
                        placeholder = "Nhập mật khẩu",
                        isPassword = true,
                        showPassword = showPassword,
                        onTogglePassword = { showPassword = !showPassword },
                        enabled = state !is AuthUiState.Loading
                    )

                    Spacer(Modifier.height(24.dp))

                    // Login Button
                    Button(
                        onClick = {
                            // Validate trước khi gửi
                            if (email.isBlank()) {
                                // Có thể show snackbar hoặc error message
                                return@Button
                            }
                            if (password.isBlank()) {
                                return@Button
                            }

                            // Format email (lowercase, trim)
                            val formattedEmail = email.trim().lowercase()
                            vm.login(formattedEmail, password)
                        },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3F51B5)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        elevation = ButtonDefaults.elevatedButtonElevation(6.dp),
                        enabled = state !is AuthUiState.Loading &&
                                email.isNotBlank() &&
                                password.isNotBlank()
                    ) {
                        if (state is AuthUiState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                "ĐĂNG NHẬP",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Hiển thị lỗi nếu có
                    if (state is AuthUiState.Error) {
                        Spacer(Modifier.height(16.dp))

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFFEBEE)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = (state as AuthUiState.Error).message,
                                color = Color(0xFFD32F2F),
                                fontSize = 14.sp,
                                modifier = Modifier.padding(12.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Hiển thị thông báo thành công
                    if (state is AuthUiState.Success) {
                        Spacer(Modifier.height(16.dp))

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFE8F5E9)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text("✓", fontSize = 18.sp, color = Color(0xFF388E3C))
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = (state as AuthUiState.Success).message,
                                    color = Color(0xFF388E3C),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Register Link
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Chưa có tài khoản? ",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                TextButton(
                    onClick = {
                        navController.navigate("register")
                        vm.resetState() // Reset state khi chuyển màn
                    },
                    enabled = state !is AuthUiState.Loading
                ) {
                    Text(
                        "Đăng ký ngay",
                        color = Color(0xFF3F51B5),
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    isPassword: Boolean = false,
    showPassword: Boolean = false,
    onTogglePassword: (() -> Unit)? = null,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        visualTransformation = if (isPassword && !showPassword)
            PasswordVisualTransformation()
        else
            VisualTransformation.None,
        trailingIcon = {
            if (isPassword) {
                val icon = if (showPassword)
                    Icons.Default.Visibility
                else
                    Icons.Default.VisibilityOff

                IconButton(onClick = { onTogglePassword?.invoke() }) {
                    Icon(
                        icon,
                        contentDescription = if (showPassword) "Ẩn mật khẩu" else "Hiện mật khẩu",
                        tint = Color(0xFF3F51B5)
                    )
                }
            }
        },
        singleLine = true,
        enabled = enabled,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF3F51B5),
            focusedLabelColor = Color(0xFF3F51B5),
            cursorColor = Color(0xFF3F51B5),
            disabledBorderColor = Color.LightGray,
            disabledTextColor = Color.Gray
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth()
    )
}