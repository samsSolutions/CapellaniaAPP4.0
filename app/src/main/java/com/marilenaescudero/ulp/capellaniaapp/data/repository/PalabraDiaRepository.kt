package com.marilenaescudero.ulp.capellaniaapp.data.repository

import android.content.Context
import com.marilenaescudero.ulp.capellaniaapp.data.remote.RetrofitClient
import com.marilenaescudero.ulp.capellaniaapp.modelo.palabraDia.CrearPalabraDiaDto
import com.marilenaescudero.ulp.capellaniaapp.modelo.palabraDia.PalabraDiaDto
import com.marilenaescudero.ulp.capellaniaapp.modelo.palabraDia.PalabraDiaHomeDto

class PalabraDiaRepository(private val context: Context) {

    private val prefs = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
    private val api = RetrofitClient.palabraDiaService(prefs)


    suspend fun obtenerHoy(): Result<PalabraDiaDto> {
        return try {
            val respuesta = api.obtenerHoy()
            Result.success(respuesta)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun obtenerPorId(id: Int): Result<PalabraDiaDto> {
        return try {
            val respuesta = api.obtenerPorId(id)
            Result.success(respuesta)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun listar(): Result<List<PalabraDiaDto>> {
        return try {
            val respuesta = api.listar()
            Result.success(respuesta)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun listarPorCapellan(idCapellan: Int): Result<List<PalabraDiaDto>> {
        return try {
            val respuesta = api.listarPorCapellan(idCapellan)
            Result.success(respuesta)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun listarInactivas(): Result<List<PalabraDiaDto>> {
        return try {
            val respuesta = api.listarInactivas()
            Result.success(respuesta)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    // Para el módulo completo
    suspend fun obtenerPorFecha(fecha: String): Result<PalabraDiaDto> {
        return try {
            val respuesta = api.obtenerPorFecha(fecha)
            Result.success(respuesta)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Para el Home
    suspend fun obtenerPorFechaSimple(fecha: String): Result<PalabraDiaHomeDto> {
        return try {
            val respuesta = api.obtenerPorFechaSimple(fecha)
            Result.success(respuesta)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun crear(dto: CrearPalabraDiaDto): Result<Unit> {
        return try {
            api.crearPalabraDia(dto)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
