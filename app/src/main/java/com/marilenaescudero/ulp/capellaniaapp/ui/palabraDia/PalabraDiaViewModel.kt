package com.marilenaescudero.ulp.capellaniaapp.ui.palabraDia

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.marilenaescudero.ulp.capellaniaapp.data.repository.PalabraDiaRepository
import com.marilenaescudero.ulp.capellaniaapp.modelo.palabraDia.CrearPalabraDiaDto
import com.marilenaescudero.ulp.capellaniaapp.modelo.palabraDia.PalabraDiaDto
import com.marilenaescudero.ulp.capellaniaapp.modelo.palabraDia.PalabraDiaHomeDto
import kotlinx.coroutines.launch
import java.time.LocalDate


class PalabraDiaViewModel(private val context: Context) : ViewModel() {

    private val repo = PalabraDiaRepository(context)

    private val _palabra = MutableLiveData<PalabraDiaDto?>()
    val palabra: LiveData<PalabraDiaDto?> get() = _palabra

    private val _palabras = MutableLiveData<List<PalabraDiaDto>>()
    val palabras: LiveData<List<PalabraDiaDto>> get() = _palabras

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _mensaje = MutableLiveData<String?>()
    val mensaje: LiveData<String?> get() = _mensaje

    val hoy = LocalDate.now().toString()

    fun cargarPalabraHoy() {
        obtenerPorFecha(hoy)
    }

    fun crearPalabraDia(dto: CrearPalabraDiaDto) {
        viewModelScope.launch {
            _loading.value = true

            repo.crear(dto).fold(
                onSuccess = {
                    _mensaje.value = "Palabra creada correctamente"
                    cargarPalabraHoy()
                },
                onFailure = {
                    _error.value = it.message
                }
            )

            _loading.value = false
        }
    }

    fun obtenerPorFecha(fecha: String) {
        viewModelScope.launch {
            _loading.value = true

            repo.obtenerPorFecha(fecha).fold(
                onSuccess = { _palabra.value = it },
                onFailure = { _error.value = it.message }
            )

            _loading.value = false
        }
    }

    fun listarPorCapellan(idCapellan: Int) {
        viewModelScope.launch {
            _loading.value = true

            repo.listarPorCapellan(idCapellan).fold(
                onSuccess = { _palabras.value = it },
                onFailure = { _error.value = it.message }
            )

            _loading.value = false
        }
    }

    fun listar() {
        viewModelScope.launch {
            _loading.value = true

            repo.listar().fold(
                onSuccess = { _palabras.value = it },
                onFailure = { _error.value = it.message }
            )

            _loading.value = false
        }
    }
}
