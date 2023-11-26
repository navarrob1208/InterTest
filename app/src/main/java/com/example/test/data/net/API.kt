package com.example.test.data.net

import androidx.lifecycle.MutableLiveData
import com.example.test.data.net.responses.Table
import com.example.test.data.net.retrofitConfig.Client
import com.example.test.data.net.retrofitConfig.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class API {
    private val baseUrl = "https://apitesting.interrapidisimo.co"
    private val client: RetrofitClient = Client().api(baseUrl)

    fun getData(user: String): MutableLiveData<ArrayList<Table>>{
        val tables: MutableLiveData<ArrayList<Table>> = MutableLiveData()

        client.getData(user).enqueue(object: Callback<ArrayList<Table>>{
            override fun onResponse(call: Call<ArrayList<Table>>, response: Response<ArrayList<Table>>) {
                if(response.isSuccessful){
                    tables.value = response.body()
                }else{
                    tables.value = null
                }
            }

            override fun onFailure(call: Call<ArrayList<Table>>, t: Throwable) {
                tables.value = null
            }
        })
        return tables
    }
}