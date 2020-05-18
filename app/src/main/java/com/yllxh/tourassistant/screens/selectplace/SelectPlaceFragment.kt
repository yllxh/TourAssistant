package com.yllxh.tourassistant.screens.selectplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.yllxh.tourassistant.R
import com.yllxh.tourassistant.data.source.local.database.entity.PlaceDB
import com.yllxh.tourassistant.databinding.FragmentSelectPlaceBinding
import com.yllxh.tourassistant.screens.selectplace.SelectPlaceFragmentDirections.actionSelectPlaceFragmentToEditPlaceFragment as toEditPlaceFragment

class SelectPlaceFragment : Fragment(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private lateinit var selectedPlace: PlaceDB
    private var marker: Marker? = null
    private lateinit var binding: FragmentSelectPlaceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectPlaceBinding.inflate(inflater)
        (childFragmentManager
            .findFragmentById(R.id.mapFragment)
                as SupportMapFragment)
            .getMapAsync(this)

        selectedPlace = SelectPlaceFragmentArgs.fromBundle(requireArguments()).selectedPlace

        binding.fab.setOnClickListener {
            findNavController().navigate(toEditPlaceFragment(selectedPlace))
        }
        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {

        map = googleMap.apply {

            if (::selectedPlace.isInitialized && selectedPlace.location.isValid()){
                val latLng = LatLng(
                    selectedPlace.location.latitude,
                    selectedPlace.location.longitude
                )
                marker = googleMap.addMarker(MarkerOptions().position(latLng))
            }

            setOnMapLongClickListener {
                if (marker != null) {
                    marker?.remove()
                }
                marker = googleMap.addMarker(MarkerOptions().position(it))
                selectedPlace = selectedPlace.apply {
                    location.latitude = it.latitude
                    location.longitude = it.longitude
                }
            }
        }
    }
}
