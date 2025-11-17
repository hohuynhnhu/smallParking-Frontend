package com.example.smartparking.repository

import com.example.smartparking.model.TimelineItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getTimeline(
        date: String,
        vehicleType: String,   // xemay hoặc xeoto
        plate: String
    ): List<TimelineItem> {

        val snapshot = db.collection("lichsuhoatdong")
            .document(date)
            .collection(vehicleType)
            .document(plate)
            .collection("timeline")
            .get()
            .await()

        return snapshot.toObjects(TimelineItem::class.java)
    }
}
