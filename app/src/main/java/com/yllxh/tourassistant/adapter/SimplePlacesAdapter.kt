package com.yllxh.tourassistant.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleObserver
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yllxh.tourassistant.R
import com.yllxh.tourassistant.data.source.local.database.entity.Place
import com.yllxh.tourassistant.databinding.SimplePlaceListItemBinding
import com.yllxh.tourassistant.utils.setBackGroundColorTo

private const val MIN_ITEMS_COUNT = 3

class SimplePlacesAdapter(
    private val usesItemCount: Boolean = false,
    usesItemSelection: Boolean = false,
    private val onItemClickListener: (Place) -> Unit = {}
) : ListAdapter<Place, SimplePlacesAdapter.ViewHolder>(PlaceDiffCallback()), LifecycleObserver {
    init {
        if (usesItemSelection) {
            ViewHolder.selectedItems = mutableListOf()
        }
    }

    private var count = MIN_ITEMS_COUNT
    private val realCount get() = currentList.size

    private var _isCollapsed: Boolean = usesItemCount
    val isCollapsed get() = _isCollapsed

    override fun getItemCount(): Int {
        return if (usesItemCount && super.getItemCount() > MIN_ITEMS_COUNT) {
            count
        } else {
            super.getItemCount()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
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

    fun getSelectedItems(): List<Place> {
        return ViewHolder.selectedItems!!.toList()
    }

    fun setSelectedItems(selectedPlaces: List<Place>) {
        ViewHolder.selectedItems?.addAll(selectedPlaces)
    }

    fun onDoneUsingSelection() {
        ViewHolder.selectedItems = null
    }


    class ViewHolder private constructor(private val binding: SimplePlaceListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            place: Place,
            onItemClickListener: (Place) -> Unit
        ) {
            binding.place = place
            binding.placeName.setOnClickListener {
                onItemClickListener(place)

                val items = selectedItems ?: return@setOnClickListener

                val isSelected = items.contains(place)
                if (isSelected) {
                    selectedItems?.remove(place)
                } else {
                    selectedItems?.add(place)
                }
                it.colorView(!isSelected)
            }

            binding.placeName.colorView(selectedItems?.contains(place))
        }

        private fun View.colorView(selected: Boolean?) {
            selected ?: return

            val color = if (selected) {
                R.color.selectedItemColor
            } else {
                R.color.nonSelectedItemColor
            }

            setBackGroundColorTo(color)
        }


        companion object {
            var selectedItems: MutableList<Place>? = null
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SimplePlaceListItemBinding.inflate(layoutInflater)
                return ViewHolder(binding)
            }
        }

    }

}