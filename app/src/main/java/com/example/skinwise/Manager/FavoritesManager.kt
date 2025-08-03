package com.example.skinwise.utils

import android.content.Context
import android.content.SharedPreferences

class FavoritesManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)

    fun addFavorite(id: Long) {
        val favorites = getFavoriteIds().toMutableSet()
        favorites.add(id.toString())
        saveFavorites(favorites)
    }

    fun removeFavorite(id: Long) {
        val favorites = getFavoriteIds().toMutableSet()
        favorites.remove(id.toString())
        saveFavorites(favorites)
    }

    fun isFavorite(id: Long): Boolean {
        return getFavoriteIds().contains(id.toString())
    }

    fun getFavoriteIds(): Set<String> {
        return prefs.getStringSet("favorite_ids", emptySet()) ?: emptySet()
    }

    private fun saveFavorites(favorites: Set<String>) {
        prefs.edit().putStringSet("favorite_ids", favorites).apply()
    }
}
