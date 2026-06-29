package com.marilenaescudero.ulp.capellaniaapp.login

data class LoginResponse(
    val token: String,
    val idUsuario: Int,
    val nombre: String,
    val apellido: String,
    val correo: String,
    val idRol: Int,
    val rol: String,
    val avatar: String?,
    val error: String?


)

