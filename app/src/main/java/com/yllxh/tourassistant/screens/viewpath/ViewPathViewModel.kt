package com.yllxh.tourassistant.screens.viewpath

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.yllxh.tourassistant.data.source.local.database.AppDatabase
import com.yllxh.tourassistant.data.source.local.database.entity.Path

class ViewPathViewModel(path: Path, app: Application) : AndroidViewModel(app) {

    var path: LiveData<Path> = Transformations.map(
        AppDatabase.getInstance(getApplication())
            .pathDao
            .getPath(path.pathId)
    ) { it.path }

}