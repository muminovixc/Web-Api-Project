package com.np.apiaplikacija.data.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    val retrofit: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://odp.iddeea.gov.ba:8096/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

object RetrofitClient {
    private val gson = GsonBuilder().setLenient().create()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://odp.iddeea.gov.ba:8096/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val dynamicApi: DynamicApiService by lazy {
        retrofit.create(DynamicApiService::class.java)
    }
}
