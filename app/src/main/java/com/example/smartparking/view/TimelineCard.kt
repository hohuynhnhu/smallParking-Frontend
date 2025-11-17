package com.example.smartparking.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartparking.model.TimelineItem

@Composable
fun TimelineCard(item: TimelineItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(Color(0xFFFFF5F5))
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = item.time,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFFFF6B6B)
            )
            Text(item.action)
        }
    }
}
