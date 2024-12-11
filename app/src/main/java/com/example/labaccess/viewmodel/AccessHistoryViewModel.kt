package com.example.labaccess.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.labaccess.model.data.AccessLog
import com.example.labaccess.model.data.Laboratory
import com.example.labaccess.model.data.Teacher
import com.example.labaccess.model.repo.AccessLogRepository
import com.example.labaccess.model.repo.LaboratoryRepository
import com.example.labaccess.model.repo.TeacherRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.launch

class AccessHistoryViewModel : ViewModel() {

    // Repositorios
    private val accessLogRepository = AccessLogRepository()
    private val teacherRepository = TeacherRepository()
    private val laboratoryRepository = LaboratoryRepository()

    // LiveData para los logs de acceso, docentes y laboratorios
    private val _accessLogs = MutableLiveData<List<AccessLog>>()
    val accessLogs: LiveData<List<AccessLog>> get() = _accessLogs

    private val _teachers = MutableLiveData<List<Teacher>>()
    val teachers: LiveData<List<Teacher>> get() = _teachers

    private val _laboratories = MutableLiveData<List<Laboratory>>()  // Corregir el nombre aquí
    val laboratory: LiveData<List<Laboratory>> get() = _laboratories

    // Inicialización: Cargar docentes y laboratorios
    init {
        loadTeachers()
        loadLaboratory()
    }

    // Método para cargar docentes desde el repositorio
    fun loadTeachers() {
        viewModelScope.launch {
            try {
                val result = teacherRepository.getAllTeachers()
                _teachers.postValue(result) // Usar postValue() en lugar de value
            } catch (e: Exception) {
                // Manejo de errores, puedes actualizar el _teachers con una lista vacía o con un mensaje de error.
                _teachers.postValue(emptyList())
            }
        }
    }

    // Método para cargar laboratorios desde el repositorio
    fun loadLaboratory() {
        viewModelScope.launch {
            try {
                val result = laboratoryRepository.getAllLaboratories()
                _laboratories.postValue(result) // Usar postValue() en lugar de value
            } catch (e: Exception) {
                // Manejo de errores, puedes actualizar el _laboratories con una lista vacía o con un mensaje de error.
                _laboratories.postValue(emptyList())
            }
        }
    }

    // Método para filtrar los registros de acceso según las fechas
// Método para filtrar los registros de acceso según las fechas
    fun filterAccessLogs(startTimestamp: Timestamp?, endTimestamp: Timestamp?) {
        viewModelScope.launch {
            val logs = accessLogRepository.getAccessLogsFiltered(startTimestamp, endTimestamp)

            // Usar postValue en lugar de setValue
            _accessLogs.postValue(logs)
        }
    }


    // Método para obtener los datos de un docente
    fun getTeacherData(teacherRef: DocumentReference): LiveData<Teacher?> {
        val result = MutableLiveData<Teacher?>()
        viewModelScope.launch {
            try {
                val teacher = teacherRepository.getTeacherData(teacherRef) // Corregir para usar teacherRepository
                result.postValue(teacher)
            } catch (e: Exception) {
                result.postValue(null)
            }
        }
        return result
    }

    // Método para obtener los datos de un laboratorio
    fun getLaboratoryData(labRef: DocumentReference): LiveData<Laboratory?> {
        val result = MutableLiveData<Laboratory?>() // Corregir para usar Laboratory en lugar de Laboratory
        viewModelScope.launch {
            try {
                val laboratory = laboratoryRepository.getLaboratoryData(labRef) // Corregir para usar laboratoryRepository
                result.postValue(laboratory)
            } catch (e: Exception) {
                result.postValue(null)
            }
        }
        return result
    }
}
