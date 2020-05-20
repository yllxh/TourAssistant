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
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.yllxh.tourassistant.R
import com.yllxh.tourassistant.data.model.Address
import com.yllxh.tourassistant.data.model.LocationDB
import com.yllxh.tourassistant.data.source.local.database.entity.PlaceDB
import com.yllxh.tourassistant.databinding.FragmentSelectPlaceBinding
import kotlinx.android.synthetic.main.activity_main.*
import com.yllxh.tourassistant.screens.selectplace.SelectPlaceFragmentDirections.actionSelectPlaceFragmentToEditPlaceFragment as toEditPlaceFragment


private val placeFields = listOf(
    Place.Field.ID,
    Place.Field.ADDRESS,
    Place.Field.NAME,
    Place.Field.LAT_LNG
)

class SelectPlaceFragment : Fragment(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private lateinit var autoComplete: AutocompleteSupportFragment
    private lateinit var placesClient: PlacesClient
    private var marker: Marker? = null
    private val selectedPlace: PlaceDB get() = viewModel.selectedPlace.value!!
    private lateinit var binding: FragmentSelectPlaceBinding
    private val viewModel by lazy {
        val place = SelectPlaceFragmentArgs.fromBundle(requireArguments()).selectedPlace
        val factory = SelectPlaceViewModelFactory(place, requireActivity().application)
        ViewModelProvider(this, factory).get(SelectPlaceViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Places.initialize(requireContext(), getString(R.string.google_maps_key))
        placesClient = Places.createClient(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectPlaceBinding.inflate(inflater)

        viewModel.selectedPlace.observe(viewLifecycleOwner, Observer {
            marker?.title = it.location.addressAsString

            Toast.makeText(requireContext(), it.location.addressAsString, Toast.LENGTH_SHORT).show()
        })
        autoComplete =
            (childFragmentManager
                .findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment)
                .apply { setPlaceFields(placeFields) }

        autoComplete.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                viewModel.setSelectedPlace(place.toPlaceDB())
                place.latLng?.let {
                    marker = map.addMarker(MarkerOptions()
                        .position(it))
                        .apply { title = selectedPlace.name }

                    map.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(it, 15f)
                    )
                }
            }

            override fun onError(status: Status) {
                Toast.makeText(
                    requireContext(),
                    "Place could not be retrieved",
                    Toast.LENGTH_SHORT
                ).show()
            }

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

private fun Place.toPlaceDB(): PlaceDB {
    val latitude = latLng?.latitude ?: Double.MAX_VALUE
    val longitude = latLng?.longitude ?: Double.MAX_VALUE

    val placeDB = PlaceDB()
    placeDB.name = name ?: ""
    placeDB.location = LocationDB(
        latitude = latitude,
        longitude = longitude,
        address = Address(address = address ?: "")
    )

    return placeDB
}
