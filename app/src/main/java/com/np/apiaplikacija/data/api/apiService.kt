package com.np.apiaplikacija.data.api

import com.google.gson.JsonElement
import com.np.apiaplikacija.model.DatasetListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiService {
    @POST("dataset/list")
    suspend fun getDatasets(
        @Header("Authorization") token: String,
        @Body body: Map<String, Int> = mapOf("languageId" to 2)
    ): DatasetListResponse
}
interface DynamicApiService {
    @POST
    suspend fun getDynamicJson(
        @Url url: String,
        @Header("Authorization") token: String,
        @Body body: Map<String, String>
    ): JsonElement
}

