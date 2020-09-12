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
    GoogleApi_Place.Field.BUSINESS_STATUS,
    GoogleApi_Place.Field.ID,
    GoogleApi_Place.Field.LAT_LNG,
    GoogleApi_Place.Field.NAME,
    GoogleApi_Place.Field.PHOTO_METADATAS,
    GoogleApi_Place.Field.PLUS_CODE,
    GoogleApi_Place.Field.TYPES,
    GoogleApi_Place.Field.UTC_OFFSET,
    GoogleApi_Place.Field.VIEWPORT
//    GoogleApi_Place.Field.WEBSITE_URI,
//    GoogleApi_Place.Field.RATING,
//    GoogleApi_Place.Field.USER_RATINGS_TOTAL,
//    GoogleApi_Place.Field.PHONE_NUMBER,
//    GoogleApi_Place.Field.OPENING_HOURS,
)

class SelectPlaceMapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private lateinit var autoComplete: AutocompleteSupportFragment
    private lateinit var placesClient: PlacesClient
    private var marker: Marker? = null

    private lateinit var locationRetriever: LocationRetriever

    private lateinit var binding: FragmentSelectPlaceBinding
    private val viewModel by lazy {
        val place = SelectPlaceMapFragmentArgs.fromBundle(requireArguments()).selectedPlace
        val factory = SelectPlaceMapViewModelFactory(place, requireActivity().application)
        ViewModelProvider(this, factory).get(SelectPlaceMapViewModel::class.java)
    }
    private val selectedPlace: Place get() = viewModel.selectedPlace.value!!

    private val onAutoCompletePlaceSelected = object : PlaceSelectionListener {
        override fun onPlaceSelected(apiPlace: GoogleApi_Place) {
            viewModel.setSelectedPlace(apiPlace.toPlace())
        }

        override fun onError(status: Status) {
            toast("Place could not be retrieved")
        }
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
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        (childFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment)
            .getMapAsync(this)

        autoComplete = (childFragmentManager
            .findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment)
            .apply { setPlaceFields(placeFields) }

        autoComplete.setOnPlaceSelectedListener(onAutoCompletePlaceSelected)


        binding.trackUserLocationButton.setOnClickListener(this::onToggleTrackUserLocation)

        observe(viewModel.userLocation, this::onUserLocationUpdated)
        observe(viewModel.selectedPlace, this::onSelectedPlaceUpdated)
        observe(viewModel.isTrackingUser, this::onIsTrackingUserUpdated)

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

    private fun onSelectedPlaceUpdated(place: Place) {
        if (!::map.isInitialized || place.location.isNotValid())
            return

        viewModel.setTrackUserLocation(false)

        marker?.remove()
        marker = map.addSimpleMarker(place)
        map.animateCameraAt(place)


        if (!place.location.addressAsString.isBlank()) {
            marker!!.title = place.location.addressAsString
        }
    }

    private fun onToggleTrackUserLocation(v: View) {
        viewModel.setTrackUserLocation(!locationRetriever.isTrackingUser)
    }

    private fun onIsTrackingUserUpdated(isTracking: Boolean) {
        if (::locationRetriever.isInitialized)
            locationRetriever.setTrackUserLocation(isTracking)

        binding.trackUserLocationButton.setColor(
            if (isTracking)
                R.color.colorTrackingUser
            else
                R.color.colorStoppedTrackingUser
        )
    }

    private fun onUserLocationUpdated(userLocation: LatLng?) {
        userLocation ?: return

        if (locationRetriever.isTrackingUser) {
            map.animateCameraAt(userLocation)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {

        locationRetriever = LocationRetriever(
            this,
            onLocationReceived = viewModel::updateUserLocation,
            onMissingPermission = ::onMissingLocationPermission
        )
        viewModel.setTrackUserLocation(locationRetriever.isTrackingUser)

        map = googleMap.apply {
            uiSettings.isMyLocationButtonEnabled = false
            val hasLocationPermission = hasLocationPermission()

            isMyLocationEnabled = hasLocationPermission
            binding.trackUserLocationButton.isEnabled = hasLocationPermission

            if (!hasLocationPermission) {
                onMissingLocationPermission()
            }

            if (selectedPlace.placeId != 0L
                && selectedPlace.location.isValid()) {

                marker = addSimpleMarker(selectedPlace)
                animateCameraAt(selectedPlace)
            } else {
                viewModel.setTrackUserLocation(true)
            }

            setOnMapLongClickListener(this@SelectPlaceMapFragment::onMapLongClickListener)
            setOnPoiClickListener(this@SelectPlaceMapFragment::onPoiClickListener)
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


