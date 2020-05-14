package com.yllxh.tourassistant.screens.editpatth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yllxh.tourassistant.data.source.local.database.AppDatabase
import com.yllxh.tourassistant.data.source.local.database.entity.Place

class SelectPlacesDialogViewModel(app: Application) : AndroidViewModel(app) {

    private val placesDao = AppDatabase.getInstance(app).placeDao
    val places: LiveData<List<Place>> get() = placesDao.getAllPlaces()


}
