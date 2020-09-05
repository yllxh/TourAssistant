package com.yllxh.tourassistant.screens.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.yllxh.tourassistant.data.source.local.database.AppDatabase
import com.yllxh.tourassistant.data.source.local.database.dao.CrossReferenceDao
import com.yllxh.tourassistant.data.source.local.database.dao.PathDao
import com.yllxh.tourassistant.data.source.local.database.dao.PlaceDao
import com.yllxh.tourassistant.data.source.local.database.dao.ToDoDao
import com.yllxh.tourassistant.data.source.local.database.entity.Path
import com.yllxh.tourassistant.data.source.local.database.entity.Place
import com.yllxh.tourassistant.data.source.local.database.entity.relation.PathWithPlaces
import com.yllxh.tourassistant.data.source.local.database.entity.relation.PlaceWithToDos

class MainRepository(context: Context) {

    private val toDosDao: ToDoDao
    private val placeDao: PlaceDao
    private val pathDao: PathDao
    private val crossRefDao: CrossReferenceDao

    init {
        val db = AppDatabase.getInstance(context)

        toDosDao = db.toDoDao
        placeDao = db.placeDao
        pathDao = db.pathDao
        crossRefDao = db.crossRefDao
    }

    val toDos = toDosDao.getAllToDos()

    val places: LiveData<List<Place>> =
        Transformations.map(placeDao.getAllPlacesWithToDos()) { placesWithToDos ->
            placesWithToDos.map(PlaceWithToDos::place)
        }

    val paths: LiveData<List<Path>> =
        Transformations.map(pathDao.getAllPathsWithPlaces()) { it.map(PathWithPlaces::path) }
}