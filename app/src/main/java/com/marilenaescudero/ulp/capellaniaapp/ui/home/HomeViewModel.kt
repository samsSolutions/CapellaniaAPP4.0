package com.marilenaescudero.ulp.capellaniaapp.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marilenaescudero.ulp.capellaniaapp.data.repository.PalabraDiaRepository
import com.marilenaescudero.ulp.capellaniaapp.modelo.palabraDia.PalabraDiaHomeDto
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel(private val context: Context) : ViewModel() {

    private val repo = PalabraDiaRepository(context)

    private val _palabra = MutableLiveData<PalabraDiaHomeDto?>()
    val palabra: LiveData<PalabraDiaHomeDto?> get() = _palabra

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun cargarPalabraHoy() {
        val hoy = LocalDate.now().toString()

        viewModelScope.launch {
            repo.obtenerPorFechaSimple(hoy).fold(
                onSuccess = { _palabra.value = it },
                onFailure = { _error.value = it.message }
            )
        }
    }
}
