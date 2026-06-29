package com.marilenaescudero.ulp.capellaniaapp.ui.familia

import android.content.Context
import androidx.lifecycle.*
import com.marilenaescudero.ulp.capellaniaapp.data.repository.FamiliaRepository
import com.marilenaescudero.ulp.capellaniaapp.modelo.familia.FamiliaItem
import kotlinx.coroutines.launch

class FamiliaViewModel(private val context: Context) : ViewModel() {

    private val repo = FamiliaRepository(context)
    private var paginaActual = 1
    private val tamañoPagina = 3
    private var ultimaPagina = false
    private var cargando = false

    private val _lista = MutableLiveData<MutableList<FamiliaItem>>(mutableListOf())
    val lista: LiveData<MutableList<FamiliaItem>> get() = _lista

    private val _item = MutableLiveData<FamiliaItem>()
    val item: LiveData<FamiliaItem> get() = _item

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error


    fun cargarCategoria(categoria: String) {
        if (cargando || ultimaPagina) return

        viewModelScope.launch {
            try {
                cargando = true
                _loading.value = true

                val respuesta = repo.listarPorCategoriaPaginado(
                    categoria,
                    paginaActual,
                    tamañoPagina
                )

                if (respuesta.registros.isEmpty()) {
                    ultimaPagina = true
                    return@launch
                }

                val listaActual = _lista.value ?: mutableListOf()
                listaActual.addAll(respuesta.registros)
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

    fun crear(item: FamiliaItem, onSuccess: (FamiliaItem) -> Unit) {
        viewModelScope.launch {
            try {
                val creado = repo.crear(item)
                onSuccess(creado)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun actualizar(id: Int, item: FamiliaItem, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                if (repo.actualizar(id, item)) {
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
