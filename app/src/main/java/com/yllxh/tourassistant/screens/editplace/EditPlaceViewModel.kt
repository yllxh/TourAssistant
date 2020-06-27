package com.yllxh.tourassistant.screens.editplace

import android.app.Application
import androidx.lifecycle.*
import com.yllxh.tourassistant.data.source.local.database.entity.Place
import com.yllxh.tourassistant.data.source.local.database.entity.ToDo
import kotlinx.coroutines.launch

class EditPlaceViewModel(place: Place, app: Application) : AndroidViewModel(app) {

    private val repository = PlaceRepository(place, app.applicationContext)

    val toDos: LiveData<List<ToDo>> get() = repository.toDos

    fun saveChangesToPlace(place: Place) = viewModelScope.launch {
        repository.saveChangesToPlace(place)
    }

}