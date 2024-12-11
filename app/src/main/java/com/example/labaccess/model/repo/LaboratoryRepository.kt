package com.example.labaccess.model.repo

import android.util.Log
import com.example.labaccess.model.data.AccessCard
import com.example.labaccess.model.data.Assignment
import com.example.labaccess.model.data.Course
import com.example.labaccess.model.data.Laboratory
import com.example.labaccess.model.data.LaboratoryJoin
import com.example.labaccess.model.data.Teacher
import com.example.labaccess.model.data.TeacherAccessCard
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class LaboratoryRepository {

    private val db = FirebaseFirestore.getInstance()
    private val laboratoryCollection = db.collection("laboratories")


    suspend fun addNewLaboratory(userData: Map<String, Any?>): Boolean {
        return try {
            laboratoryCollection.document().set(userData).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    // Actualizar un laboratory
    suspend fun updateLaboratory(laboratoryId: String, updatedFields: Map<String, Any?>): Boolean {
        return try {
            laboratoryCollection.document(laboratoryId).update(updatedFields).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getLaboratory(laboratoryId: String): Laboratory? {
        return try {
            // Obtener el documento principal
            val document = laboratoryCollection.document(laboratoryId).get().await()

            if (document.exists()) {
                // Mapear el documento principal a un objeto Laboratory
                val laboratory = document.toObject(Laboratory::class.java) ?: return null

                // Obtener la subcolección access assignment
                val assignmentSnapshot = laboratoryCollection.document(laboratoryId)
                    .collection("assignments").get().await()
                val assignment = assignmentSnapshot.documents.mapNotNull { document ->
                    val assignment = document.toObject(Assignment::class.java)
                    assignment?.copy(id = document.id) // Asignar la id del documento al objeto
                }
                
                // Retornar el objeto Laboratory completo con las subcolecciones
                laboratory.copy(assignments = assignment)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getAllLaboratories(): List<Laboratory> {
        return try {
            val snapshot = laboratoryCollection.get().await()
            snapshot.documents.mapNotNull { document ->
                val laboratory = document.toObject(Laboratory::class.java) ?: return@mapNotNull null
                Log.d("LaboratoryRepository", "getAllLaboratories: ${laboratory.name}")
                laboratory.copy(id = document.id)
            }
        } catch (e: Exception) {
            Log.d("LaboratoryRepository", "getAllLaboratories: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getAllLaboratoryJoin(): List<LaboratoryJoin> {
        return try {
            // Obtiene todos los documentos de la colección de laboratorios
            val snapshot = laboratoryCollection.get().await()

            // Mapea los laboratorios y sus datos asociados
            snapshot.documents.mapNotNull { document ->
                // Convierte el documento actual a un objeto Laboratory
                val laboratory = document.toObject(Laboratory::class.java)?.copy(id = document.id)
                    ?: return@mapNotNull null

                // Obtiene los assignments relacionados con el laboratorio actual
                val assignmentSnapshot = laboratoryCollection.document(laboratory.id)
                    .collection("assignments").get().await()

                assignmentSnapshot.documents.mapNotNull { assignmentDocument ->
                    val assignment = assignmentDocument.toObject(Assignment::class.java)?.copy(id = assignmentDocument.id)
                        ?: return@mapNotNull null

                    // Obtiene los datos del profesor relacionado
                    val teacherData = assignment.teacherId?.let { teacherRef ->
                        teacherRef.get().await().toObject(Teacher::class.java)
                    }

                    // Obtiene los datos del curso relacionado
                    val courseData = assignment.courseId?.let { courseRef ->
                        courseRef.get().await().toObject(Course::class.java)
                    }

                    // Crea y retorna el objeto LaboratoryJoin
                    LaboratoryJoin(
                        id = laboratory.id,
                        capacity = laboratory.capacity,
                        description = laboratory.description,
                        name = laboratory.name,
                        dayOfWeek = assignment.dayOfWeek,
                        endDate = assignment.endDate,
                        startDate = assignment.startDate,
                        timeSlot = assignment.timeSlot,
                        teacher = teacherData?.name.orEmpty(),
                        course = courseData?.name.orEmpty()
                    )
                }
            }.flatten()
        } catch (e: Exception) {
            Log.e("LaboratoryRepository", "Error: ${e.message}", e)
            emptyList()
        }
    }

    // Obtener lista de assignments
    suspend fun getAssignments(laboratoryId: String): List<Assignment>? {
        val laboratory = getLaboratory(laboratoryId)
        return laboratory?.assignments
    }

    suspend fun getAllAssignments(): List<Assignment> {
        return try {
            val snapshot = laboratoryCollection.get().await()
            snapshot.documents.flatMap { document ->
                val assignmentSnapshot = laboratoryCollection.document(document.id)
                    .collection("assignment").get().await()
                assignmentSnapshot.documents.mapNotNull { assignmentDocument ->
                    val assignment = assignmentDocument.toObject(Assignment::class.java)
                    assignment?.copy(id = assignmentDocument.id)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // Agregar una nueva assignment
    suspend fun addAssignment(laboratoryId: String, assignment: Assignment): Boolean {
        // Se excluye el atributo id para que Firestore genere uno nueva
        val addCard = mapOf(
            "courseName" to assignment.courseName,
            "dayOfWeek" to assignment.dayOfWeek,
            "endDate" to assignment.endDate,
            "startDate" to assignment.startDate,
            "timeSlot" to assignment.timeSlot
        )
        return try {
            laboratoryCollection.document(laboratoryId)
                .collection("assignment")
                .add(addCard) // Agregar un nuevo documento
                .await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Actualizar una assignment específica
    suspend fun updateAssignment(laboratoryId: String, assignment: Assignment): Boolean {
        val id = assignment.id
        // No se considera el id en la actualización
        val updatedCard = mapOf(
            "courseName" to assignment.courseName,
            "dayOfWeek" to assignment.dayOfWeek,
            "endDate" to assignment.endDate,
            "startDate" to assignment.startDate,
            "timeSlot" to assignment.timeSlot
        )
        return try {
            laboratoryCollection.document(laboratoryId)
                .collection("assignment")
                .document(id)
                .set(updatedCard) // Sobrescribe solo este documento
                .await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Eliminar una assignment específica
    suspend fun removeAssignment(laboratoryId: String, id: String): Boolean {
        return try {
            laboratoryCollection.document(laboratoryId)
                .collection("assignment")
                .document(id)
                .delete()
                .await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getLaboratoryData(labRef: DocumentReference): Laboratory? {
        return try {
            // Hacer la consulta a Firestore para obtener el documento del laboratorio
            val documentSnapshot = labRef.get().await()
            // Verificar si el documento existe
            if (documentSnapshot.exists()) {
                // Convertir el documento en un objeto Laboratories
                documentSnapshot.toObject(Laboratory::class.java)
            } else {
                null // Si el documento no existe, devolver null
            }
        } catch (e: Exception) {
            // Manejar cualquier excepción (por ejemplo, problemas de red)
            null
        }
    }
    
}
