package com.yllxh.tourassistant.screens.editpatth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yllxh.tourassistant.data.source.local.database.AppDatabase
import com.yllxh.tourassistant.data.source.local.database.dao.CrossReferenceDao
import com.yllxh.tourassistant.data.source.local.database.dao.PathDao
import com.yllxh.tourassistant.data.source.local.database.entity.Path
import com.yllxh.tourassistant.data.source.local.database.entity.Place
import com.yllxh.tourassistant.data.source.local.database.entity.crossreference.PathPlaceCrossRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditPathViewModel(path: Path, app: Application) : AndroidViewModel(app) {

    private val pathDao: PathDao
    private val crossRefDao: CrossReferenceDao

    init {
        val db = AppDatabase.getInstance(app)
        pathDao = db.pathDao
        crossRefDao = db.crossRefDao
    }

    private val _path = MutableLiveData(path)
    val path get() = _path
    private val originallySelectedPlaces = path.places.toList()

    fun saveChangesToPath(editedPath: Path) {
        viewModelScope.launch {
            savePath(editedPath)
        }
    }

    private suspend fun savePath(editedPath: Path) = withContext(Dispatchers.IO) {
        val path = _path.value?.copy(name = editedPath.name) ?: return@withContext

        removeUnselectedPathPlacesCrossRef(path)

        val listOfCrossRef = mutableListOf<PathPlaceCrossRef>()

        path.places.forEach { place ->
            listOfCrossRef.add(PathPlaceCrossRef(path.pathId, place.placeId))
        }

        pathDao.insertPath(path)
        crossRefDao.insertCrossRefs(listOfCrossRef)

    }

    private fun removeUnselectedPathPlacesCrossRef(path: Path) {
        val currentlySelected = path.places
        val unselectedPlaces = originallySelectedPlaces.filter {
            !currentlySelected.contains(it)
        }

        val crossRef = mutableListOf<PathPlaceCrossRef>()

        unselectedPlaces.forEach {
            crossRef.add(PathPlaceCrossRef(path.pathId, it.placeId))
        }

        crossRefDao.removeCrossRefs(crossRef)
    }

    fun setSelectedPlaces(selectedPlaces: List<Place>) {
        val value: Path = _path.value ?: return

        _path.value = value.apply {
            places = selectedPlaces
        }
    }
}