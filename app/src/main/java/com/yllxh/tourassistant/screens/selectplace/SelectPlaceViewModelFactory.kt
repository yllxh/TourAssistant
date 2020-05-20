package com.yllxh.tourassistant.screens.selectplace

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yllxh.tourassistant.data.source.local.database.entity.PlaceDB
import java.lang.IllegalArgumentException

class SelectPlaceViewModelFactory(
    private val placeDB: PlaceDB,
    private val app: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SelectPlaceViewModel::class.java)) {
            return SelectPlaceViewModel(placeDB, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}