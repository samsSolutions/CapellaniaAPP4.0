package com.marilenaescudero.ulp.capellaniaapp.modelo.evento

data class InscripcionEventoItem(
    val idInscripcion: Int,
    val idEvento: Int,
    val fecha: String,
    val tipo: String,
    val nombre: String,
    val direccion: String,
    val publicoObjetivo: String,
    val propio: Boolean,
    val cupo: Int?,
    val estado: Boolean
)
