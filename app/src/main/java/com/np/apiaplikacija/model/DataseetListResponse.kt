package com.np.apiaplikacija.model

data class DatasetListResponse(
    val success: Boolean,
    val result: List<DatasetItem>
)

data class DatasetItem(
    val id: Int,
    val name: String,
    val apiUrl: String
)
