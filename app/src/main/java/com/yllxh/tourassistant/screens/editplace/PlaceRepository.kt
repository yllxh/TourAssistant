package com.yllxh.tourassistant.screens.editplace

import android.content.Context
import com.yllxh.tourassistant.data.source.local.database.AppDatabase
import com.yllxh.tourassistant.data.source.local.database.entity.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaceRepository(context: Context) {
    private val placeDao = AppDatabase.getInstance(context).placeDao

    suspend fun saveChangesToPlace(place: Place) {
        withContext(Dispatchers.IO) {
            if (place.placeId != 0L)
                placeDao.updatePlace(place)
            else
                placeDao.insertPlace(place)
        }
    }
}
