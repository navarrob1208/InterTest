package com.example.test.data.net.retrofitConfig

import com.example.test.data.net.responses.Table
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface RetrofitClient {
    @GET("/FtAppAgencias012/apiControllerPruebas/api/SincronizadorDatos/ObtenerEsquema/true")
    fun getData(
        @Header("usuario") user: String
    ):Call<ArrayList<Table>>
}