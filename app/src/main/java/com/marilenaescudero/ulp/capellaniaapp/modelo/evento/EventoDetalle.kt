package com.marilenaescudero.ulp.capellaniaapp.modelo.evento

data class EventoDetalle(
    val idEvento: Int,
    val fecha: String,
    val tipo: String,
    val nombre: String,
    val direccion: String,
    val publicoObjetivo: String,
    val descripcion: String,
    val idCapellan: Int,
    val imagen: String,
    val propio: Boolean,
    val cupo: String?,
    val fechaCreacion: String,
    val fechaActualizacion: String?,
    val estado: Boolean
)

data class EventoDetalleResponse(
    val detalleEvento: EventoDetalle,
    val cuposRestantes: Int?
)
