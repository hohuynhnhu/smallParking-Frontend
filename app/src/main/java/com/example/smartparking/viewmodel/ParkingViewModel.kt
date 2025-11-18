

package com.example.smartparking.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ParkingStatus(
    val trangthai: Boolean = false,
    val timestamp: String = "",
    val canhbao: Boolean = false
)

class ParkingViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance()

    private val _parkingStatus = MutableStateFlow<ParkingStatus?>(null)
    val parkingStatus: StateFlow<ParkingStatus?> = _parkingStatus

    // Load trạng thái xe realtime
    fun loadParkingStatus(licensePlate: String) {
        val ref = database.getReference("biensotrongbai").child(licensePlate)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val trangthai = snapshot.child("trangthai").getValue(Boolean::class.java) ?: false
                val timestamp = snapshot.child("timestamp").getValue(String::class.java) ?: ""
                val canhbao = snapshot.child("canhbao").getValue(Boolean::class.java) ?: false

                _parkingStatus.value = ParkingStatus(
                    trangthai = trangthai,
                    timestamp = timestamp,
                    canhbao = canhbao
                )
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    // Update trạng thái xe
    fun updateTrangThai(licensePlate: String, newStatus: Boolean) {
        val ref = database.getReference("biensotrongbai").child(licensePlate)
        ref.child("trangthai").setValue(newStatus)
    }

    // ===== HÀM MỚI: XỬ LÝ KHI USER XÁC NHẬN LÀ MÌNH =====
    fun clearAlertAndLeave(licensePlate: String) {
        val ref = database.getReference("biensotrongbai").child(licensePlate)

        // Tạo map để update nhiều field cùng lúc
        val updates = hashMapOf<String, Any>(
            "canhbao" to false,      // Tắt cảnh báo
            "trangthai" to false     // Set xe đã rời đi
        )

        // Update tất cả cùng lúc
        ref.updateChildren(updates)
            .addOnSuccessListener {
                println("Đã cập nhật: canhbao=false, trangthai=false")
            }
            .addOnFailureListener { error ->
                println("Lỗi cập nhật: ${error.message}")
            }
    }

    // Tắt cảnh báo (không đổi trạng thái xe)
    fun clearAlert(licensePlate: String) {
        val ref = database.getReference("biensotrongbai").child(licensePlate)
        ref.child("canhbao").setValue(false)
    }
}
