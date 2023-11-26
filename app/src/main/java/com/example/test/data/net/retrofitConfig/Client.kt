package com.example.test.data.net.retrofitConfig

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Client {
    private var retrofit: Retrofit? = null

    private fun client(url: String): Retrofit?{
        val client = OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).readTimeout(35, TimeUnit.SECONDS).build()
        if(retrofit == null){
            retrofit = Retrofit.Builder().baseUrl(url).client(client).addConverterFactory(GsonConverterFactory.create()).build()
        }
        return retrofit
    }

    fun api(url: String): RetrofitClient {
        return client(url)!!.create(RetrofitClient::class.java)
    }

}