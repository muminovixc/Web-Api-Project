package com.np.apiaplikacija.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.Log
import com.google.gson.JsonElement
import com.np.apiaplikacija.data.api.RetrofitClient
import com.np.apiaplikacija.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DatasetDetailViewModel : ViewModel() {
    private val _json = MutableStateFlow<JsonElement?>(null)
    val json: StateFlow<JsonElement?> = _json

    fun loadData(url: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.dynamicApi.getDynamicJson(
                    url = url,
                    token = "Bearer ${BuildConfig.API_KEY}",
                    body = mapOf("languageId" to "1")
                )
                _json.value = response
            } catch (e: Exception) {

            }
        }
    }
}