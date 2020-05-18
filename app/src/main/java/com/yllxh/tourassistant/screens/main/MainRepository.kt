package com.yllxh.tourassistant.screens.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.yllxh.tourassistant.data.source.local.database.AppDatabase
import com.yllxh.tourassistant.data.source.local.database.dao.CrossReferenceDao
import com.yllxh.tourassistant.data.source.local.database.dao.PathDao
import com.yllxh.tourassistant.data.source.local.database.dao.PlaceDao
import com.yllxh.tourassistant.data.source.local.database.entity.PathDB

class MainRepository(context: Context) {

    private val placeDao: PlaceDao
    private val pathDao: PathDao
    private val crossRefDao: CrossReferenceDao

    init {
        val db = AppDatabase.getInstance(context)

        placeDao = db.placeDao
        pathDao = db.pathDao
        crossRefDao = db.crossRefDao
    }

    val places = placeDao.getAllPlaces()

    private val pathsWithPlaces = pathDao.getAllPathsWithPlaces()
    val paths: LiveData<List<PathDB>> = Transformations.map(pathsWithPlaces) { it ->
        return@map it.map { it.path }
    }
}