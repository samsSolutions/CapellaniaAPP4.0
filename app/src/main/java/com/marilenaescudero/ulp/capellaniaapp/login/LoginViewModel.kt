package com.marilenaescudero.ulp.capellaniaapp.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val context: Context) : ViewModel() {

    private val repository = LoginRepository(context)

    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState> = _state

    fun login(correo: String, password: String) {
        if (correo.isBlank() || password.isBlank()) {
            _state.value = LoginState.Error("Campos vacíos")
            return
        }

        _state.value = LoginState.Loading

        viewModelScope.launch {
            val result = repository.login(correo, password)

            result.fold(
                onSuccess = { data ->
                    _state.value = LoginState.Success(data)
                },
                onFailure = { error ->
                    _state.value = LoginState.Error(error.message ?: "Error desconocido")
                }
            )
        }
    }
}



// Estados posibles del login
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val data: LoginResponse) : LoginState()
    data class Error(val message: String) : LoginState()
}
