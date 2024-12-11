package com.example.snapchance.viewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.labaccess.model.data.AccessCard
import com.example.labaccess.model.repo.TeacherRepository
import kotlinx.coroutines.launch

class AccessCardViewModel: ViewModel() {

    // Repositorio para manejar las tarjetas de acceso
    private val repository = TeacherRepository()

    // Lista observable de experiencias laborales
    private val _accessCards = MutableLiveData<List<AccessCard>>()
    val accessCards: LiveData<List<AccessCard>> get() = _accessCards

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
    private val _issueDate = MutableLiveData<String>()
    val issueDate: LiveData<String> get() = _issueDate
    
    // LiveData para la fecha de expiración
    private val _expiryDate = MutableLiveData<String>()
    val expiryDate: LiveData<String> get() = _expiryDate

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
    fun updateIssueDate(issueDate: String) {
        _issueDate.value = issueDate
    }

    // Método para actualizar la fecha de expiración
    fun updateExpiryDate(expiryDate: String) {
        _expiryDate.value = expiryDate
    }

    // Método para guardar (agregar o actualizar) la experiencia laboral actual
    fun saveAccessCard(workerId: String) {
        val currentItem = AccessCard(
            id = _id.value ?: "",
            cardNumber = _cardNumber.value ?: "",
            state = _state.value ?: "",
            issueDate = _issueDate.value ?: "",
            expiryDate = _expiryDate.value ?: ""
        )
        viewModelScope.launch {
            if (currentItem.id.isNotEmpty()) {
                _saveResult.value = repository.updateAccessCard(workerId, currentItem)
            } else {
                _saveResult.value = repository.addAccessCard(workerId, currentItem)
            }
        }
    }

    // Método para eliminar una card
    fun removeAccessCard(teacherId: String, id: String) {
        viewModelScope.launch {
            _removeResult.value = repository.removeAccessCard(teacherId, id)
        }
    }

    // Método para cargar experiencias laborales de un trabajador
    fun fetchAccessCards(teacherId: String) {
        viewModelScope.launch {
            _accessCards.value = repository.getAccessCards(teacherId)
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
        _issueDate.value = ""
        _expiryDate.value = ""
    }

}
