package com.marilenaescudero.ulp.capellaniaapp.data.repository

import android.content.Context
import android.os.Environment
import com.marilenaescudero.ulp.capellaniaapp.data.remote.RetrofitClient
import com.marilenaescudero.ulp.capellaniaapp.modelo.evento.ActualizarEventoDto
import com.marilenaescudero.ulp.capellaniaapp.modelo.evento.CrearEventoDto
import com.marilenaescudero.ulp.capellaniaapp.modelo.evento.EventoDetalleResponse
import com.marilenaescudero.ulp.capellaniaapp.modelo.evento.EventoItem
import com.marilenaescudero.ulp.capellaniaapp.modelo.evento.InscripcionDto
import com.marilenaescudero.ulp.capellaniaapp.modelo.evento.InscripcionEventoItem
import com.marilenaescudero.ulp.capellaniaapp.modelo.evento.InscriptoItem
import java.io.File


class EventoRepository(private val context: Context) {

    private val prefs = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
    private val api = RetrofitClient.eventosService(prefs)

    // LISTAR EVENTOS DISPONIBLES
    suspend fun listarDisponibles(): Result<List<EventoItem>> = try {
        Result.success(api.listarDisponibles())
    } catch (e: Exception) {
        Result.failure(e)
    }

    // DETALLE DE EVENTO
    suspend fun obtenerDetalle(idEvento: Int): Result<EventoDetalleResponse> = try {
        Result.success(api.obtenerDetalle(idEvento))
    } catch (e: Exception) {
        Result.failure(e)
    }

    // MIS INSCRIPCIONES
    suspend fun misInscripciones(): Result<List<InscripcionEventoItem>> = try {
        Result.success(api.misInscripciones())
    } catch (e: Exception) {
        Result.failure(e)
    }

    // CREAR EVENTO
    suspend fun crearEvento(dto: CrearEventoDto): Result<Unit> = try {
        api.crearEvento(dto)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    // ACTUALIZAR EVENTO
    suspend fun actualizarEvento(id: Int, dto: ActualizarEventoDto): Result<Unit> = try {
        api.actualizarEvento(id, dto)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    // BAJA
    suspend fun bajaEvento(id: Int): Result<Unit> = try {
        api.bajaEvento(id)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    //inscribirse
    suspend fun inscribirse(idEvento: Int, documento: Int): Result<Unit> {
        return try {
            val dto = InscripcionDto(idEvento, documento)
            api.inscribirse(dto)
            Result.success(Unit)

        } catch (e: retrofit2.HttpException) {
            when (e.code()) {
                404 -> Result.failure(Exception("Usted ya está inscripto a este evento"))
                400 -> Result.failure(Exception("Datos inválidos"))
                else -> Result.failure(Exception("Error inesperado (${e.code()})"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun guardarPdfLocal(bytes: ByteArray?, idInscripcion: Int): String {
        val ruta = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "ticket_$idInscripcion.pdf"
        )
        bytes?.let { ruta.writeBytes(it) }
        return ruta.absolutePath
    }

  //ticket
    suspend fun obtenerTicket(idInscripcion: Int): Result<String> {
        return try {
            val response = api.obtenerTicket(idInscripcion)
            if (response.isSuccessful) {
                val bytes = response.body()?.bytes()
                val ruta = guardarPdfLocal(bytes, idInscripcion)
                Result.success(ruta)
            } else {
                Result.failure(Exception("Error al obtener el ticket (${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun verInscriptos(idEvento: Int): Result<List<InscriptoItem>> {
        return try {
            val response = api.verInscriptos(idEvento)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error al obtener inscriptos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun misEventosCreados(): Result<List<EventoItem>> {
        return try {
            val response = api.misEventosCreados()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error al obtener eventos creados"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }





}
