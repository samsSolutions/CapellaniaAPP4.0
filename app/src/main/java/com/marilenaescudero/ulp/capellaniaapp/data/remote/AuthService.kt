package com.marilenaescudero.ulp.capellaniaapp.data.remote

import com.marilenaescudero.ulp.capellaniaapp.login.LoginRequest
import com.marilenaescudero.ulp.capellaniaapp.login.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}


