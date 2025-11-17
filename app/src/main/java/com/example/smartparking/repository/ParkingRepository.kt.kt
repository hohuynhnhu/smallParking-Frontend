package com.example.smartparking.repository

import com.example.smartparking.model.DayHistoryModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ParkingRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val collection = firestore.collection("lichsuhoatdong")

    suspend fun getAllHistory(): List<DayHistoryModel> {
        val result = mutableListOf<DayHistoryModel>()

        // Lấy tất cả ngày
        val daysSnapshot = collection.get().await()

        for (dayDoc in daysSnapshot.documents) {
            val day = dayDoc.id  // ví dụ "09112025"

            // Lấy danh sách loại xe (xemay / oto)
            val typeSnapshot = collection.document(day).get().await()

            val mapType = typeSnapshot.data ?: continue

            for ((vehicleType, _) in mapType) {

                // Lấy danh sách biển số
                val platesSnapshot = collection.document(day)
                    .collection(vehicleType)
                    .get()
                    .await()

                for (plateDoc in platesSnapshot.documents) {
                    val plate = plateDoc.id

                    // Lấy timeline
                    val timelineSnapshot = collection.document(day)
                        .collection(vehicleType)
                        .document(plate)
                        .collection("timeline")
                        .get()
                        .await()

                    val timelineList = timelineSnapshot.documents.map { it.data ?: emptyMap<String, Any>() }

                    // Thêm vào list kết quả
                    result.add(
                        DayHistoryModel(
                            date = day,
                            vehicleType = vehicleType,
                            plate = plate,
                            timeline = timelineList
                        )
                    )
                }
            }
        }

        return result
    }
}
