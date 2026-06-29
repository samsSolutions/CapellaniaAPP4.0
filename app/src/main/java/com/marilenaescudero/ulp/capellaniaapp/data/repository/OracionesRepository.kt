package com.marilenaescudero.ulp.capellaniaapp.data.repository

import android.content.Context
import com.marilenaescudero.ulp.capellaniaapp.data.remote.OracionesApiService
import com.marilenaescudero.ulp.capellaniaapp.data.remote.RetrofitClient
import com.marilenaescudero.ulp.capellaniaapp.modelo.oraciones.CrearOracionDto
import com.marilenaescudero.ulp.capellaniaapp.modelo.oraciones.EditarOracionDto
import com.marilenaescudero.ulp.capellaniaapp.modelo.oraciones.OracionDto
import com.marilenaescudero.ulp.capellaniaapp.modelo.oraciones.PaginacionRespuesta

class OracionesRepository(private val context: Context) {

    private val prefs = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
    private val api = RetrofitClient.oracionesService(prefs)

    // Ya NO se usa getToken() porque el interceptor agrega el token automáticamente

    suspend fun listarPorCategoriaPaginado(idCategoria: Int, page: Int, pageSize: Int): PaginacionRespuesta<OracionDto> {
        return api.listarPorCategoriaPaginado(idCategoria, page, pageSize)
    }


    suspend fun obtenerPorId(id: Int): OracionDto {
        return api.obtenerPorId(id)
    }

    suspend fun crear(dto: CrearOracionDto): OracionDto {
        return api.crear(dto)
    }

    suspend fun editar(id: Int, dto: EditarOracionDto): Boolean {
        return api.editar(id, dto).isSuccessful
    }

    suspend fun eliminar(id: Int): Boolean {
        return api.eliminar(id).isSuccessful
    }
}



