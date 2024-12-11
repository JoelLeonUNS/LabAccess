package com.example.labaccess.model.repo

import android.util.Log
import com.example.labaccess.model.data.Assignment
import com.example.labaccess.model.data.LabUsageReport
import com.example.labaccess.model.data.Laboratory
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class LabUseRepository {

    private val firestore = FirebaseFirestore.getInstance()

    // Cambiar para devolver el objeto Laboratory completo
    fun getLaboratory(callback: (List<Laboratory>) -> Unit) {
        firestore.collection("laboratories").get()
            .addOnSuccessListener { querySnapshot ->
                val labList = mutableListOf<Laboratory>()
                for (document in querySnapshot.documents) {
                    val labName = document.getString("name")
                    val labId = document.id  // Usamos el ID del documento como id
                    val labCapacity = document.getLong("capacity")?.toInt() ?: 0
                    val labDescription = document.getString("description") ?: ""
                    val accessCardList = document.get("accessCard") as? List<Assignment> ?: listOf()

                    if (labName != null) {
                        val lab = Laboratory(
                            id = labId,
                            name = labName,
                            capacity = labCapacity,
                            description = labDescription,
                        )
                        labList.add(lab)
                    }
                }
                callback(labList)  // Pasamos la lista de objetos Laboratory al callback
            }
            .addOnFailureListener { exception ->
                Log.e("LabUseRepository", "Error getting labs: ", exception)
            }
    }

    // Método para obtener los reportes de uso (sin cambios aquí)
    fun getLabUsageReports(startDate: String, endDate: String, labId: String, callback: (List<LabUsageReport>) -> Unit) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Formato de fecha
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault()) // Formato de hora

        // Configurar zona horaria
        val timeZone = TimeZone.getTimeZone("America/Lima") // Ajusta según tu ubicación
        dateFormat.timeZone = timeZone
        timeFormat.timeZone = timeZone


        try {
            val startDateParsed = dateFormat.parse(startDate)
            val endDateParsed = dateFormat.parse(endDate)

            val startTimestamp = Timestamp(startDateParsed!!)
            val endTimestamp = Timestamp(endDateParsed!!)

            val labRef = firestore.collection("laboratories").document(labId)

            val accessLogsRef = firestore.collection("accessLogs")
                .whereEqualTo("labId", labRef)
                .whereGreaterThanOrEqualTo("entryTime", startTimestamp)
                .whereLessThanOrEqualTo("exitTime", endTimestamp)

            Log.d("LabUseReport", "Consultando entre: $startTimestamp y $endTimestamp para laboratorio: $labId")

            accessLogsRef.get().addOnSuccessListener { querySnapshot ->
                val reports = mutableListOf<LabUsageReport>()

                for (document in querySnapshot.documents) {
                    val labUsageReport = LabUsageReport()

                    // Obtener referencia del profesor
                    val teacherRef = document.getDocumentReference("teacherId")

                    // Obtener información de laboratorio
                    labRef.get().addOnSuccessListener { labSnapshot ->
                        labUsageReport.labName = labSnapshot.getString("name") ?: "Nombre no disponible"

                        // Obtener información del profesor
                        teacherRef?.get()?.addOnSuccessListener { teacherSnapshot ->
                            labUsageReport.teacherName = teacherSnapshot.getString("name") ?: "Nombre no disponible"

                            // Convertir fechas de Timestamp a String
                            labUsageReport.entryTime = document.getTimestamp("entryTime")?.toDate()?.let { date ->
                                timeFormat.format(date)
                            } ?: "Fecha no disponible"

                            labUsageReport.exitTime = document.getTimestamp("exitTime")?.toDate()?.let { date ->
                                timeFormat.format(date)
                            } ?: "Fecha no disponible"

                            labUsageReport.usageDate = document.getTimestamp("entryTime")?.toDate()?.let { date ->
                                dateFormat.format(date) // Solo la fecha
                            } ?: "Fecha no disponible"

                            // Añadir el reporte a la lista
                            reports.add(labUsageReport)

                            // Verificar si todos los reportes han sido procesados
                            if (reports.size == querySnapshot.size()) {
                                Log.d("LabUseRepository", "Reportes obtenidos: ${reports.size}")
                                callback(reports)
                            }
                        }?.addOnFailureListener { teacherError ->
                            Log.e("LabUseRepository", "Error al obtener el profesor: ${teacherError.message}")
                        }
                    }.addOnFailureListener { labError ->
                        Log.e("LabUseRepository", "Error al obtener el laboratorio: ${labError.message}")
                    }
                }
            }.addOnFailureListener { exception ->
                Log.e("LabUseRepository", "Error al obtener los reportes: ${exception.message}")
            }
        } catch (e: Exception) {
            Log.e("LabUseRepository", "Error al procesar fechas: ${e.message}")
        }
    }




}


