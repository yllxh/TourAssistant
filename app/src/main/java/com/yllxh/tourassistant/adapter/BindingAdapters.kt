package com.yllxh.tourassistant.adapter

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yllxh.tourassistant.R
import com.yllxh.tourassistant.data.source.local.database.entity.Path
import com.yllxh.tourassistant.data.source.local.database.entity.Place
import com.yllxh.tourassistant.data.source.local.database.relation.PathWithPlaces
import java.lang.IllegalArgumentException

@BindingAdapter("lat_lng_of_place")
fun TextView.setLatLngOfPlace(place: Place?) {
    place?.let {
        val latitude = place.location.latLng.latitude
        val longitude = place.location.latLng.longitude
        text = resources.getString(R.string.place_lat_lng_template, latitude, longitude)
    }
}

@BindingAdapter("view_visibility")
fun View.setViewVisibility(isVisible: Boolean?) {
    if (isVisible == null) return

    visibility = if (isVisible)
        View.VISIBLE
    else
        View.GONE

}
