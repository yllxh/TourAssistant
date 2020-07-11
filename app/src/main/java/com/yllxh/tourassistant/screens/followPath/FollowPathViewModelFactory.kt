package com.yllxh.tourassistant.screens.followPath

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yllxh.tourassistant.data.source.local.database.entity.Path
import java.lang.IllegalArgumentException

class FollowPathViewModelFactory(
    private val path: Path,
    private val app: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FollowPathViewModel::class.java)) {
            return FollowPathViewModel(path, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}