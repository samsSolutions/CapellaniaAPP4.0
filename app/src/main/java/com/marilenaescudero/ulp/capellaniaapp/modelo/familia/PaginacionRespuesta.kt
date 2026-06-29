package com.marilenaescudero.ulp.capellaniaapp.modelo.familia

data class PaginacionRespuesta<T>(
    val paginaActual: Int,
    val totalPaginas: Int,
    val totalRegistros: Int,
    val pageSize: Int,
    val registros: List<T>
)
