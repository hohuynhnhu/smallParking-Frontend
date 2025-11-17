package com.example.smartparking.model

data class DayHistoryModel(
    val date: String = "",                      // ví dụ: "09112025"
    val vehicleType: String = "",               // ví dụ: "xemay" hoặc "oto"
    val plate: String = "",                     // ví dụ: "71H112345"
    val timeline: List<Map<String, Any>> = emptyList()  // danh sách timeline (timeline0, timeline1...)
)
