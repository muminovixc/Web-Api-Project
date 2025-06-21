package com.np.apiaplikacija.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.np.apiaplikacija.data.db.AppDatabase
import com.np.apiaplikacija.data.db.DatasetEntity
import com.np.apiaplikacija.data.db.FavoriteEntity
import com.np.apiaplikacija.model.DatasetItem
import com.np.apiaplikacija.repository.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ApiViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ApiRepository()
    private val db = AppDatabase.getInstance(application)
    private val datasetDao = db.datasetDao()
    private val favoritesDao = db.favoritesDao()

    private val _datasets = MutableStateFlow<List<DatasetItem>>(emptyList())
    val datasets: StateFlow<List<DatasetItem>> = _datasets

    private val _favorites = MutableStateFlow<List<FavoriteEntity>>(emptyList())
    val favorites: StateFlow<List<FavoriteEntity>> = _favorites

    private val sharedPrefs = application.getSharedPreferences("user_prefs", Application.MODE_PRIVATE)
    private val userId = sharedPrefs.getInt("user_id", -1)

    fun loadDatasets(token: String) {
        viewModelScope.launch {
            try {
                val response = repository.fetchDatasets(token)
                if (response.success) {
                    _datasets.value = response.result

                    // Cache data in local Room DB
                    datasetDao.deleteAll()
                    datasetDao.insertAll(response.result.map {
                        DatasetEntity(id = it.id, name = it.name, apiUrl = it.apiUrl)
                    })
                } else {
                    loadCachedDatasets()
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Greška prilikom poziva: ${e.message}", e)
                loadCachedDatasets()
            }
        }
    }

    private suspend fun loadCachedDatasets() {
        val cached = datasetDao.getAllDatasets().map {
            DatasetItem(id = it.id, name = it.name, apiUrl = it.apiUrl)

        }
        _datasets.value = cached
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _favorites.value = favoritesDao.getAllFavorites().first()
        }
    }

    fun addToFavorites(name: String, apiUrl: String) {
        viewModelScope.launch {
            val fav = FavoriteEntity(name = name, url = apiUrl, userId = userId)
            favoritesDao.insertFavorite(fav)
            loadFavorites()
        }
    }

    fun removeFavoriteByUrl(url: String) {
        viewModelScope.launch {
            favoritesDao.deleteByUrl(url)
            loadFavorites()
        }
    }
}
