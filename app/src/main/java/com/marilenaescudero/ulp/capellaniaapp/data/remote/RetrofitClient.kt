package com.marilenaescudero.ulp.capellaniaapp.data.remote

import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://192.168.0.10:5000/api/"

    private val gson = GsonBuilder().setLenient().create()

    fun getClient(prefs: SharedPreferences): Retrofit {

        val client = OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor(prefs))
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    fun authService(prefs: SharedPreferences): AuthService =
        getClient(prefs).create(AuthService::class.java)

    fun familiaService(prefs: SharedPreferences): FamiliaApiService =
        getClient(prefs).create(FamiliaApiService::class.java)

    fun oracionesService(prefs: SharedPreferences): OracionesApiService =
        getClient(prefs).create(OracionesApiService::class.java)

    fun eventosService(prefs: SharedPreferences): EventosApiService =
        getClient(prefs).create(EventosApiService::class.java)

    fun palabraDiaService(prefs: SharedPreferences): PalabraDiaApiService =
        getClient(prefs).create(PalabraDiaApiService::class.java)

}

