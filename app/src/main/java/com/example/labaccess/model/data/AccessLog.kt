package com.example.labaccess.model.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import java.util.Date

data class AccessLog(
    val entryTime: Timestamp,              // Fecha de entrada del log
    val exitTime: Timestamp,               // Fecha de salida del log
    val labId: DocumentReference,     // Referencia al documento del laboratorio
    val teacherId: DocumentReference  // Referencia al documento del docente
)
