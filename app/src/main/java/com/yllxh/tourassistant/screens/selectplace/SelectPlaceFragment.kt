package com.yllxh.tourassistant.screens.selectplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.yllxh.tourassistant.R
import com.yllxh.tourassistant.data.source.local.database.entity.PlaceDB
import com.yllxh.tourassistant.databinding.FragmentSelectPlaceBinding

class SelectPlaceFragment : Fragment(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private var selectedPlace: PlaceDB = PlaceDB()
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


        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap.apply {
            setOnMapLongClickListener {
                if(marker != null){
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
