package com.marilenaescudero.ulp.capellaniaapp.data.remote

import com.marilenaescudero.ulp.capellaniaapp.modelo.familia.FamiliaItem
import com.marilenaescudero.ulp.capellaniaapp.modelo.oraciones.PaginacionRespuesta
import retrofit2.Response
import retrofit2.http.*

interface FamiliaApiService {

    @GET("FamiliaContenido/{categoria}")
    suspend fun obtenerPorCategoria(
        @Path("categoria") categoria: String
    ): List<FamiliaItem>

    @GET("FamiliaContenido/detalle/{id}")
    suspend fun obtenerPorId(
        @Path("id") id: Int
    ): FamiliaItem

    @POST("FamiliaContenido")
    suspend fun crearContenido(
        @Body item: FamiliaItem
    ): FamiliaItem

    @PUT("FamiliaContenido/{id}")
    suspend fun actualizarContenido(
        @Path("id") id: Int,
        @Body item: FamiliaItem
    ): Response<Unit>

    @DELETE("FamiliaContenido/{id}")
    suspend fun eliminarContenido(
        @Path("id") id: Int
    ): Response<Unit>
    @GET("FamiliaContenido/paginado/categoria/{categoria}")
    suspend fun listarPorCategoriaPaginado(
        @Path("categoria") categoria: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): PaginacionRespuesta<FamiliaItem>
}
