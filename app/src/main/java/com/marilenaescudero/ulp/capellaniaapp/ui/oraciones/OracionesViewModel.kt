package com.marilenaescudero.ulp.capellaniaapp.ui.oraciones

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marilenaescudero.ulp.capellaniaapp.data.remote.RetrofitClient
import com.marilenaescudero.ulp.capellaniaapp.data.repository.OracionesRepository
import com.marilenaescudero.ulp.capellaniaapp.modelo.oraciones.CrearOracionDto
import com.marilenaescudero.ulp.capellaniaapp.modelo.oraciones.EditarOracionDto
import com.marilenaescudero.ulp.capellaniaapp.modelo.oraciones.OracionDto
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class OracionesViewModel(private val context: Context) : ViewModel() {

    private val repo = OracionesRepository(context)

    // paginacion
    private var paginaActual = 1
    private val tamañoPagina = 3
    private var ultimaPagina = false
    private var cargando = false

    private val _lista = MutableLiveData<MutableList<OracionDto>>(mutableListOf())
    val lista: LiveData<MutableList<OracionDto>> get() = _lista

    private val _item = MutableLiveData<OracionDto>()
    val item: LiveData<OracionDto> get() = _item

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error


    fun cargarCategoria(idCategoria: Int) {
        if (cargando || ultimaPagina) return

        viewModelScope.launch {
            try {
                cargando = true
                _loading.value = true

                val respuesta = repo.listarPorCategoriaPaginado(
                    idCategoria,
                    paginaActual,
                    tamañoPagina
                )

                if (respuesta.registros.isEmpty()) {
                    ultimaPagina = true
                    return@launch
                }

                val formato = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                val ordenados = respuesta.registros.sortedByDescending {
                    LocalDateTime.parse(it.fecha, formato)
                }

                val listaActual = _lista.value ?: mutableListOf()
                listaActual.addAll(ordenados)
                _lista.value = listaActual
                paginaActual++

                if (paginaActual > respuesta.totalPaginas) {
                    ultimaPagina = true
                }

            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                cargando = false
                _loading.value = false
            }
        }
    }

    fun reiniciarPaginacion() {
        paginaActual = 1
        ultimaPagina = false
        _lista.value = mutableListOf()
    }

    fun cargarItem(id: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _item.value = repo.obtenerPorId(id)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun crear(dto: CrearOracionDto, onSuccess: (OracionDto) -> Unit) {
        viewModelScope.launch {
            try {
                val creada = repo.crear(dto)
                onSuccess(creada)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun editar(id: Int, dto: EditarOracionDto, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                if (repo.editar(id, dto)) {
                    onSuccess()
                } else {
                    _error.value = "Error al actualizar"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun eliminar(id: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                if (repo.eliminar(id)) {
                    onSuccess()
                } else {
                    _error.value = "Error al eliminar"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}

