package com.yllxh.tourassistant.screens.editpatth

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yllxh.tourassistant.data.source.local.database.AppDatabase
import com.yllxh.tourassistant.data.source.local.database.dao.CrossReferenceDao
import com.yllxh.tourassistant.data.source.local.database.dao.PathDao
import com.yllxh.tourassistant.data.source.local.database.entity.Path
import com.yllxh.tourassistant.data.source.local.database.entity.crossreference.PathPlaceCrossRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EditPathRepository(
    app: Application,
    path: Path
) {
    private val pathDao: PathDao
    private val crossRefDao: CrossReferenceDao

    init {
        val db = AppDatabase.getInstance(app)
        pathDao = db.pathDao
        crossRefDao = db.crossRefDao
    }

    private val _path = MutableLiveData(path)
    val path: LiveData<Path> get() = _path

    suspend fun savePath(editedPath: Path) = withContext(Dispatchers.IO) {

        var savedPath = editedPath

        if (editedPath.pathId == 0L) {
            // required to properly insert crossRef
            val newId = pathDao.insertPath(editedPath)
            savedPath = editedPath.copy(pathId = newId)
        } else {
            removeUnselectedPathPlacesCrossRef(editedPath)
            pathDao.updatePath(editedPath)
        }

        insertCrossRef(savedPath)
    }

    private fun insertCrossRef(editedPath: Path) = with(mutableListOf<PathPlaceCrossRef>()) {

        editedPath.places.forEach { place ->
            add(PathPlaceCrossRef(editedPath.pathId, place.placeId))
        }
        crossRefDao.insertCrossRefs(this)
    }

    private fun removeUnselectedPathPlacesCrossRef(path: Path) {
        val currentlySelected = path.places
        val unselectedPlaces = pathDao.getRawPath(path.pathId).path.places.filter {
            !currentlySelected.contains(it)
        }

        with(mutableListOf<PathPlaceCrossRef>()) { // cross reference to be be removed

            unselectedPlaces.forEach {
                add(PathPlaceCrossRef(path.pathId, it.placeId))
            }

            crossRefDao.removeCrossRefs(this)
        }
    }

}