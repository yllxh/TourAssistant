package com.yllxh.tourassistant.screens.selectplace

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import android.os.Bundle
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.model.Place as GoogleApi_Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.yllxh.tourassistant.R
import com.yllxh.tourassistant.data.model.Location
import com.yllxh.tourassistant.data.source.local.database.entity.Place
import com.yllxh.tourassistant.databinding.FragmentSelectPlaceBinding
import com.yllxh.tourassistant.utils.*
import com.yllxh.tourassistant.screens.selectplace.SelectPlaceFragmentDirections.actionSelectPlaceFragmentToEditPlaceFragment as toEditPlaceFragment


private val placeFields = listOf(
    GoogleApi_Place.Field.ADDRESS,
    GoogleApi_Place.Field.ID,
    GoogleApi_Place.Field.NAME,
    GoogleApi_Place.Field.LAT_LNG
)

class SelectPlaceFragment : Fragment(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private lateinit var autoComplete: AutocompleteSupportFragment
    private lateinit var placesClient: PlacesClient
    private var marker: Marker? = null
    private val selectedPlace: Place get() = viewModel.selectedPlace.value!!
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

        (childFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment)
            .getMapAsync(this)

        autoComplete =
            (childFragmentManager
                .findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment)
                .apply { setPlaceFields(placeFields) }

        autoComplete.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(apiPlace: GoogleApi_Place) {
                viewModel.setSelectedPlace(apiPlace.toPlace())
            }

            override fun onError(status: Status) {
                toast("Place could not be retrieved")
            }
        })


        observe(viewModel.selectedPlace) {
            if (!::map.isInitialized)
                return@observe
            marker?.remove()
            marker = map.addSimpleMarker(it)
            map.animateCamera(it)

            if (!it.location.addressAsString.isEmptyOrBlank()) {
                marker!!.title = it.location.addressAsString
            }
        }

        observe(viewModel.fetchingInfo){
            when(it){
                REQUEST.STARTED -> toast("Fetching info...")
                REQUEST.FINISHED -> toast("Done.")
                REQUEST.FAILED -> toast("Info not found.")
                REQUEST.UNKNOWN -> {}
            }
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(toEditPlaceFragment(selectedPlace))
        }
        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap.apply {
            if (selectedPlace.location.isValid()) {
                marker = googleMap.addSimpleMarker(selectedPlace)
                googleMap.animateCamera(selectedPlace)
            }

            setOnMapLongClickListener {

                val newPlace = Place(
                    selectedPlace.placeId,
                    location = Location(it.latitude, it.longitude),
                    _importance = selectedPlace.importance
                )
                viewModel.setSelectedPlace(newPlace)
            }
        }
    }
}


