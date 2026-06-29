package com.marilenaescudero.ulp.capellaniaapp.data.remote

import com.marilenaescudero.ulp.capellaniaapp.modelo.evento.ActualizarEventoDto
import com.marilenaescudero.ulp.capellaniaapp.modelo.evento.CrearEventoDto
import com.marilenaescudero.ulp.capellaniaapp.modelo.evento.EventoDetalleResponse
import com.marilenaescudero.ulp.capellaniaapp.modelo.evento.EventoItem
import com.marilenaescudero.ulp.capellaniaapp.modelo.evento.InscripcionDto
import com.marilenaescudero.ulp.capellaniaapp.modelo.evento.InscripcionEventoItem
import com.marilenaescudero.ulp.capellaniaapp.modelo.evento.InscriptoItem
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface EventosApiService {

    // CREAR EVENTO
    @POST("Evento/crear")
    suspend fun crearEvento(@Body dto: CrearEventoDto)

    // ACTUALIZAR EVENTO
    @PUT("Evento/actualizar/{id}")
    suspend fun actualizarEvento(
        @Path("id") id: Int,
        @Body dto: ActualizarEventoDto)

    // DETALLE DE EVENTO
    @GET("Evento/{idEvento}")
    suspend fun obtenerDetalle(
        @Path("idEvento") idEvento: Int
    ): EventoDetalleResponse

    // BAJA
    @PUT("Evento/baja/{id}")
    suspend fun bajaEvento(@Path("id") id: Int)


    // LISTAR EVENTOS DISPONIBLES
    @GET("Evento/disponibles")
    suspend fun listarDisponibles(): List<EventoItem>


    // MIS INSCRIPCIONES
    @GET("Evento/mis-inscripciones")
    suspend fun misInscripciones(): List<InscripcionEventoItem>


    // inscripciones
    @POST("Evento/inscribirme")
    suspend fun inscribirse(@Body dto: InscripcionDto)


    // ticket
    @GET("Evento/ticket/{idInscripcion}")
    suspend fun obtenerTicket(
        @Path("idInscripcion") idInscripcion: Int
    ): Response<ResponseBody>

    //mis eventos
    @GET("Evento/mis-creados")
    suspend fun misEventosCreados(): Response<List<EventoItem>>

    //ver inscriptos
    @GET("Evento/{idEvento}/inscriptos")
    suspend fun verInscriptos(
        @Path("idEvento") idEvento: Int
    ): Response<List<InscriptoItem>>


}
