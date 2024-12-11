package com.example.labaccess.model.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

data class Assignment(
    val id: String = "",
    val courseName: String = "",
    val dayOfWeek: String = "",
    val endDate: Timestamp = Timestamp.now(),
    val startDate: Timestamp = Timestamp.now(),
    val timeSlot: String ="",
    val courseId: DocumentReference? = null,
    val teacherId: DocumentReference? = null
)
