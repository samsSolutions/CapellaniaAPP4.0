package com.marilenaescudero.ulp.capellaniaapp.modelo.oraciones

data class EditarOracionDto(
    val idCategoria: Int? = null,
    val descripcion: String? = null,
    val estado: Boolean? = null
)