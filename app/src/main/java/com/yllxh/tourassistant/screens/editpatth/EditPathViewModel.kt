package com.yllxh.tourassistant.screens.editpatth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yllxh.tourassistant.data.source.local.database.entity.Path
import com.yllxh.tourassistant.data.source.local.database.entity.Place
import kotlinx.coroutines.launch

class EditPathViewModel(path: Path, app: Application) : AndroidViewModel(app) {

    private val repository = EditPathRepository(app, path)

    val path get() =  repository.path

    fun saveChangesToPath(editedPath: Path) {
        viewModelScope.launch {
            repository.savePath(editedPath)
        }
    }
}