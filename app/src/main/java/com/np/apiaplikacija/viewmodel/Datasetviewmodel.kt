package com.np.apiaplikacija.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.np.apiaplikacija.data.db.FavoriteEntity
import com.np.apiaplikacija.data.db.FavoritesDatabase
import com.np.apiaplikacija.model.DatasetItem
import com.np.apiaplikacija.repository.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ApiViewModel(application: Application) : AndroidViewModel(application) {
    val userId = getApplication<Application>()
        .getSharedPreferences("user_prefs", Application.MODE_PRIVATE)
        .getInt("user_id", -1)
    private val repository = ApiRepository()
    private val _datasets = MutableStateFlow<List<DatasetItem>>(emptyList())
    val datasets: StateFlow<List<DatasetItem>> = _datasets

    private val favoritesDao = FavoritesDatabase.getInstance(application).favoritesDao()

    private val _favorites = MutableStateFlow<List<FavoriteEntity>>(emptyList())
    val favorites: StateFlow<List<FavoriteEntity>> = _favorites

    fun loadDatasets(token: String) {
        viewModelScope.launch {
            try {
                val response = repository.fetchDatasets(token)
                if (response.success) {
                    _datasets.value = response.result
                }
                Log.d("API_RESPONSE", "Odgovor: $response")
            } catch (e: Exception) {
                Log.e("API_ERROR", "Greška prilikom poziva: ${e.message}", e)
            }
        }
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _favorites.value = favoritesDao.getAllFavorites().first()
        }
    }

    fun addToFavorites(name: String, apiUrl: String) {
        viewModelScope.launch {
            val userId = getApplication<Application>()
                .getSharedPreferences("user_prefs", Application.MODE_PRIVATE)
                .getInt("user_id", -1)
            val fav = FavoriteEntity(
                name = name,
                url = apiUrl,
                userId = userId
            )
            favoritesDao.insertFavorite(fav)
        }
    }


    fun removeFavoriteByUrl(url: String) {
        viewModelScope.launch {
            favoritesDao.deleteByUrl(url)
            loadFavorites()
        }
    }
}
