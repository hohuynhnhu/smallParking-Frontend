//
//package com.example.smartparking.view
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.material.icons.filled.Logout
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import com.example.smartparking.viewmodel.AuthViewModel
//import kotlinx.coroutines.launch
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ParkingStatusScreen(
//    navController: NavController,
//    vm: AuthViewModel = viewModel()
//) {
//    val scope = rememberCoroutineScope()
//    var showLogoutDialog by remember { mutableStateOf(false) }
//
//    // 🔥 Lấy userData từ ViewModel
//    val userData by vm.userData.collectAsState()
//
//    // Lấy tên để hiển thị (lấy tên cuối cùng)
//    val displayName = userData?.fullName?.split(" ")?.lastOrNull() ?: "Guest"
//
//    // Lấy biển số xe
//    val licensePlate = userData?.licensePlate ?: "Chưa có"
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White),
//        horizontalAlignment = Alignment.CenterHorizontally,
//    ) {
//        // Thanh tiêu đề màu xanh
//        TopAppBar(
//            title = {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = "Small Parking",
//                        color = Color.White,
//                        fontSize = 25.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//
//                    Text(
//                        text = "Hi $displayName",
//                        color = Color.White,
//                        fontSize = 23.sp,
//                        fontWeight = FontWeight.Medium
//                    )
//                }
//            },
//            colors = TopAppBarDefaults.topAppBarColors(
//                containerColor = Color(0xFF1565C0)
//            )
//        )
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        // Card trạng thái xe - 🔥 Hiển thị biển số từ database
//        InfoCard(
//            title = "Trạng thái xe",
//            content = "Biển số xe: $licensePlate",
//            buttonText = "Chưa đỗ"
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Card số lượt vào
//        InfoCard(
//            title = "Số lượt vào của bạn: 1",
//            buttonText = "Thêm lượt"
//        )
//
//        Spacer(modifier = Modifier.weight(1f))
//
//        // Icon góc phải dưới
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            horizontalArrangement = Arrangement.End
//        ) {
//            Icon(
//                imageVector = Icons.Default.Logout,
//                contentDescription = "Logout",
//                tint = Color.Black,
//                modifier = Modifier
//                    .size(28.dp)
//                    .clickable {
//                        // Hiển thị dialog xác nhận
//                        showLogoutDialog = true
//                    }
//            )
//        }
//
//        // Dialog xác nhận đăng xuất
//        if (showLogoutDialog) {
//            AlertDialog(
//                onDismissRequest = { showLogoutDialog = false },
//                title = { Text("Đăng xuất") },
//                text = { Text("Bạn có chắc chắn muốn đăng xuất?") },
//                confirmButton = {
//                    TextButton(
//                        onClick = {
//                            scope.launch {
//                                // Đăng xuất
//                                vm.logout()
//
//                                // Chuyển về màn login
//                                navController.navigate("login") {
//                                    popUpTo(0) { inclusive = true }
//                                }
//                            }
//                            showLogoutDialog = false
//                        }
//                    ) {
//                        Text("Đăng xuất", color = Color.Red)
//                    }
//                },
//                dismissButton = {
//                    TextButton(onClick = { showLogoutDialog = false }) {
//                        Text("Hủy")
//                    }
//                }
//            )
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun InfoCard(
//    title: String,
//    content: String? = null,
//    buttonText: String,
//    onButtonClick: (() -> Unit)? = null
//) {
//    Card(
//        shape = RoundedCornerShape(20.dp),
//        colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB)), // xanh nhạt
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 24.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .padding(16.dp)
//                .fillMaxWidth(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = title,
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier
//                    .align(Alignment.Start)
//                    .padding(bottom = 4.dp)
//            )
//            if (content != null) {
//                Text(
//                    text = content,
//                    fontSize = 15.sp,
//                    modifier = Modifier
//                        .align(Alignment.Start)
//                        .padding(bottom = 8.dp)
//                )
//            }
//            Button(
//                onClick = { onButtonClick?.invoke() },
//                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
//                shape = RoundedCornerShape(50),
//                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp),
//                modifier = Modifier.height(36.dp)
//            ) {
//                Text(
//                    text = buttonText,
//                    color = Color(0xFF1565C0),
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Medium
//                )
//            }
//        }
//    }
//}
package com.example.smartparking.view

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.smartparking.viewmodel.AuthViewModel
import com.example.smartparking.viewmodel.ParkingViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParkingStatusScreen(
    navController: NavController,
    vm: AuthViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Lấy thông tin user
    val userData by vm.userData.collectAsState()
    val displayName = userData?.fullName?.split(" ")?.lastOrNull() ?: "Guest"
    val licensePlate = userData?.licensePlate ?: "Chưa có"

    // Parking ViewModel
//    val parkingVM: ParkingViewModel = viewModel(
//        viewModelStoreOwner = LocalContext.current as? ComponentActivity
//    )
    val parkingVM: ParkingViewModel = viewModel()


    val status by parkingVM.parkingStatus.collectAsState()

    // Tải trạng thái realtime khi vào màn
    LaunchedEffect(licensePlate) {
        if (licensePlate != "Chưa có") {
            parkingVM.loadParkingStatus(licensePlate)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // HEADER
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Small Parking", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    Text("Hi $displayName", color = Color.White, fontSize = 23.sp)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1565C0))
        )

        Spacer(modifier = Modifier.height(32.dp))

        // ===========================
        //        TRẠNG THÁI XE
        // ===========================
        InfoCard(
            title = "Trạng thái xe",
            content = buildString {
                append("Biển số: $licensePlate\n")

                if (status != null) {
                    append("Thời gian vào: ${status?.timestamp ?: "Không có"}\n")
                    append("Đang đỗ: ${status?.trangthai ?: false}\n")
                }
            },
            buttonText =
                when {
                    status?.trangthai == true -> "Rời đi"
                    status?.trangthai == false -> "Chưa đỗ"
                    else -> "Chưa có dữ liệu"
                },
            onButtonClick = {
                if (status?.trangthai == true) {
                    parkingVM.updateTrangThai(licensePlate, false)
                }
            }
        )

        // ===========================
        //      CẢNH BÁO (nếu có)
        // ===========================
        if (status?.canhbao == true) {
            Text(
                text = "⚠ Cảnh báo: Xe có dấu hiệu bất thường!",
                color = Color.Red,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // LOGOUT ICON
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { showLogoutDialog = true }
            )
        }

        // DIALOG
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Đăng xuất") },
                text = { Text("Bạn có chắc chắn muốn đăng xuất?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            scope.launch {
                                vm.logout()
                                navController.navigate("login") {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                    ) { Text("Đăng xuất", color = Color.Red) }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("Hủy")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoCard(
    title: String,
    content: String? = null,
    buttonText: String,
    onButtonClick: (() -> Unit)? = null
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB)), // xanh nhạt
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 4.dp)
            )
            if (content != null) {
                Text(
                    text = content,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 8.dp)
                )
            }
            Button(
                onClick = { onButtonClick?.invoke() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(50),
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text(
                    text = buttonText,
                    color = Color(0xFF1565C0),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}