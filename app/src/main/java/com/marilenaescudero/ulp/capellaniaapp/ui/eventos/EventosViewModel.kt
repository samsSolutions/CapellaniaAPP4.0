package com.marilenaescudero.ulp.capellaniaapp.ui.eventos

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marilenaescudero.ulp.capellaniaapp.data.repository.EventoRepository
import com.marilenaescudero.ulp.capellaniaapp.modelo.evento.ActualizarEventoDto
import com.marilenaescudero.ulp.capellaniaapp.modelo.evento.CrearEventoDto
import com.marilenaescudero.ulp.capellaniaapp.modelo.evento.EventoDetalle
import com.marilenaescudero.ulp.capellaniaapp.modelo.evento.EventoItem
import com.marilenaescudero.ulp.capellaniaapp.modelo.evento.InscripcionEventoItem
import com.marilenaescudero.ulp.capellaniaapp.modelo.evento.InscriptoItem
import kotlinx.coroutines.launch

class EventosViewModel(private val context: Context) : ViewModel() {

    private val repo = EventoRepository(context)

    private val _lista = MutableLiveData<List<EventoItem>>()
    val lista: LiveData<List<EventoItem>> get() = _lista

    private val _detalle = MutableLiveData<EventoDetalle>()
    val detalle: LiveData<EventoDetalle> get() = _detalle

    private val _cuposRestantes = MutableLiveData<Int?>()
    val cuposRestantes: LiveData<Int?> get() = _cuposRestantes

    private val _inscripciones = MutableLiveData<List<InscripcionEventoItem>>()
    val inscripciones: LiveData<List<InscripcionEventoItem>> get() = _inscripciones

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _eventosCreados = MutableLiveData<List<EventoItem>>()
    val eventosCreados: LiveData<List<EventoItem>> get() = _eventosCreados

    private val _inscriptosEvento = MutableLiveData<List<InscriptoItem>>()
    val inscriptosEvento: LiveData<List<InscriptoItem>> get() = _inscriptosEvento


    // LISTAR EVENTOS DISPONIBLES
    fun cargarEventosDisponibles() {
        viewModelScope.launch {
            _loading.value = true
            repo.listarDisponibles().fold(
                onSuccess = { _lista.value = it },
                onFailure = { _error.value = it.message }
            )
            _loading.value = false
        }
    }

    // DETALLE DE EVENTO
    fun cargarDetalle(idEvento: Int) {
        viewModelScope.launch {
            _loading.value = true
            repo.obtenerDetalle(idEvento).fold(
                onSuccess = { resp ->
                    _detalle.value = resp.detalleEvento
                    _cuposRestantes.value = resp.cuposRestantes
                },
                onFailure = { _error.value = it.message }
            )
            _loading.value = false
        }
    }

    // MIS INSCRIPCIONES
    fun cargarMisInscripciones() {
        viewModelScope.launch {
            _loading.value = true
            repo.misInscripciones().fold(
                onSuccess = { _inscripciones.value = it },
                onFailure = { _error.value = it.message }
            )
            _loading.value = false
        }
    }

    // CREAR EVENTO
    fun crearEvento(dto: CrearEventoDto, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            repo.crearEvento(dto).fold(
                onSuccess = { onSuccess() },
                onFailure = { _error.value = it.message }
            )
            _loading.value = false
        }
    }

    // ACTUALIZAR EVENTO
    fun actualizarEvento(id: Int, dto: ActualizarEventoDto, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            repo.actualizarEvento(id, dto).fold(
                onSuccess = { onSuccess() },
                onFailure = { _error.value = it.message }
            )
            _loading.value = false
        }
    }

    // ELIMINAR EVENTO
    fun eliminarEvento(id: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            repo.bajaEvento(id).fold(
                onSuccess = { onSuccess() },
                onFailure = { _error.value = it.message }
            )
            _loading.value = false
        }
    }

    //  verifica si esta la inscripcion
    fun obtenerInscripcion(idEvento: Int, callback: (InscripcionEventoItem?) -> Unit) {
        viewModelScope.launch {
            val lista = repo.misInscripciones()
            if (lista.isSuccess) {
                val inscripcion = lista.getOrNull()?.find { it.idEvento == idEvento }
                callback(inscripcion)
            } else {
                callback(null)
            }
        }
    }


    //  INSCRIBIRSE
    fun inscribirse(idEvento: Int, documento: Int, callback: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            repo.inscribirse(idEvento, documento).fold(
                onSuccess = { callback(true, "Inscripción realizada") },
                onFailure = { callback(false, it.message ?: "Error al inscribirse") }
            )
        }
    }


    fun obtenerTicket(idInscripcion: Int, callback: (String?) -> Unit) {
        viewModelScope.launch {
            val resultado = repo.obtenerTicket(idInscripcion)
            if (resultado.isSuccess) {
                callback(resultado.getOrNull())
            } else {
                callback(null)
            }
        }
    }
    fun cargarEventosCreados() {
        viewModelScope.launch {
            _loading.value = true
            repo.misEventosCreados().fold(
                { _eventosCreados.postValue(it) },
                { _error.postValue(it.message) }
            )
            _loading.value = false
        }
    }

    fun cargarInscriptos(idEvento: Int) {
        viewModelScope.launch {
            _loading.value = true
            repo.verInscriptos(idEvento).fold(
                { _inscriptosEvento.postValue(it) },
                { _error.postValue(it.message) }
            )
            _loading.value = false
        }
    }



}

