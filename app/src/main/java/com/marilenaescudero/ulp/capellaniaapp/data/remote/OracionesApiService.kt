package com.marilenaescudero.ulp.capellaniaapp.data.remote

import com.marilenaescudero.ulp.capellaniaapp.modelo.oraciones.CrearOracionDto
import com.marilenaescudero.ulp.capellaniaapp.modelo.oraciones.EditarOracionDto
import com.marilenaescudero.ulp.capellaniaapp.modelo.oraciones.OracionDto
import com.marilenaescudero.ulp.capellaniaapp.modelo.oraciones.PaginacionRespuesta
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface OracionesApiService {

    @GET("Oracion/categoria/{idCategoria}")
    suspend fun listarPorCategoria(
        @Path("idCategoria") idCategoria: Int
    ): List<OracionDto>

    @GET("Oracion/{id}")
    suspend fun obtenerPorId(
        @Path("id") id: Int
    ): OracionDto

    @POST("Oracion")
    suspend fun crear(
        @Body dto: CrearOracionDto
    ): OracionDto

    @PUT("Oracion/{id}")
    suspend fun editar(
        @Path("id") id: Int,
        @Body dto: EditarOracionDto
    ): Response<Unit>

    @DELETE("Oracion/{id}")
    suspend fun eliminar(
        @Path("id") id: Int
    ): Response<Unit>

    @GET("oracion/paginado/categoria/{idCategoria}")
    suspend fun listarPorCategoriaPaginado(
        @Path("idCategoria") idCategoria: Int,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): PaginacionRespuesta<OracionDto>


}
