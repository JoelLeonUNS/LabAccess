package com.example.labaccess.model.data

import com.google.firebase.Timestamp

data class AccessCard(
    val id: String = "",
    val cardNumber: String = "",
    val state: String = "",
    val issueDate: Timestamp = Timestamp.now(), // Cambia String a Timestamp
    val expiryDate: Timestamp = Timestamp.now(),
)
