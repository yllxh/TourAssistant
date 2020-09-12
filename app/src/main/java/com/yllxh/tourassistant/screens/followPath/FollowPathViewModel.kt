package com.yllxh.tourassistant.screens.followPath

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.maps.model.DirectionsRoute
import com.yllxh.tourassistant.data.source.local.database.entity.Path
import com.yllxh.tourassistant.data.source.local.database.entity.Place
import com.yllxh.tourassistant.utils.toLatLng

class FollowPathViewModel(val path: Path, app: Application) : AndroidViewModel(app) {

    private val _accumulatedDistances = MutableLiveData<List<Pair<Place, Long>>>()
    val accumulatedDistances: LiveData<List<Pair<Place, Long>>> get() = _accumulatedDistances

    private val _nextDestination  = MutableLiveData<Pair<Place, Long>>()
    val nextDestination : LiveData<Pair<Place, Long>> get() = _nextDestination

    private val _secondNextDestination  = MutableLiveData<Pair<Place, Long>>()
    val secondNextDestination : LiveData<Pair<Place, Long>> get() = _secondNextDestination

    private val _thirdNextDestination  = MutableLiveData<Pair<Place, Long>>()
    val  thirdNextDestination : LiveData<Pair<Place, Long>> get() =  _thirdNextDestination

    var trackUser: Boolean = true

    private val _userLocation = MutableLiveData<Location>()
    val userLocation: LiveData<Location> get() = _userLocation

    fun updateUserLocation(userLocation: Location) {
        _userLocation.value = userLocation
    }

    fun onDirectionRouteRetrieved(route: DirectionsRoute) {

        val latitude = userLocation.value?.latitude ?: return
        val longitude = userLocation.value?.longitude ?: return
        val userLatLng = LatLng(latitude, longitude)

        val mutableListOf = mutableListOf<Pair<Place, Long>>()
        for ((i, place) in path.places.withIndex()) {
            if (i == 0) {
                mutableListOf.add(
                    Pair(
                        place,
                        getDistanceInMeters(userLatLng, route.legs.first().startLocation.toLatLng())
                    ))
            } else {
                mutableListOf.add(
                    Pair(place, mutableListOf[i - 1].second + route.legs[i - 1].distance.inMeters))
            }
        }
        _accumulatedDistances.value = mutableListOf
        _nextDestination.value = mutableListOf[0]
        _secondNextDestination.value = mutableListOf[1]
        _thirdNextDestination.value = mutableListOf[2]
    }

    fun onUserLocationUpdated(userLocation: Location){
        val latitude = userLocation.latitude
        val longitude = userLocation.longitude
        val userLatLng = LatLng(latitude, longitude)

        /*
        if distance between user and next is greater than 5m
            then
            update the first item in the accumulated Distances
        else
            then
            remove the current first item in the accumulated distance
            and update the new first item in the accumulated Distances
         */

    }

    private fun getDistanceInMeters(start: LatLng, end: LatLng): Long {
        val results = FloatArray(1)
        Location.distanceBetween(
            start.latitude,
            start.longitude,
            end.latitude,
            end.longitude,
            results
        )
        return results[0].toLong()
    }
}