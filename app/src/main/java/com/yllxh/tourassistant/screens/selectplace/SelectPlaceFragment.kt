package com.yllxh.tourassistant.screens.selectplace

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.yllxh.tourassistant.R
import com.yllxh.tourassistant.databinding.FragmentSelectPlaceBinding
import com.yllxh.tourassistant.utils.LocationRetriever

private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
private const val DEFAULT_CAMERA_ZOOM = 15f

class SelectPlaceFragment : Fragment(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private val locationRetriever by lazy {
        LocationRetriever(requireContext(), onLocationReceived = this::moveCameraToLocation)
    }
    private lateinit var binding: FragmentSelectPlaceBinding

    private val isLocationPermissionGranted: Boolean
        get() {
            return ContextCompat.checkSelfPermission(
                requireContext().applicationContext,
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectPlaceBinding.inflate(inflater)
        (childFragmentManager
            .findFragmentById(R.id.mapFragment)
                as SupportMapFragment)
            .getMapAsync(this)


        binding.fab.setOnClickListener { startUpdatingUserLocation() }
        return binding.root
    }

    private fun startUpdatingUserLocation() {
        if (!isLocationPermissionGranted) {
            getLocationPermission()
            return
        }
        locationRetriever.toggleTracking()
    }

    private fun getLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(ACCESS_FINE_LOCATION),
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    startUpdatingUserLocation()
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap.apply {
            setOnMapLongClickListener {
                googleMap.addMarker(MarkerOptions().position(it))
            }
        }
    }

    private fun Location.getLatLng(): LatLng {
        return LatLng(latitude, longitude)
    }

    private fun moveCameraToLocation(location: Location) {
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                location.getLatLng(),
                DEFAULT_CAMERA_ZOOM
            )
        )
    }
}
