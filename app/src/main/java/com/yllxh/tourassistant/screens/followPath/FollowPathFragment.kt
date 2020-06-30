package com.yllxh.tourassistant.screens.followPath

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap

import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.internal.PolylineEncoding
import com.google.maps.model.DirectionsResult
import com.google.maps.model.LatLng
import com.google.maps.model.TravelMode
import com.yllxh.tourassistant.R
import com.yllxh.tourassistant.utils.animateCameraAt
import com.yllxh.tourassistant.utils.onMainThread
import com.yllxh.tourassistant.utils.toMapsModelLatLng
import com.google.android.gms.maps.model.LatLng as AndroidGmsMapsModelLatLng

class FollowPathFragment : Fragment() {
    private lateinit var map: GoogleMap

    private val path by lazy { FollowPathFragmentArgs.fromBundle(requireArguments()).path }

    private lateinit var geoApiContext: GeoApiContext
    private val onMapReadyCallback = OnMapReadyCallback { googleMap ->
        map = googleMap
        calculatePath()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_follow_path, container, false)

        (childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment)
            .getMapAsync(onMapReadyCallback)

        if (!::geoApiContext.isInitialized) {
            geoApiContext = GeoApiContext.Builder()
                .apiKey(getString(R.string.google_maps_key))
                .build()
        }
        return inflate.rootView
    }


    private fun calculatePath() {
        val locations = mutableListOf<LatLng>().apply{
            path.places.forEach {
                add(it.location.toMapsModelLatLng())
            }
        }
        DirectionsApiRequest(geoApiContext).apply {
            val first = locations.first()
            origin(first)
            destination(locations.last())
            map.animateCameraAt(AndroidGmsMapsModelLatLng(first.lat,first.lng))

            waypoints(*(locations.apply {
                removeAt(0)
                removeAt(size - 1)
            }.toTypedArray()))

            mode(TravelMode.WALKING)
            setCallback(object : PendingResult.Callback<DirectionsResult?> {
                override fun onFailure(e: Throwable?) {
                    Log.d("FollowPathFragment", "onResult: ${e?.printStackTrace()}")
                }

                override fun onResult(result: DirectionsResult?)  = onMainThread {
                    val routes = result?.routes
                        ?: return@onMainThread
                    for (route in routes) {
                        val decodedPath: MutableList<LatLng> =
                            PolylineEncoding.decode(route.overviewPolyline.encodedPath)

                        val polyline = PolylineOptions().apply {
                            addAll(decodedPath.map { AndroidGmsMapsModelLatLng(it.lat, it.lng) })
                        }
                        val addPolyline = map.addPolyline(polyline)
                        addPolyline.color = ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark)
                        addPolyline.width = 10f
                    }
                }
            })
        }

    }
}



















