package com.yllxh.tourassistant.screens.editplace

import android.content.Context
import androidx.lifecycle.LiveData
import com.yllxh.tourassistant.data.source.local.database.AppDatabase
import com.yllxh.tourassistant.data.source.local.database.dao.PlaceDao
import com.yllxh.tourassistant.data.source.local.database.entity.Place
import com.yllxh.tourassistant.data.source.local.database.entity.ToDo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaceRepository(place: Place, context: Context) {
    private val placeDao: PlaceDao
    val toDos: LiveData<List<ToDo>>

    init {
        val db = AppDatabase.getInstance(context)
        placeDao = db.placeDao
        toDos = db.toDoDao.getToDosWithPlaceId(place.placeId)
    }

    suspend fun saveChangesToPlace(place: Place) = withContext(Dispatchers.IO) {
        if (place.placeId != 0L)
            placeDao.updatePlace(place)
        else
            placeDao.insertPlace(place)
    }
}
