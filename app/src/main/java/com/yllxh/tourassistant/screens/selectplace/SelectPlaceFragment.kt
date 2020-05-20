package com.yllxh.tourassistant.screens.selectplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.yllxh.tourassistant.R
import com.yllxh.tourassistant.data.source.local.database.entity.PlaceDB
import com.yllxh.tourassistant.databinding.FragmentSelectPlaceBinding
import com.yllxh.tourassistant.screens.selectplace.SelectPlaceFragmentDirections.actionSelectPlaceFragmentToEditPlaceFragment as toEditPlaceFragment

class SelectPlaceFragment : Fragment(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private lateinit var placesClient: PlacesClient
    private var marker: Marker? = null
    private val selectedPlace get() = viewModel.selectedPlace.value!!
    private lateinit var binding: FragmentSelectPlaceBinding
    private val viewModel by lazy {
        ViewModelProvider(this).get(SelectPlaceViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectPlaceBinding.inflate(inflater)

        val fromBundle = SelectPlaceFragmentArgs.fromBundle(requireArguments())
        viewModel.setSelectedPlace(fromBundle.selectedPlace)

        viewModel.selectedPlace.observe(viewLifecycleOwner, Observer {
            marker?.title = it.location.addressAsString

            Toast.makeText(requireContext(), it.location.addressAsString, Toast.LENGTH_SHORT).show()
        })

        (childFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment)
            .getMapAsync(this)


        binding.fab.setOnClickListener {
            findNavController().navigate(toEditPlaceFragment(selectedPlace))
        }
        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {

        map = googleMap.apply {

            if (selectedPlace.location.isValid()) {
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
                viewModel.setSelectedPlace(PlaceDB(selectedPlace.placeId).apply {
                    location.latitude = it.latitude
                    location.longitude = it.longitude
                })
            }
        }
    }
}
