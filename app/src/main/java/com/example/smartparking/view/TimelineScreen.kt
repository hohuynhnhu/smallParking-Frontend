package com.example.smartparking.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.smartparking.model.TimelineItem
import com.example.smartparking.repository.FirestoreRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineScreen(
    date: String,
    vehicleType: String,
    plate: String,
    navController: NavController
) {
    val repo = FirestoreRepository()
    var items by remember { mutableStateOf<List<TimelineItem>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        items = repo.getTimeline(date, vehicleType, plate)
        loading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Timeline $plate") })
        }
    ) { padding ->

        if (loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                text = "Ngày $date — Loại xe: $vehicleType",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            items.forEach { item ->
                TimelineCard(item)
            }
        }
    }
}
