package com.example.snapchance.viewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.labaccess.model.data.AccessCard
import com.example.labaccess.model.data.TeacherAccessCard
import com.example.labaccess.model.repo.TeacherRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch

class AccessCardViewModel: ViewModel() {

    // Repositorio para manejar las tarjetas de acceso
    private val repository = TeacherRepository()

    // Lista observable de cards
    private val _accessCards = MutableLiveData<List<AccessCard>>()
    val accessCards: LiveData<List<AccessCard>> get() = _accessCards

    // Lista observable de cards junto con datos del docente
    private val _teacherAccessCards = MutableLiveData<List<TeacherAccessCard>>()
    val teacherAccessCards: LiveData<List<TeacherAccessCard>> get() = _teacherAccessCards

    // LiveData para manejar el resultado de guardar datos
    private val _saveResult = MutableLiveData<Boolean?>()
    val saveResult: LiveData<Boolean?> get() = _saveResult

    private val _removeResult = MutableLiveData<Boolean?>()
    val removeResult: LiveData<Boolean?> get() = _removeResult

    // Live data para los atributos
    private val _id = MutableLiveData<String>()
    val id: LiveData<String> get() = _id

    // LiveData para numero de tarjeta
    private val _cardNumber = MutableLiveData<String>()
    val cardNumber: LiveData<String> get() = _cardNumber
    
    // LiveData para el estado
    private val _state = MutableLiveData<String>()
    val state: LiveData<String> get() = _state
    
    // LiveData para la fecha de emisión
    private val _issueDate = MutableLiveData<Timestamp>()
    val issueDate: LiveData<Timestamp> get() = _issueDate
    
    // LiveData para la fecha de expiración
    private val _expiryDate = MutableLiveData<Timestamp>()
    val expiryDate: LiveData<Timestamp> get() = _expiryDate

    // Métodos para actualizar los atributos del ítem actual
    fun updateId(id: String) {
        _id.value = id
    }

    // Método para actualizar el número de tarjeta
    fun updateCardNumber(cardNumber: String) {
        _cardNumber.value = cardNumber
    }

    // Método para actualizar el estado
    fun updateState(state: String) {
        _state.value = state
    }

    // Método para actualizar la fecha de emisión
    fun updateIssueDate(issueDate: Timestamp) {
        _issueDate.value = issueDate
    }

    // Método para actualizar la fecha de expiración
    fun updateExpiryDate(expiryDate: Timestamp) {
        _expiryDate.value = expiryDate
    }

    // Método para guardar (agregar o actualizar) la experiencia laboral actual
    fun saveAccessCard(teacherId: String) {
        val currentItem = AccessCard(
            id = _id.value ?: "",
            cardNumber = _cardNumber.value ?: "",
            state = _state.value ?: "Activo",
            issueDate = _issueDate.value ?: Timestamp.now(),
            expiryDate = _expiryDate.value ?: Timestamp.now()
        )
        viewModelScope.launch {
            if (currentItem.id.isNotEmpty()) {
                _saveResult.value = repository.updateAccessCard(teacherId, currentItem)
            } else {
                _saveResult.value = repository.addAccessCard(teacherId, currentItem)
            }
        }
    }

    // Método para eliminar una card
    fun removeAccessCard(teacherId: String, id: String) {
        viewModelScope.launch {
            _removeResult.value = repository.removeAccessCard(teacherId, id)
        }
    }

    // Método para cargar cards de un trabajador
    fun fetchAccessCards(teacherId: String) {
        viewModelScope.launch {
            _accessCards.value = repository.getAccessCards(teacherId)
        }
    }

    fun fecthAllAccessCards() {
        viewModelScope.launch {
            _accessCards.value = repository.getAllAccessCards()
        }
    }

    fun fecthAllTeacherAccessCards() {
        viewModelScope.launch {
            _teacherAccessCards.value = repository.getAllTeacherAccessCards()
        }
    }

    fun resetResult() {
        _saveResult.value = null
        _removeResult.value = null
    }

    // Método para limpiar el ítem actual
    fun clearCurrentItem() {
        _id.value = ""
        _cardNumber.value = ""
        _state.value = ""
        _issueDate.value = Timestamp.now()
        _expiryDate.value = Timestamp.now()
    }

}
