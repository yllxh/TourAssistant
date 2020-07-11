package com.yllxh.tourassistant.screens.followPath

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yllxh.tourassistant.data.source.local.database.entity.Path

class FollowPathViewModel(path: Path, app: Application) : AndroidViewModel(app) {

    var trackUser: Boolean = true
    private val _userLocation = MutableLiveData<Location>()
    val userLocation: LiveData<Location> get() = _userLocation

    fun updateUserLocation(userLocation: Location) {
        _userLocation.value = userLocation
    }
}