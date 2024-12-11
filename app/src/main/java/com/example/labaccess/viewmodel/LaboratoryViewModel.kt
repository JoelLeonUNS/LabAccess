package com.example.snapchance.viewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.labaccess.model.data.Assignment
import com.example.labaccess.model.data.Laboratory
import com.example.labaccess.model.data.LaboratoryJoin
import com.example.labaccess.model.repo.LaboratoryRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class LaboratoryViewModel: ViewModel() {

    // Repositorio para manejar las tarjetas de acceso
    private val repository = LaboratoryRepository()

    // Lista observable de cards
    private val _assignments = MutableLiveData<List<Assignment>>()
    val assignments: LiveData<List<Assignment>> get() = _assignments

    // Lista observable de laboratorios
    private val _laboratories = MutableLiveData<List<Laboratory>>()
    val laboratories: LiveData<List<Laboratory>> get() = _laboratories

    // Lista observable de cards junto con datos del docente
    private val _laboratoryJoin = MutableLiveData<List<LaboratoryJoin>>()
    val laboratoryJoin: LiveData<List<LaboratoryJoin>> get() = _laboratoryJoin

    // LiveData para manejar el resultado de guardar datos
    private val _saveResult = MutableLiveData<Boolean?>()
    val saveResult: LiveData<Boolean?> get() = _saveResult

    private val _removeResult = MutableLiveData<Boolean?>()
    val removeResult: LiveData<Boolean?> get() = _removeResult

    // Live data para los atributos
    private val _id = MutableLiveData<String>()
    val id: LiveData<String> get() = _id

    // LiveData para capacidad
    private val _capacity = MutableLiveData<Int>()
    val capacity: LiveData<Int> get() = _capacity

    // LiveData para la descripción
    private val _description = MutableLiveData<String>()
    val description: LiveData<String> get() = _description

    // LiveData para el nombre
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    // LiveData para el día de la semana
    private val _dayOfWeek = MutableLiveData<String>()
    val dayOfWeek: LiveData<String> get() = _dayOfWeek

    // LiveData para la fecha de inicio
    private val _startDate = MutableLiveData<Timestamp>()
    val startDate: LiveData<Timestamp> get() = _startDate

    // LiveData para la fecha de finalización
    private val _endDate = MutableLiveData<Timestamp>()
    val endDate: LiveData<Timestamp> get() = _endDate

    // LiveData para el horario
    private val _timeSlot = MutableLiveData<String>()
    val timeSlot: LiveData<String> get() = _timeSlot

    // LiveData para el teacherId
    private val _teacherId = MutableLiveData<String>()
    val teacherId: LiveData<String> get() = _teacherId

    // LiveData para el curso
    private val _courseId = MutableLiveData<String>()
    val courseId: LiveData<String> get() = _courseId

    // Actualizar
    fun updateId(id: String) {
        _id.value = id
    }

    fun updateCapacity(capacity: Int) {
        _capacity.value = capacity
    }

    fun updateDescription(description: String) {
        _description.value = description
    }

    fun updateName(name: String) {
        _name.value = name
    }

    fun updateDayOfWeek(dayOfWeek: String) {
        _dayOfWeek.value = dayOfWeek
    }

    fun updateStartDate(startDate: Timestamp) {
        _startDate.value = startDate
    }

    fun updateEndDate(endDate: Timestamp) {
        _endDate.value = endDate
    }

    fun updateTimeSlot(timeSlot: String) {
        _timeSlot.value = timeSlot
    }

    fun updateTeacherId(teacherId: String) {
        _teacherId.value = teacherId
    }

    fun updateCourseId(courseId: String) {
        _courseId.value = courseId
    }

    // Método para guardar (agregar o actualizar) la experiencia laboral actual
    fun saveAssignment() {
        val db = FirebaseFirestore.getInstance()
        val currentItem = Assignment(
            id = _id.value ?: "",
            dayOfWeek = _dayOfWeek.value ?: "",
            endDate = _endDate.value ?: Timestamp.now(),
            startDate = _startDate.value ?: Timestamp.now(),
            timeSlot = _timeSlot.value ?: "",
            teacherId = db.collection("teachers").document(_teacherId.value ?: ""),
            courseId = db.collection("courses").document(_courseId.value ?: "")
        )
        viewModelScope.launch {
            if (currentItem.id.isNotEmpty()) {
                _saveResult.value = repository.updateAssignment(_id.value?:"", currentItem)
            } else {
                _saveResult.value = repository.addAssignment(_id.value?:"", currentItem)
            }
        }
    }

    // Método para eliminar una card
    fun removeAssignment(laboratoryId: String, id: String) {
        viewModelScope.launch {
            _removeResult.value = repository.removeAssignment(laboratoryId, id)
        }
    }

    // Método para cargar cards de un trabajador
    fun fetchAssignments(laboratoryId: String) {
        viewModelScope.launch {
            _assignments.value = repository.getAssignments(laboratoryId)
        }
    }

    fun fecthAllAssignments() {
        viewModelScope.launch {
            _assignments.value = repository.getAllAssignments()
        }
    }

    fun fecthAllLaboratories() {
        viewModelScope.launch {
            _laboratories.value = repository.getAllLaboratories()
        }
    }

    fun fecthAllLaboratoryJoin() {
        viewModelScope.launch {
            _laboratoryJoin.value = repository.getAllLaboratoryJoin()
        }
    }

    fun resetResult() {
        _saveResult.value = null
        _removeResult.value = null
    }

    // Método para limpiar el ítem actual
    fun clearCurrentItem() {
        _id.value = ""
        _dayOfWeek.value = ""
        _startDate.value = Timestamp.now()
        _endDate.value = Timestamp.now()
        _timeSlot.value = ""
    }

}
