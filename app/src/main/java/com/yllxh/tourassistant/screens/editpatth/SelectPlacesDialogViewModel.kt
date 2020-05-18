package com.yllxh.tourassistant.screens.editpatth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.yllxh.tourassistant.data.source.local.database.AppDatabase
import com.yllxh.tourassistant.data.source.local.database.entity.PlaceDB

class SelectPlacesDialogViewModel(app: Application) : AndroidViewModel(app) {

    private val placesDao = AppDatabase.getInstance(app).placeDao
    val places: LiveData<List<PlaceDB>> get() = placesDao.getAllPlaces()


}
