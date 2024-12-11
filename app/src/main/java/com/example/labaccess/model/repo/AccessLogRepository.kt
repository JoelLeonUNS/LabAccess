package com.example.labaccess.model.repo

import com.example.labaccess.model.data.AccessLog
import com.example.labaccess.model.data.Laboratory
import com.example.labaccess.model.data.Teacher
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class AccessLogRepository {

    private val db = FirebaseFirestore.getInstance()
    private val accessLogCollection = db.collection("accessLogs")

    // Agregar un nuevo registro de acceso
    suspend fun addAccessLog(accessLog: AccessLog): Boolean {
        return try {
            val logData = mapOf(
                "entryTime" to accessLog.entryTime,
                "exitTime" to accessLog.exitTime,
                "labId" to accessLog.labId,  // Aquí pasas la referencia del documento del laboratorio
                "teacherId" to accessLog.teacherId  // Aquí pasas la referencia del documento del docente
            )
            // Añadir el AccessLog con las referencias a Firestore
            accessLogCollection.add(logData).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    fun getDocumentReference(collectionName: String, documentId: String): DocumentReference {
        return db.collection(collectionName).document(documentId)
    }
    // Obtener todos los registros de acceso
    suspend fun getAllAccessLogs(): List<AccessLog> {
        return try {
            val snapshot = accessLogCollection.get().await()
            snapshot.documents.mapNotNull { doc ->
                val entryTime = doc.getTimestamp("entryTime") ?: Timestamp.now()  // Mantenerlo como Timestamp
                val exitTime = doc.getTimestamp("exitTime") ?: Timestamp.now()  // Mantenerlo como Timestamp

                // Obtener las referencias a los documentos completos
                val labId = doc.getDocumentReference("labId")  // Aquí obtienes la referencia al documento del laboratorio
                val teacherId = doc.getDocumentReference("teacherId")  // Aquí obtienes la referencia al documento del docente

                // Crear el objeto AccessLog con las referencias completas
                if (labId != null && teacherId != null) {
                    AccessLog(entryTime, exitTime, labId, teacherId)
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // Método para obtener los registros de acceso filtrados por fechas
    // Método para obtener los registros de acceso filtrados por fechas
    suspend fun getAccessLogsFiltered(startDate: Timestamp?, endDate: Timestamp?): List<AccessLog> {
        return try {
            var query: Query = accessLogCollection

            // Filtrar por fecha de entrada (startDate)
            startDate?.let {
                query = query.whereGreaterThanOrEqualTo("entryTime", it)  // Usar directamente Timestamp
            }

            // Filtrar por fecha de salida (endDate)
            endDate?.let {
                query = query.whereLessThanOrEqualTo("exitTime", it)  // Usar directamente Timestamp
            }

            // Obtener los documentos que coinciden con el filtro
            val querySnapshot = query.get().await()

            // Mapear los documentos obtenidos a objetos AccessLog
            querySnapshot.documents.mapNotNull { doc ->
                val entryTime = doc.getTimestamp("entryTime") ?: Timestamp.now()  // Mantenerlo como Timestamp
                val exitTime = doc.getTimestamp("exitTime") ?: Timestamp.now()  // Mantenerlo como Timestamp
                val labId = doc.getDocumentReference("labId")
                val teacherId = doc.getDocumentReference("teacherId")

                // Crear el objeto AccessLog con las referencias completas
                if (labId != null && teacherId != null) {
                    AccessLog(entryTime, exitTime, labId, teacherId)
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }


    // Eliminar un registro de acceso
    suspend fun deleteAccessLog(logId: String): Boolean {
        return try {
            accessLogCollection.document(logId).delete().await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Métodos para obtener los datos de laboratorio y docente (como ya estaban)
    suspend fun getLaboratoryData(labRef: DocumentReference): Laboratory? {
        return try {
            val document = labRef.get().await()  // Obtener el documento completo de la referencia
            document.toObject(Laboratory::class.java)  // Mapear el documento a la clase correspondiente
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getTeacherData(teacherRef: DocumentReference): Teacher? {
        return try {
            val document = teacherRef.get().await()  // Obtener el documento completo de la referencia
            document.toObject(Teacher::class.java)  // Mapear el documento a la clase correspondiente
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
