
package com.example.smartparking.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Garage
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.smartparking.repository.AuthRepository
import com.example.smartparking.viewmodel.AuthViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class DateGroup(
    val date: String,
    val vehicleGroups: List<VehicleGroup>
)

data class VehicleGroup(
    val vehicleType: String,
    val licensePlate: String,
    val timelines: List<TimelineData>
)

data class TimelineData(
    val timelineId: String,
    val fields: Map<String, Any?>
)

data class ParkingStatus(
    val vehicleCount: Int = 0,
    val vehicles: List<String> = emptyList(),
    val gateStatus: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardGuardScreen(
    navController: NavController,
    vm: AuthViewModel
) {
    val repository = AuthRepository(androidx.compose.ui.platform.LocalContext.current)
    var userData by remember { mutableStateOf<com.example.smartparking.model.UserData?>(null) }
    var dateGroups by remember { mutableStateOf<List<DateGroup>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // Realtime Database states
    var parkingStatus by remember { mutableStateOf(ParkingStatus()) }
    var realtimeError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        userData = repository.userData.first()

        // Lắng nghe Realtime Database
        val database = FirebaseDatabase.getInstance()
        val bienSoRef = database.getReference("biensotrongbai")
        val gateRef = database.getReference("trangthaicong")

        // Lắng nghe danh sách biển số xe
        bienSoRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val vehicleList = mutableListOf<String>()
                    for (child in snapshot.children) {
                        child.key?.let { vehicleList.add(it) }
                    }
                    parkingStatus = parkingStatus.copy(
                        vehicleCount = vehicleList.size,
                        vehicles = vehicleList
                    )
                    realtimeError = null
                } catch (e: Exception) {
                    realtimeError = "Lỗi đọc biển số xe: ${e.message}"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                realtimeError = "Lỗi kết nối: ${error.message}"
            }
        })

        // Lắng nghe trạng thái cổng
        gateRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val gateStatus = snapshot.getValue(Boolean::class.java) ?: false
                    parkingStatus = parkingStatus.copy(gateStatus = gateStatus)
                } catch (e: Exception) {
                    realtimeError = "Lỗi đọc trạng thái cổng: ${e.message}"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                realtimeError = "Lỗi kết nối cổng: ${error.message}"
            }
        })
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Dashboard - Admin") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF6699FF)
                    )
                )

                userData?.let { user ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF3F4F6))
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Xin chào, Bảo vệ: ${user.fullName}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.Black
                            )
                            Text(
                                text = user.email,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // === REALTIME PARKING STATUS ===
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF4CAF50)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "TRẠNG THÁI BÃI ĐỖ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            modifier = Modifier.weight(1f).padding(4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${parkingStatus.vehicleCount}",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF6699FF)
                                )
                                Text(
                                    text = "Xe trong bãi",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (parkingStatus.gateStatus) Color(0xFF4CAF50) else Color(0xFFFF5252)
                            ),
                            modifier = Modifier.weight(1f).padding(4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (parkingStatus.gateStatus) "MỞ" else "ĐÓNG",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Trạng thái cổng",
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    if (parkingStatus.vehicles.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = Color.White.copy(alpha = 0.3f))
                        Spacer(modifier = Modifier.height(12.dp))

                        var showAllVehicles by remember { mutableStateOf(false) }
                        val displayLimit = 6
                        val displayVehicles = if (showAllVehicles) {
                            parkingStatus.vehicles
                        } else {
                            parkingStatus.vehicles.take(displayLimit)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Danh sách xe trong bãi:",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            if (parkingStatus.vehicles.size > displayLimit) {
                                TextButton(
                                    onClick = { showAllVehicles = !showAllVehicles },
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text(
                                        text = if (showAllVehicles) "Thu gọn" else "Xem tất cả",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Icon(
                                        imageVector = if (showAllVehicles) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (parkingStatus.vehicles.size > 20 && showAllVehicles) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White.copy(alpha = 0.95f)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                            ) {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    items(displayVehicles.chunked(3)) { row ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            row.forEach { plate ->
                                                Card(
                                                    colors = CardDefaults.cardColors(
                                                        containerColor = Color.White
                                                    ),
                                                    modifier = Modifier.weight(1f),
                                                    elevation = CardDefaults.cardElevation(2.dp)
                                                ) {
                                                    Text(
                                                        text = plate,
                                                        modifier = Modifier
                                                            .padding(8.dp)
                                                            .fillMaxWidth(),
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(0xFF6699FF)
                                                    )
                                                }
                                            }
                                            repeat(3 - row.size) {
                                                Spacer(modifier = Modifier.weight(1f))
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                displayVehicles.chunked(3).forEach { row ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        row.forEach { plate ->
                                            Card(
                                                colors = CardDefaults.cardColors(
                                                    containerColor = Color.White
                                                ),
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .padding(vertical = 3.dp),
                                                elevation = CardDefaults.cardElevation(2.dp)
                                            ) {
                                                Text(
                                                    text = plate,
                                                    modifier = Modifier
                                                        .padding(10.dp)
                                                        .fillMaxWidth(),
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF6699FF)
                                                )
                                            }
                                        }
                                        repeat(3 - row.size) {
                                            Spacer(modifier = Modifier.weight(1f))
                                        }
                                    }
                                }
                            }
                        }

                        if (!showAllVehicles && parkingStatus.vehicles.size > displayLimit) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "+ ${parkingStatus.vehicles.size - displayLimit} xe khác",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.8f),
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        }
                    }

                    realtimeError?.let { error ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = error,
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ NÚT XEM LỊCH SỬ - CODE TRỰC TIẾP TRONG onClick
            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        errorMessage = null
                        android.util.Log.d("DashboardGuard", "🔵 Bắt đầu load...")

                        try {
                            val db = FirebaseFirestore.getInstance()
                            val tempList = mutableListOf<DateGroup>()

                            val datesSnapshot = db.collection("lichsuhoatdong").get().await()
                            android.util.Log.d("DashboardGuard", "Tìm thấy ${datesSnapshot.documents.size} ngày")

                            for (dateDoc in datesSnapshot.documents) {
                                val date = dateDoc.id
                                val vehicleGroups = mutableListOf<VehicleGroup>()

                                // XE MÁY
                                val xeMaySnapshot = db.collection("lichsuhoatdong")
                                    .document(date)
                                    .collection("xemay")
                                    .get().await()

                                for (xeMayDoc in xeMaySnapshot.documents) {
                                    val timelines = mutableListOf<TimelineData>()
                                    val timelineSnapshot = db.collection("lichsuhoatdong")
                                        .document(date)
                                        .collection("xemay")
                                        .document(xeMayDoc.id)
                                        .collection("timeline")
                                        .get().await()

                                    for (timelineDoc in timelineSnapshot.documents) {
                                        timelines.add(
                                            TimelineData(
                                                timelineId = timelineDoc.id,
                                                fields = timelineDoc.data ?: emptyMap()
                                            )
                                        )
                                    }

                                    if (timelines.isNotEmpty()) {
                                        vehicleGroups.add(
                                            VehicleGroup(
                                                vehicleType = "Xe máy",
                                                licensePlate = xeMayDoc.id,
                                                timelines = timelines
                                            )
                                        )
                                    }
                                }

                                // XE Ô TÔ
                                val xeOtoSnapshot = db.collection("lichsuhoatdong")
                                    .document(date)
                                    .collection("xeoto")
                                    .get().await()

                                for (xeOtoDoc in xeOtoSnapshot.documents) {
                                    val timelines = mutableListOf<TimelineData>()
                                    val timelineSnapshot = db.collection("lichsuhoatdong")
                                        .document(date)
                                        .collection("xeoto")
                                        .document(xeOtoDoc.id)
                                        .collection("timeline")
                                        .get().await()

                                    for (timelineDoc in timelineSnapshot.documents) {
                                        timelines.add(
                                            TimelineData(
                                                timelineId = timelineDoc.id,
                                                fields = timelineDoc.data ?: emptyMap()
                                            )
                                        )
                                    }

                                    if (timelines.isNotEmpty()) {
                                        vehicleGroups.add(
                                            VehicleGroup(
                                                vehicleType = "Xe ô tô",
                                                licensePlate = xeOtoDoc.id,
                                                timelines = timelines
                                            )
                                        )
                                    }
                                }

                                if (vehicleGroups.isNotEmpty()) {
                                    tempList.add(DateGroup(date, vehicleGroups))
                                }
                            }

                            dateGroups = tempList
                            android.util.Log.d("DashboardGuard", "🎉 Đã set dateGroups = ${dateGroups.size}")

                            if (tempList.isEmpty()) {
                                errorMessage = "Không có dữ liệu"
                            }

                        } catch (e: Exception) {
                            errorMessage = "Lỗi: ${e.message}"
                            android.util.Log.e("DashboardGuard", " Lỗi", e)
                        } finally {
                            isLoading = false
                            android.util.Log.d("DashboardGuard", "🏁 Kết thúc. dateGroups.size = ${dateGroups.size}")
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6699FF)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Xem lịch sử hoạt động", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ PHẦN HIỂN THỊ KẾT QUẢ
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when {
                    isLoading -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFF6699FF))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Đang tải dữ liệu...", fontSize = 14.sp, color = Color.Gray)
                        }
                    }

                    errorMessage != null -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "❌ $errorMessage",
                                color = Color.Red,
                                fontSize = 14.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }

                    dateGroups.isNotEmpty() -> {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Text(
                                "Lịch sử hoạt động (${dateGroups.size} ngày)",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(dateGroups) { dateGroup ->
                                    DateGroupItem(dateGroup)
                                }
                            }
                        }
                    }

                    else -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "Chưa có dữ liệu lịch sử",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                            Text(
                                "Bấm nút 'Xem lịch sử hoạt động' để tải",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }

            // Nút đăng xuất ở cuối
            Button(
                onClick = {
                    vm.logout()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                ),
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 16.dp)
            ) {
                Text("Đăng xuất")
            }
        }
    }
}

