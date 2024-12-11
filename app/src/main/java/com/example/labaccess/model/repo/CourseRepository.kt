package com.example.labaccess.model.repo

import android.util.Log
import com.example.labaccess.model.data.AccessCard
import com.example.labaccess.model.data.Assignment
import com.example.labaccess.model.data.Course
import com.example.labaccess.model.data.Laboratory
import com.example.labaccess.model.data.LaboratoryJoin
import com.example.labaccess.model.data.Teacher
import com.example.labaccess.model.data.TeacherAccessCard
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class CourseRepository {

    private val db = FirebaseFirestore.getInstance()
    private val courseCollection = db.collection("courses")


    suspend fun addNewCourse(userData: Map<String, Any?>): Boolean {
        return try {
            courseCollection.document().set(userData).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    // Actualizar un course
    suspend fun updateCourse(courseId: String, updatedFields: Map<String, Any?>): Boolean {
        return try {
            courseCollection.document(courseId).update(updatedFields).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getCourse(courseId: String): Course? {
        return try {
            // Obtener el documento principal
            val document = courseCollection.document(courseId).get().await()

            if (document.exists()) {
                // Mapear el documento principal a un objeto Course
                document.toObject(Course::class.java) ?: return null
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getAllcourses(): List<Course> {
        return try {
            val snapshot = courseCollection.get().await()
            snapshot.documents.mapNotNull { document ->
                val course = document.toObject(Course::class.java) ?: return@mapNotNull null
                course.copy(id = document.id)
            }
        } catch (e: Exception) {
            Log.d("CourseRepository", "getAllcourses: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }
    
}
