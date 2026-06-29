package com.marilenaescudero.ulp.capellaniaapp.data.remote

import com.marilenaescudero.ulp.capellaniaapp.modelo.palabraDia.CrearPalabraDiaDto
import com.marilenaescudero.ulp.capellaniaapp.modelo.palabraDia.PalabraDiaDto
import com.marilenaescudero.ulp.capellaniaapp.modelo.palabraDia.PalabraDiaHomeDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PalabraDiaApiService {

    @GET("PalabraDia/hoy")
    suspend fun obtenerHoy(): PalabraDiaDto

    @GET("PalabraDia/{id}")
    suspend fun obtenerPorId(@Path("id") id: Int): PalabraDiaDto

    @GET("PalabraDia")
    suspend fun listar(): List<PalabraDiaDto>

    @GET("PalabraDia/capellan/{idCapellan}")
    suspend fun listarPorCapellan(@Path("idCapellan") idCapellan: Int): List<PalabraDiaDto>

    @GET("PalabraDia/inactivas")
    suspend fun listarInactivas(): List<PalabraDiaDto>

    @GET("PalabraDia/fecha/{fecha}")
    suspend fun obtenerPorFecha(@Path("fecha") fecha: String): PalabraDiaDto

    @POST("PalabraDia")
    suspend fun crearPalabraDia(@Body dto: CrearPalabraDiaDto)

   // @PUT("PalabraDia/{id}")
    //suspend fun editarPalabraDia(@Path("id") id: Int, @Body dto: EditarPalabraDiaDto)

    @DELETE("PalabraDia/{id}")
    suspend fun eliminarPalabraDia(@Path("id") id: Int)

    @GET("PalabraDia/fecha-simple/{fecha}")
    suspend fun obtenerPorFechaSimple(@Path("fecha") fecha: String): PalabraDiaHomeDto




}


