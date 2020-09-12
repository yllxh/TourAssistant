package com.yllxh.tourassistant.screens.viewpath

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yllxh.tourassistant.data.source.local.database.entity.Path

class ViewPathViewModelFactory (private val path: Path, private val app: Application) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewPathViewModel::class.java)) {
            return ViewPathViewModel(path, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}