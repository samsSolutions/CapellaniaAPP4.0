package com.marilenaescudero.ulp.capellaniaapp.modelo.oraciones

data class OracionDto(
    val idOracion: Int,
    val idCategoria: Int,
    val categoriaNombre: String,
    val idCapellan: Int,
    val capellanNombre: String,
    val fecha: String,
    val fechaActualizacion: String?,
    val descripcion: String,
    val estado: Boolean
)