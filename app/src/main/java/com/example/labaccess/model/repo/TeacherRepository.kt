package com.example.labaccess.model.repo

import android.util.Log
import com.example.labaccess.model.data.AccessCard
import com.example.labaccess.model.data.Teacher
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TeacherRepository {

    private val db = FirebaseFirestore.getInstance()
    private val teacherCollection = db.collection("teachers")

    // Actualizar un teacher
    suspend fun updateTeacher(teacherId: String, updatedFields: Map<String, Any?>): Boolean {
        return try {
            teacherCollection.document(teacherId).update(updatedFields).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getTeacher(teacherId: String): Teacher? {
        return try {
            // Obtener el documento principal
            val document = teacherCollection.document(teacherId).get().await()

            if (document.exists()) {
                // Mapear el documento principal a un objeto Teacher
                val teacher = document.toObject(Teacher::class.java) ?: return null

                // Obtener la subcolección access card
                val accessCardSnapshot = teacherCollection.document(teacherId)
                    .collection("accessCard").get().await()
                val accessCard = accessCardSnapshot.documents.mapNotNull { document ->
                    val card = document.toObject(AccessCard::class.java)
                    card?.copy(id = document.id) // Asignar la id del documento al objeto
                }
                
                // Retornar el objeto Teacher completo con las subcolecciones
                teacher.copy(accessCard = accessCard)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Obtener lista de cards
    suspend fun getAccessCards(teacherId: String): List<AccessCard>? {
        val teacher = getTeacher(teacherId)
        return teacher?.accessCard
    }

    // Agregar una nueva card
    suspend fun addAccessCard(teacherId: String, card: AccessCard): Boolean {
        // Se excluye el atributo id para que Firestore genere uno nueva
        val addCard = mapOf(
            "cardNumber" to card.cardNumber,
            "issueDate" to card.issueDate,
            "expiryDate" to card.expiryDate,
            "state" to card.state
        )
        return try {
            teacherCollection.document(teacherId)
                .collection("accessCard")
                .add(addCard) // Agregar un nuevo documento
                .await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Actualizar una card específica
    suspend fun updateAccessCard(teacherId: String, card: AccessCard): Boolean {
        val id = card.id
        // No se considera el id en la actualización
        val updatedCard = mapOf(
            "cardNumber" to card.cardNumber,
            "issueDate" to card.issueDate,
            "expiryDate" to card.expiryDate,
            "state" to card.state
        )
        return try {
            teacherCollection.document(teacherId)
                .collection("accessCard")
                .document(id)
                .set(updatedCard) // Sobrescribe solo este documento
                .await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Eliminar una card específica
    suspend fun removeAccessCard(teacherId: String, id: String): Boolean {
        return try {
            teacherCollection.document(teacherId)
                .collection("accessCard")
                .document(id)
                .delete()
                .await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
}
