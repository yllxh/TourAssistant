package com.yllxh.tourassistant.screens.editplace

import android.content.Context
import com.yllxh.tourassistant.data.source.local.database.AppDatabase
import com.yllxh.tourassistant.data.source.local.database.entity.PlaceDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaceRepository(context: Context) {
    private val placeDao = AppDatabase.getInstance(context).placeDao

    suspend fun saveChangesToPlace(placeDB: PlaceDB) {
        withContext(Dispatchers.IO) {
            if (placeDB.placeId != 0L)
                placeDao.updatePlace(placeDB)
            else
                placeDao.insertPlace(placeDB)
        }
    }
}
