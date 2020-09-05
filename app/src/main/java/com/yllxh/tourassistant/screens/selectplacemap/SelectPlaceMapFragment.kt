package com.yllxh.tourassistant.screens.selectplacemap

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.PointOfInterest
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.yllxh.tourassistant.R
import com.yllxh.tourassistant.data.model.Location
import com.yllxh.tourassistant.data.source.local.database.entity.Place
import com.yllxh.tourassistant.databinding.FragmentSelectPlaceBinding
import com.yllxh.tourassistant.utils.*
import com.google.android.libraries.places.api.model.Place as GoogleApi_Place
import com.yllxh.tourassistant.screens.selectplacemap.SelectPlaceMapFragmentDirections.actionSelectPlaceFragmentToEditPlaceFragment as toEditPlaceFragment


private val placeFields = listOf(
    GoogleApi_Place.Field.ADDRESS,
    GoogleApi_Place.Field.ID,
    GoogleApi_Place.Field.NAME,
    GoogleApi_Place.Field.LAT_LNG
)

class SelectPlaceMapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private lateinit var autoComplete: AutocompleteSupportFragment
    private lateinit var placesClient: PlacesClient
    private var marker: Marker? = null

    private lateinit var locationRetriever: LocationRetriever

    private lateinit var binding: FragmentSelectPlaceBinding
    private val selectedPlace: Place get() = viewModel.selectedPlace.value!!
    private val viewModel by lazy {
        val place = SelectPlaceMapFragmentArgs.fromBundle(requireArguments()).selectedPlace
        val factory = SelectPlaceMapViewModelFactory(place, requireActivity().application)
        ViewModelProvider(this, factory).get(SelectPlaceMapViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

        autoComplete = (childFragmentManager
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
        binding.trackUserLocationButton.setOnClickListener {
            if (locationRetriever.keepTrackOfUser) {
                locationRetriever.keepTrackOfUser = false
                binding.trackUserLocationButton.setColor(R.color.colorStoppedTrackingUser)
            } else {
                locationRetriever.keepTrackOfUser = true
                locationRetriever.requestDeviceLocation()
                binding.trackUserLocationButton.setColor(R.color.colorTrackingUser)
            }
        }
        observe(viewModel.selectedPlace) {
            if (!::map.isInitialized || it.location.isNotValid())
                return@observe

            marker?.remove()
            marker = map.addSimpleMarker(it)
            map.animateCameraAt(it)


            if (!it.location.addressAsString.isBlank()) {
                marker!!.title = it.location.addressAsString
            }
        }

        observe(viewModel.fetchingInfo) {
            if (selectedPlace.placeId == 0L)
                return@observe

            when (it) {
                REQUEST.STARTED -> toast("Fetching info...")
                REQUEST.FINISHED -> toast("Done.")
                REQUEST.FAILED -> toast("Info not found.")
                REQUEST.UNKNOWN -> {
                }
            }
        }

        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap.apply {
            uiSettings.isMyLocationButtonEnabled = false
            if (hasLocationPermission()) {
                isMyLocationEnabled = true
                binding.trackUserLocationButton.isEnabled = true
            } else {
                isMyLocationEnabled = false
                binding.trackUserLocationButton.isEnabled = false
                onMissingLocationPermission()
            }
            if (selectedPlace.location.isValid()) {
                marker = addSimpleMarker(selectedPlace)
                animateCameraAt(selectedPlace)
            }

            setOnMapLongClickListener(this@SelectPlaceMapFragment::onMapLongClickListener)
            setOnPoiClickListener(this@SelectPlaceMapFragment::onPoiClickListener)
        }

        locationRetriever = LocationRetriever(
            this,
            onLocationReceived = viewModel::updateUserLocation,
            onMissingPermission = { onMissingLocationPermission() }
        )

        locationRetriever.requestDeviceLocation()

        observe(viewModel.userLocation) {
            if (locationRetriever.keepTrackOfUser)
                map.animateCameraAt(it)
        }
    }

    private fun onPoiClickListener(poi: PointOfInterest) {
        placesClient.fetchPlace(
            FetchPlaceRequest.newInstance(poi.placeId, placeFields)

        ).addOnCompleteListener {
            if (!it.isSuccessful)
                return@addOnCompleteListener

            it.result?.place?.let {
                viewModel.setSelectedPlace(it.toPlace())
            }
        }
    }

    private fun onMapLongClickListener(latLng: LatLng) {
        val newPlace = selectedPlace.copy(
            location = Location(latLng.latitude, latLng.longitude),
            _importance = selectedPlace.importance
        )
        viewModel.setSelectedPlace(newPlace)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.select_path_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.save_selected_place -> {
                findNavController().navigate(toEditPlaceFragment(selectedPlace))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}


