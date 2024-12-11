package com.example.labaccess.model.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

data class LaboratoryJoin(
    val id: String = "",
    val capacity: Int = 0,
    val description: String = "",
    val name: String = "",
    val dayOfWeek: String = "",
    val endDate: Timestamp = Timestamp.now(),
    val startDate: Timestamp = Timestamp.now(),
    val timeSlot: String ="",
    val course: String = "",
    val teacher: String = ""
)
