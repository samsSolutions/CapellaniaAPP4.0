package com.marilenaescudero.ulp.capellaniaapp.modelo.evento

data class CrearEventoDto(
    val fecha: String,
    val tipo: String,
    val nombre: String,
    val direccion: String,
    val publicoObjetivo: String,
    val descripcion: String,
    val imagen: String,
    val propio: Boolean,
    val cupo: Int?
)