package com.yllxh.tourassistant.screens.followPath

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
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
import com.yllxh.tourassistant.databinding.FragmentFollowPathBinding
import com.yllxh.tourassistant.utils.*

class FollowPathFragment : Fragment(), OnMapReadyCallback {
    private lateinit var geoApiContext: GeoApiContext
    private lateinit var map: GoogleMap

    private lateinit var locationRetriever: LocationRetriever

    private val path by lazy { FollowPathFragmentArgs.fromBundle(requireArguments()).path }

    private lateinit var binding: FragmentFollowPathBinding
    private val viewModel by lazy {
        val factory = FollowPathViewModelFactory(path, requireActivity().application)
        ViewModelProvider(this, factory).get(FollowPathViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowPathBinding.inflate(inflater, container, false)
        (childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment)
            .getMapAsync(this)

        if (!::geoApiContext.isInitialized) {
            geoApiContext = GeoApiContext.Builder()
                .apiKey(getString(R.string.google_maps_key))
                .build()
        }

        binding.trackUserLocationButton.setOnClickListener {
            val color = if (viewModel.trackUser) {
                R.color.colorStoppedTrackingUser
            } else {
                R.color.colorTrackingUser
            }
            binding.trackUserLocationButton.setColor(color)

            viewModel.trackUser = !viewModel.trackUser
        }

        observe(viewModel.userLocation) {
            if (viewModel.trackUser){
                map.animateCameraAt(
                    com.google.android.gms.maps.model.LatLng(it.latitude, it.longitude))
            }
        }
        return binding.root
    }

    private fun calculatePath() {
        val locations = mutableListOf<LatLng>()
        path.places.forEach {
            locations.add(it.location.toMapsModelLatLng())
        }

        val first = locations.first()
        DirectionsApiRequest(geoApiContext).apply {
            origin(first)
            destination(locations.last())
            map.animateCameraAt(com.google.android.gms.maps.model.LatLng(first.lat, first.lng))


            val waypoints = locations.apply {
                // Remove the origin and the destination from the waypoints
                removeAt(size - 1)
                removeAt(0)
            }.toTypedArray()

            waypoints(*waypoints)
            mode(TravelMode.WALKING)
            setCallback(directionRequestResponse)
        }
    }

    private val directionRequestResponse = object : PendingResult.Callback<DirectionsResult?> {
        override fun onFailure(e: Throwable?) {
            Log.d("FollowPathFragment", "onResult: $e")
        }

        override fun onResult(result: DirectionsResult?) = onMainThread {
            val routes = result?.routes ?: return@onMainThread

            for (route in routes) {
//                viewModel.onDirectionRouteRetrieved(route)

                val decodedPath: MutableList<LatLng> =
                    PolylineEncoding.decode(route.overviewPolyline.encodedPath)

                val polyline = PolylineOptions().apply {
                    addAll(decodedPath.map {
                        com.google.android.gms.maps.model.LatLng(
                            it.lat,
                            it.lng
                        )
                    })
                }
                val addPolyline = map.addPolyline(polyline)
                addPolyline.color =
                    ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark)
                addPolyline.width = 10f

            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {

        map = googleMap.apply {
            uiSettings.isMyLocationButtonEnabled = false
            if (hasLocationPermission()) {
                enableUserTracking()
            } else {
                isMyLocationEnabled = false
                binding.trackUserLocationButton.isEnabled = false
                onMissingLocationPermission { this.enableUserTracking() }
            }

            path.places.forEach {
                if (it.location.isValid()) {
                    addSimpleMarker(it)
                }
            }
        }
        calculatePath()
        locationRetriever = LocationRetriever(
            this,
            keepTrackOfUser = true,
            onLocationReceived = viewModel::updateUserLocation,
            onMissingPermission = { onMissingLocationPermission() }
        )
        locationRetriever.requestDeviceLocation()
    }

    @SuppressLint("MissingPermission")
    private fun GoogleMap.enableUserTracking() {
        isMyLocationEnabled = true
        binding.trackUserLocationButton.isEnabled = true
    }
}



















