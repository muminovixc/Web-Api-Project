package com.np.apiaplikacija.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonElement
import com.np.apiaplikacija.data.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomePageViewModel : ViewModel() {

    private val _filteredResults = MutableStateFlow<List<String>>(emptyList())
    val filteredResults: StateFlow<List<String>> = _filteredResults

    fun loadFilteredData(
        type: String,
        entity: String,
        canton: String,
        municipality: String,
        year: String,
        token: String
    ) {
        viewModelScope.launch {
            try {
                val url = when (type) {
                    "Novorođeni" -> "https://odp.iddeea.gov.ba:8096/api/NewbornByRequestDate/List"
                    "Umrli" -> "https://odp.iddeea.gov.ba:8096/api/DeceasedByDate/List"
                    else -> ""
                }

                if (url.isNotEmpty()) {
                    val response: JsonElement = RetrofitClient.dynamicApi.getDynamicJson(
                        url = url,
                        token = "Bearer $token",
                        body = mapOf(
                            "entity" to entity,
                            "canton" to canton,
                            "municipality" to municipality,
                            "year" to year
                        )
                    )


                    val results = mutableListOf<String>()

                    if (response.isJsonObject && response.asJsonObject["result"]?.isJsonArray == true) {
                        response.asJsonObject["result"].asJsonArray.forEach { obj ->
                            val item = obj.asJsonObject.entrySet().joinToString(" | ") {
                                "${it.key}: ${it.value.asString}"
                            }
                            results.add(item)
                        }
                    } else {
                        results.add("Nepodržan odgovor: nije JSON niz u 'result'.")
                    }


                    _filteredResults.value = results
                } else {
                    _filteredResults.value = listOf("Nepoznata ruta za tip: $type")
                }
            } catch (e: Exception) {
                _filteredResults.value = listOf("Greška: ${e.message}")
            }
        }
    }
}
