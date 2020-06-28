package com.yllxh.tourassistant.screens.followPath

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.model.DirectionsResult
import com.google.maps.model.LatLng
import com.yllxh.tourassistant.R
import com.yllxh.tourassistant.utils.toMapsModelLatLng
import com.yllxh.tourassistant.utils.toast

class FollowPathFragment : Fragment() {

    private val path by lazy { FollowPathFragmentArgs.fromBundle(requireArguments()).path }

    private lateinit var geoApiContext: GeoApiContext
    private val onMapReadyCallback = OnMapReadyCallback { googleMap ->

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment)
            .getMapAsync(onMapReadyCallback)

        if (!::geoApiContext.isInitialized) {
            geoApiContext = GeoApiContext.Builder()
                .apiKey(getString(R.string.google_maps_key))
                .build()
        }
        return inflater.inflate(R.layout.fragment_follow_path, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calculatePath()

    }

    private fun calculatePath() {
        val locations = mutableListOf<LatLng>().apply{
            path.places.forEach {
                add(it.location.toMapsModelLatLng())
            }
        }
        DirectionsApiRequest(geoApiContext).apply {
            origin(locations.first())
            destination(locations.last())

            waypoints(*(locations.apply {
                removeAt(0)
                removeAt(size - 1)
            }.toTypedArray()))

            setCallback(object : PendingResult.Callback<DirectionsResult?> {
                override fun onFailure(e: Throwable?) {
                    toast(e.toString())
                }

                override fun onResult(result: DirectionsResult?) {
                    Log.d("AAAAAAAAAAAA", "onResult: ${result.toString()}")
                }
            })
        }

    }
}



















