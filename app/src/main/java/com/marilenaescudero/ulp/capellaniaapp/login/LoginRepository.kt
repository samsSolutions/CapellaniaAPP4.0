package com.marilenaescudero.ulp.capellaniaapp.login

import android.content.Context
import com.marilenaescudero.ulp.capellaniaapp.data.remote.RetrofitClient

class LoginRepository(private val context: Context) {

    suspend fun login(correo: String, password: String): Result<LoginResponse> {
        return try {
            val prefs = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
            val request = LoginRequest(correo, password)
            val response = RetrofitClient.authService(prefs).login(request)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Credenciales incorrectas"
                Result.failure(Exception(errorMsg))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}



