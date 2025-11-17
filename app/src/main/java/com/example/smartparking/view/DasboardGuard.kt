package com.example.smartparking.view


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.smartparking.repository.AuthRepository
import com.example.smartparking.viewmodel.AuthViewModel
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardGuardScreen(
    navController: NavController,
    vm: AuthViewModel
) {
    val repository = AuthRepository(androidx.compose.ui.platform.LocalContext.current)
    var userData by remember { mutableStateOf<com.example.smartparking.model.UserData?>(null) }

    LaunchedEffect(Unit) {
        userData = repository.userData.first()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard - Admin") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFF6B6B)
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Chào mừng Admin!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF6B6B)
            )

            Spacer(modifier = Modifier.height(16.dp))

            userData?.let { user ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFE0E0)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Email: ${user.email}")
                        Text("Họ tên: ${user.fullName}")
                        Text(
                            "Role: ${user.role}",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF6B6B)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Quyền quản trị viên",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    vm.logout()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                )
            ) {
                Text("Đăng xuất")
            }
        }
    }
}