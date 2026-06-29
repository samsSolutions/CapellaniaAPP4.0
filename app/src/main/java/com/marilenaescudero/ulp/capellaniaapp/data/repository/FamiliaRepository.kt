package com.marilenaescudero.ulp.capellaniaapp.data.repository

import android.content.Context
import com.marilenaescudero.ulp.capellaniaapp.data.remote.RetrofitClient
import com.marilenaescudero.ulp.capellaniaapp.modelo.familia.FamiliaItem
import com.marilenaescudero.ulp.capellaniaapp.modelo.oraciones.PaginacionRespuesta

class FamiliaRepository(private val context: Context) {

    private val prefs = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
    private val api = RetrofitClient.familiaService(prefs)

    suspend fun obtenerPorCategoria(categoria: String): List<FamiliaItem> {
        return api.obtenerPorCategoria(categoria)
    }

    suspend fun obtenerPorId(id: Int): FamiliaItem {
        return api.obtenerPorId(id)
    }

    suspend fun crear(item: FamiliaItem): FamiliaItem {
        return api.crearContenido(item)
    }

    suspend fun actualizar(id: Int, item: FamiliaItem): Boolean {
        val response = api.actualizarContenido(id, item)
        return response.isSuccessful
    }

    suspend fun eliminar(id: Int): Boolean {
        val response = api.eliminarContenido(id)
        return response.isSuccessful
    }
    suspend fun listarPorCategoriaPaginado(
        categoria: String,
        page: Int,
        pageSize: Int
    ): PaginacionRespuesta<FamiliaItem> {
        return api.listarPorCategoriaPaginado(categoria, page, pageSize)
    }
}
