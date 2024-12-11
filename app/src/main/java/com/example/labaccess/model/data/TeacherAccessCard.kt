package com.example.labaccess.model.data

import com.google.firebase.Timestamp

data class TeacherAccessCard(
    val id: String = "",
    val cardNumber: String = "",
    val state: String = "",
    val issueDate: Timestamp = Timestamp.now(), // Cambia String a Timestamp
    val expiryDate: Timestamp = Timestamp.now(),
    val teacherId: String = "",
    val name: String = "",
    val email: String = "",
    val stateTeacher: String = "",
)
