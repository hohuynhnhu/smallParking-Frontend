package com.example.smartparking.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartparking.model.DayHistoryModel
import com.example.smartparking.model.ParkingStatus
import com.example.smartparking.repository.ParkingRepository
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ParkingViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance().getReference("biensoxetrongbai")

    private val _parkingStatus = MutableStateFlow<ParkingStatus?>(null)
    val parkingStatus = _parkingStatus.asStateFlow()
    private val repo = ParkingRepository()

    var history by mutableStateOf<List<DayHistoryModel>>(emptyList())
        private set

    var loading by mutableStateOf(false)

    fun loadHistory() {
        viewModelScope.launch {
            loading = true
            history = repo.getAllHistory()
            loading = false
        }
    }
    fun loadParkingStatus(plate: String) {
        db.child(plate).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _parkingStatus.value = snapshot.getValue(ParkingStatus::class.java)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun updateTrangThai(plate: String, value: Boolean) {
        db.child(plate).child("trangthai").setValue(value)
    }
}
