package com.yllxh.tourassistant.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yllxh.tourassistant.R
import com.yllxh.tourassistant.data.source.local.database.entity.Path
import com.yllxh.tourassistant.databinding.ItemPathListBinding

class PathsAdapter(
    private val onItemClickListener: (Path) -> Unit
) : ListAdapter<Path, PathsAdapter.ViewHolder>(PathDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClickListener)
    }

    class ViewHolder private constructor(private val binding: ItemPathListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            path: Path,
            onItemClickListener: (Path) -> Unit
        ) {
            binding.path = path
            binding.root.setOnClickListener {
                onItemClickListener(path)
            }

            val resources = binding.root.context.resources
            val hideText = resources.getString(R.string.show_less)
            val showText = resources.getString(R.string.show_all)

            val simplePlacesAdapter = SimplePlacesAdapter(true).also { it.submitList(path.places) }

            binding.simplePlacesRecycleView.adapter = simplePlacesAdapter
            binding.showOrHidePlaces.text =
                if(simplePlacesAdapter.isCollapsed) showText else hideText

            binding.showOrHidePlaces.setOnClickListener {
                val textView = (it as TextView)

                textView.text = if (textView.text == hideText) {
                    simplePlacesAdapter.hideItems()
                    showText
                } else {
                    simplePlacesAdapter.showAllItems()
                    hideText
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemPathListBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

private class PathDiffCallback : DiffUtil.ItemCallback<Path>() {
    override fun areItemsTheSame(oldItem: Path, newItem: Path): Boolean {
        return oldItem.pathId == newItem.pathId
    }

    override fun areContentsTheSame(oldItem: Path, newItem: Path): Boolean {
        return oldItem == newItem
    }
}