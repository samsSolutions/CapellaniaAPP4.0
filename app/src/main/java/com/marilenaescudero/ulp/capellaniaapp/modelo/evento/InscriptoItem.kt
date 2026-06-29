package com.marilenaescudero.ulp.capellaniaapp.modelo.evento

data class InscriptoItem(
    val idUsuario: Int,
    val nombre: String,
    val apellido: String,
    val correo: String,
    val documento: Int,
    val fechaCreacion: String
)
