package com.yllxh.tourassistant.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yllxh.tourassistant.R
import com.yllxh.tourassistant.data.source.local.database.entity.Place
import com.yllxh.tourassistant.databinding.PlacesListItemBinding
import com.yllxh.tourassistant.utils.toggleStrings

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
            binding.apply {
                this.place = place
                root.setOnClickListener { onClickListener(place) }
                placeToDoRecycleView.adapter = TodosAdapter().apply { submitList(place.toDos) }
                showToDosButton.setOnClickListener {
                    val context = root.context
                    val currentText = showToDosButton.text.toString()
                    val visibility = placeToDoRecycleView.visibility
                    placeToDoRecycleView.visibility = when (visibility) {
                        View.GONE -> View.VISIBLE
                        else -> View.GONE
                    }
                    showToDosButton.text = currentText.toggleStrings(
                        context.getString(R.string.hide_toDos),
                        context.getString(R.string.show_todos)
                    )
                }
            }
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