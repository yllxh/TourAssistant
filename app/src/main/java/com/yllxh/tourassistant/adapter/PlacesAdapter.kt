package com.yllxh.tourassistant.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yllxh.tourassistant.data.source.local.database.entity.Place
import com.yllxh.tourassistant.databinding.PlacesListItemBinding

class PlacesAdapter(private val onClickListener: (Place) -> Unit)
    : ListAdapter<Place, PlacesAdapter.ViewHolder>(PlaceDiffCallback()) {

    override fun submitList(list: List<Place>?) {
        super.submitList(list?.toMutableList())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onClickListener)
    }

    class ViewHolder private constructor(private val binding: PlacesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            place: Place,
            onClickListener: (Place) -> Unit
        ) {
            binding.place = place
            binding.root.setOnClickListener { onClickListener(place) }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PlacesListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}

class PlaceDiffCallback : DiffUtil.ItemCallback<Place>() {
    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem.placeId == newItem.placeId
    }

    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem == newItem
    }

}