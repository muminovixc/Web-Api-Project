package com.np.apiaplikacija.repository

import com.np.apiaplikacija.data.api.ApiClient

class ApiRepository {
    suspend fun fetchDatasets(token: String) =
        ApiClient.retrofit.getDatasets("Bearer $token")
}
