package com.yllxh.tourassistant.adapter

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleObserver
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yllxh.tourassistant.R
import com.yllxh.tourassistant.data.source.local.database.entity.Place
import com.yllxh.tourassistant.databinding.ItemSimplePlaceListBinding
import com.yllxh.tourassistant.utils.setBackGroundColorTo

private const val MIN_ITEMS_COUNT = 3

class SimplePlacesAdapter(
    private val usesItemCount: Boolean = false,
    private val usesItemSelection: Boolean = false,
    private val onItemClickListener: (Place) -> Unit = {}
) : ListAdapter<Place, SimplePlacesAdapter.ViewHolder>(PlaceDiffCallback()), LifecycleObserver {

    private var count = MIN_ITEMS_COUNT
    private val realCount get() = currentList.size
    private var _isCollapsed: Boolean = usesItemCount
    val isCollapsed get() = _isCollapsed

    private var selectedPlaces = mutableListOf<Place>()

    override fun getItemCount(): Int {
        return when {
            usesItemCount && super.getItemCount() > MIN_ITEMS_COUNT -> count
            else -> super.getItemCount()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClickListener)
    }

    fun hideItems() {
        if (!usesItemCount) return

        count = MIN_ITEMS_COUNT
        _isCollapsed = true
        notifyDataSetChanged()
    }

    fun showAllItems() {
        if (!usesItemCount) return

        count = realCount
        _isCollapsed = false
        notifyDataSetChanged()
    }

    fun setSelectedItems(selectedPlaces: List<Place>) {
        this.selectedPlaces = selectedPlaces.toMutableList()
        notifyDataSetChanged()
    }

    fun getSelectedItems(): List<Place> {
        return selectedPlaces
    }

    fun from(parent: ViewGroup): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSimplePlaceListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemSimplePlaceListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val defaultColor = (binding.root.background as ColorDrawable?)?.color ?: ColorDrawable().color
        fun bind(
            place: Place,
            onItemClickListener: (Place) -> Unit
        ) {
            binding.place = place
            if (usesItemSelection) {
                binding.placeName.colorView(selectedPlaces.contains(place))
                binding.root.colorView(selectedPlaces.contains(place))
            }
            binding.placeName.setOnClickListener {
                onItemClickListener(place)
                if (!usesItemSelection)
                    return@setOnClickListener

                val isSelected = place in selectedPlaces
                if (isSelected) {
                    selectedPlaces.remove(place)
                } else {
                    selectedPlaces.add(place)
                }

                binding.root.colorView(!isSelected)
                it.colorView(!isSelected)
            }

        }

        private fun View.colorView(selected: Boolean?) {
            selected ?: return

            val color = if (selected) {
                R.color.colorSelectedItem
            } else {
                defaultColor
            }
            setBackGroundColorTo(color)
        }

    }
}