package com.np.apiaplikacija.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val password: String
)


@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val url: String,
    val userId: Int
)

@Entity(tableName = "datasets")
data class DatasetEntity(
    @PrimaryKey(autoGenerate = true) val autoincrement: Int = 0,
    val id: Int,
    val name: String,
    val apiUrl: String
)

