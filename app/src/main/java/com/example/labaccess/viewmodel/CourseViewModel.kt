package com.example.snapchance.viewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.labaccess.model.data.Assignment
import com.example.labaccess.model.data.Course
import com.example.labaccess.model.data.LaboratoryJoin
import com.example.labaccess.model.repo.CourseRepository
import com.example.labaccess.model.repo.LaboratoryRepository
import kotlinx.coroutines.launch

class CourseViewModel: ViewModel() {

    // Repositorio para manejar las tarjetas de acceso
    private val repository = CourseRepository()

    // Lista observable de cards
    private val _courses = MutableLiveData<List<Course>>()
    val courses: LiveData<List<Course>> get() = _courses

    // LiveData para manejar el resultado de guardar datos
    private val _saveResult = MutableLiveData<Boolean?>()
    val saveResult: LiveData<Boolean?> get() = _saveResult

    private val _removeResult = MutableLiveData<Boolean?>()
    val removeResult: LiveData<Boolean?> get() = _removeResult

    // Live data para los atributos
    private val _id = MutableLiveData<String>()
    val id: LiveData<String> get() = _id

    // LiveData para el nombre
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    // LiveData para la descripción
    private val _description = MutableLiveData<String>()
    val description: LiveData<String> get() = _description

    // Actualizar
    fun upadateId(id: String) {
        _id.value = id
    }

    fun upadateName(name: String) {
        _name.value = name
    }

    fun upadateDescription(description: String) {
        _description.value = description
    }


    fun fecthAllCourses() {
        viewModelScope.launch {
            _courses.value = repository.getAllcourses()
        }
    }

    fun resetResult() {
        _saveResult.value = null
        _removeResult.value = null
    }

    // Método para limpiar el ítem actual
    fun clearCurrentItem() {
        _id.value = ""
        _name.value = ""
        _description.value = ""
    }

}
