package com.example.smartparking.model

data class ParkingStatus(
    val trangthai: Boolean? = null,      // xe đang đỗ hay không
    val timestamp: String? = null,       // thời gian vào
    val canhbao: Boolean? = null         // cảnh báo (nếu có)
)
