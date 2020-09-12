package com.yllxh.tourassistant.screens.selectplaces

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.yllxh.tourassistant.data.source.local.database.AppDatabase
import com.yllxh.tourassistant.data.source.local.database.entity.Place

class SelectPlacesViewModel(app: Application) : AndroidViewModel(app) {

    private val placesDao = AppDatabase.getInstance(app).placeDao
    val places: LiveData<List<Place>> get() = placesDao.getAllPlaces()

    private var _selectedPlaces: List<Place>? = null
    val selectedPlaces: List<Place>? get() = _selectedPlaces


    fun setSelectedItems(places: List<Place>) {
        _selectedPlaces = places
    }
}