@Composable
fun DateGroupItem(dateGroup: DateGroup) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF6699FF)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Ngày: ${dateGroup.date}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "${dateGroup.vehicleGroups.size} phương tiện",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Thu gọn" else "Mở rộng",
                    tint = Color.White
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    dateGroup.vehicleGroups.forEach { vehicleGroup ->
                        VehicleGroupItem(vehicleGroup)
                    }
                }
            }
        }
    }
}

@Composable
fun VehicleGroupItem(vehicleGroup: VehicleGroup) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${vehicleGroup.vehicleType}: ${vehicleGroup.licensePlate}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "${vehicleGroup.timelines.size} timeline",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Thu gọn" else "Mở rộng",
                    tint = Color.Black
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    vehicleGroup.timelines.forEach { timeline ->
                        TimelineItem(timeline)
                    }
                }
            }
        }
    }
}

@Composable
fun TimelineItem(timeline: TimelineData) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Timeline: ${timeline.timelineId}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Thu gọn" else "Mở rộng",
                    tint = Color.DarkGray
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = Color.LightGray)
                Spacer(modifier = Modifier.height(8.dp))

                if (timeline.fields.isEmpty()) {
                    Text("Không có dữ liệu", color = Color.Gray, fontSize = 14.sp)
                } else {
                    timeline.fields.forEach { (key, value) ->
                        val valueStr = value?.toString() ?: "null"

                        // Kiểm tra xem có phải link Cloudinary không
                        if (valueStr.contains("cloudinary.com", ignoreCase = true) &&
                            (valueStr.endsWith(".jpg", ignoreCase = true) ||
                                    valueStr.endsWith(".png", ignoreCase = true) ||
                                    valueStr.endsWith(".jpeg", ignoreCase = true) ||
                                    valueStr.contains("/image/upload/"))) {

                            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                                Text(
                                    text = "$key:",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                // Hiển thị hình ảnh
                                AsyncImage(
                                    model = valueStr,
                                    contentDescription = key,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentScale = ContentScale.Fit
                                )

                                Text(
                                    text = valueStr,
                                    fontSize = 10.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        } else {
                            // Hiển thị field thông thường
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "$key:",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = valueStr,
                                    fontSize = 14.sp,
                                    color = Color.DarkGray,
                                    modifier = Modifier.weight(1.5f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}