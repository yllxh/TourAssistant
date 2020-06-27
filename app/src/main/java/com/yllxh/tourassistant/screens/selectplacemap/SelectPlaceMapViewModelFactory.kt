package com.yllxh.tourassistant.screens.selectplacemap

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yllxh.tourassistant.data.source.local.database.entity.Place
import java.lang.IllegalArgumentException

class SelectPlaceMapViewModelFactory(
    private val place: Place,
    private val app: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SelectPlaceMapViewModel::class.java)) {
            return SelectPlaceMapViewModel(place, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}