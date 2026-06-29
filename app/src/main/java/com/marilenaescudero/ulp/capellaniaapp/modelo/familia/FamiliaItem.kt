package com.marilenaescudero.ulp.capellaniaapp.modelo.familia

data class FamiliaItem(
    val id: Int,
    val titulo: String,
    val reflexion: String,
    val versiculo: String,
    val categoria: String,
    val usuarioId: Int? = null
)
