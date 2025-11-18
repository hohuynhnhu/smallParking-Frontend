//
//package com.example.smartparking.view
//
//import androidx.activity.ComponentActivity
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
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.platform.LocalContext
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import com.example.smartparking.viewmodel.AuthViewModel
//import com.example.smartparking.viewmodel.ParkingViewModel
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
//    // Lấy thông tin user
//    val userData by vm.userData.collectAsState()
//    val displayName = userData?.fullName?.split(" ")?.lastOrNull() ?: "Guest"
//    val licensePlate = userData?.licensePlate ?: "Chưa có"
//
//    // Parking ViewModel
////    val parkingVM: ParkingViewModel = viewModel(
////        viewModelStoreOwner = LocalContext.current as? ComponentActivity
////    )
//    val parkingVM: ParkingViewModel = viewModel()
//
//
//    val status by parkingVM.parkingStatus.collectAsState()
//
//    // Tải trạng thái realtime khi vào màn
//    LaunchedEffect(licensePlate) {
//        if (licensePlate != "Chưa có") {
//            parkingVM.loadParkingStatus(licensePlate)
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        // HEADER
//        TopAppBar(
//            title = {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text("Small Parking", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
//                    Text("Hi $displayName", color = Color.White, fontSize = 23.sp)
//                }
//            },
//            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1565C0))
//        )
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        // ===========================
//        //        TRẠNG THÁI XE
//        // ===========================
//        InfoCard(
//            title = "Trạng thái xe",
//            content = buildString {
//                append("Biển số: $licensePlate\n")
//
//                if (status != null) {
//                    append("Thời gian vào: ${status?.timestamp ?: "Không có"}\n")
//                    append("Đang đỗ: ${status?.trangthai ?: false}\n")
//                }
//            },
//            buttonText =
//                when {
//                    status?.trangthai == true -> "Rời đi"
//                    status?.trangthai == false -> "Chưa đỗ"
//                    else -> "Chưa có dữ liệu"
//                },
//            onButtonClick = {
//                if (status?.trangthai == true) {
//                    parkingVM.updateTrangThai(licensePlate, false)
//                }
//            }
//        )
//
//        // ===========================
//        //      CẢNH BÁO (nếu có)
//        // ===========================
//        if (status?.canhbao == true) {
//            Text(
//                text = "⚠ Cảnh báo: Xe có dấu hiệu bất thường!",
//                color = Color.Red,
//                fontSize = 18.sp,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.padding(16.dp)
//            )
//        }
//
//        Spacer(modifier = Modifier.weight(1f))
//
//        // LOGOUT ICON
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            horizontalArrangement = Arrangement.End
//        ) {
//            Icon(
//                imageVector = Icons.Default.Logout,
//                contentDescription = null,
//                tint = Color.Black,
//                modifier = Modifier
//                    .size(28.dp)
//                    .clickable { showLogoutDialog = true }
//            )
//        }
//
//        // DIALOG
//        if (showLogoutDialog) {
//            AlertDialog(
//                onDismissRequest = { showLogoutDialog = false },
//                title = { Text("Đăng xuất") },
//                text = { Text("Bạn có chắc chắn muốn đăng xuất?") },
//                confirmButton = {
//                    TextButton(
//                        onClick = {
//                            scope.launch {
//                                vm.logout()
//                                navController.navigate("login") {
//                                    popUpTo(0) { inclusive = true }
//                                }
//                            }
//                        }
//                    ) { Text("Đăng xuất", color = Color.Red) }
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
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
    var showAlertDialog by remember { mutableStateOf(false) }
    var previousAlertState by remember { mutableStateOf<Boolean?>(null) }

    // Lấy thông tin user
    val userData by vm.userData.collectAsState()
    val displayName = userData?.fullName?.split(" ")?.lastOrNull() ?: "Guest"
    val licensePlate = userData?.licensePlate ?: "Chưa có"

    // Parking ViewModel
    val parkingVM: ParkingViewModel = viewModel()
    val status by parkingVM.parkingStatus.collectAsState()

    val buttonText = when {
        status?.trangthai == true -> "Rời đi"
        status?.trangthai == false -> "Chưa đỗ"
        else -> "Đã đỗ"
    }
    // Tải trạng thái realtime khi vào màn
    LaunchedEffect(licensePlate) {
        if (licensePlate != "Chưa có") {
            parkingVM.loadParkingStatus(licensePlate)
        }
    }

    // ===== THEO DÕI CẢNH BÁO REALTIME =====
    LaunchedEffect(status?.canhbao) {
        val currentAlert = status?.canhbao ?: false

        // Chỉ hiển thị cảnh báo khi trạng thái thay đổi từ false -> true
        if (previousAlertState != null && !previousAlertState!! && currentAlert) {
            showAlertDialog = true
        }

        previousAlertState = currentAlert
    }

    // Hiển thị dialog ngay khi có cảnh báo
    if (status?.canhbao == true && !showAlertDialog) {
        showAlertDialog = true
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
                append("Biển số xe ${licensePlate}\n")
                if (status != null) {
                    append("\n${if (status?.trangthai == true) "Đã đỗ" else "Chưa đỗ"}")
                }
            },

                buttonText = when {
                    status?.trangthai == true -> "Rời đi"
                    status?.trangthai == false -> "Chưa đỗ"
                    else -> "Đã đỗ"
                },
                onButtonClick = {
                    if (status?.trangthai == true) {
                        parkingVM.updateTrangThai(licensePlate, false)
                    }
                }
        )

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

        // ===== DIALOG CẢNH BÁO =====
        if (showAlertDialog && status?.canhbao == true) {
            Dialog(
                onDismissRequest = { /* Không cho tắt bằng cách click ngoài */ }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Tiêu đề cảnh báo
                        Text(
                            text = "Cảnh báo",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Nội dung cảnh báo
                        Text(
                            text = "Xe bạn ra cổng mà\nchưa cấp quyền",
                            fontSize = 18.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            lineHeight = 28.sp
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Nút "Vâng, là tôi" - Màu xanh mint
                        Button(
                            onClick = {
                                // Set canhbao = false và trangthai = false
                                parkingVM.clearAlertAndLeave(licensePlate)
                                showAlertDialog = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFB2F5EA) // Màu xanh mint nhạt
                            ),
                            shape = RoundedCornerShape(28.dp)
                        ) {
                            Text(
                                text = "Vâng, là tôi",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Nút "Không phải tôi" - Màu hồng nhạt
                        Button(
                            onClick = {
                                // Chỉ tắt dialog, không làm gì cả
                                showAlertDialog = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFC5C5) // Màu hồng nhạt
                            ),
                            shape = RoundedCornerShape(28.dp)
                        ) {
                            Text(
                                text = "Không phải tôi",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }

        // DIALOG ĐĂNG XUẤT
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
        colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB)),
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