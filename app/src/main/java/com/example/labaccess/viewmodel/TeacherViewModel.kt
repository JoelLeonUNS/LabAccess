package com.example.snapchance.viewModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.labaccess.model.data.Teacher
import com.example.labaccess.model.repo.TeacherRepository
import kotlinx.coroutines.launch

class TeacherViewModel: ViewModel() {

    // Repositorio para manejar los datos del trabajador
    private val repository = TeacherRepository()

    // Lista observable de profesores
    private val _teachers = MutableLiveData<List<Teacher>>()
    val teachers: LiveData<List<Teacher>> get() = _teachers

    // LiveData para el nombre
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    // LiveData para el email
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    // LiveData para el estado
    private val _state = MutableLiveData<String>()
    val state: LiveData<String> get() = _state

    // LiveData para el resultado de la actualización
    private val _updateResult = MutableLiveData<Boolean?>()
    val updateResult: LiveData<Boolean?> get() = _updateResult

    // Método para actualizar el nombre
    fun updateName(name: String) {
        _name.value = name
    }

    // Método para actualizar el email
    fun updateEmail(email: String) {
        _email.value = email
    }

    // Método para actualizar el estado
    fun updateState(state: String) {
        _state.value = state
    }

    // Método para obtener los datos de un teacher
    fun fetchTeacher(teacherId: String) {
        viewModelScope.launch {
            val teacherData = repository.getTeacher(teacherId)
            if (teacherData != null) {
                _name.value = teacherData.name
                _email.value = teacherData.email
                _state.value = teacherData.state
            } else {
                _name.value = ""
                _email.value = ""
                _state.value = ""
            }
        }
    }

    fun fetchAllTeachers() {
        viewModelScope.launch {
            val teachers = repository.getAllTeachers()
            _teachers.value = teachers
        }
    }

    // Método para actualizar los datos del trabajador
    fun saveTeacher(teacherId: String) {
        val fields = mapOf(
            "name" to _name.value,
            "email" to _email.value,
            "state" to _state.value
        )
        viewModelScope.launch {
            val result = repository.updateTeacher(teacherId, fields)
            _updateResult.value = result
        }
    }

    fun resetResult() {
        _updateResult.value = null
    }

}
