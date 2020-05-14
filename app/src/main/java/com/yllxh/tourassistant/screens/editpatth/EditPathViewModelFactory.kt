package com.yllxh.tourassistant.screens.editpatth

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yllxh.tourassistant.data.source.local.database.entity.Path

class EditPathViewModelFactory(private val path: Path, private val app: Application) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditPathViewModel::class.java)) {
            return EditPathViewModel(path, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}