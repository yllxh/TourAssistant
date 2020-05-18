package com.yllxh.tourassistant.screens.editplace

import android.app.Application
import androidx.lifecycle.*
import com.yllxh.tourassistant.data.source.local.database.entity.PlaceDB
import kotlinx.coroutines.launch

class EditPlaceViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = PlaceRepository(app.applicationContext)

    fun saveChangesToPlace(placeDB: PlaceDB) {
        viewModelScope.launch {
            repository.saveChangesToPlace(placeDB)
        }
    }

}