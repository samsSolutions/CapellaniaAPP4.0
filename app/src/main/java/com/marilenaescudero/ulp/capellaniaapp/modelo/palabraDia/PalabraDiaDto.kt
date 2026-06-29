package com.marilenaescudero.ulp.capellaniaapp.modelo.palabraDia

data class PalabraDiaDto(
    val idPalabra: Int,
    val fecha: String,
    val fechaPublicacion: String?,
    val fechaActualizacion: String?,
    val versiculo: String?,
    val reflexion: String?,
    val multimedia: String?,
    val idCapellan: Int,
    val estado: Boolean
)
