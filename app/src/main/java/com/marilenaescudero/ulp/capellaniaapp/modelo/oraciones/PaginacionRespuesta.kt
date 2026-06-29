package com.marilenaescudero.ulp.capellaniaapp.modelo.oraciones

data class PaginacionRespuesta<T>(
    val paginaActual: Int,
    val pageSize: Int,
    val totalRegistros: Int,
    val totalPaginas: Int,
    val registros: List<T>
)
