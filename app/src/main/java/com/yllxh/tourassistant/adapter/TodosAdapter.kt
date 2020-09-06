package com.yllxh.tourassistant.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yllxh.tourassistant.data.source.local.database.entity.ToDo
import com.yllxh.tourassistant.databinding.ItemTodoListBinding

class TodosAdapter(
    private val isNested: Boolean = false,
    private val onItemClickListener: (ToDo) -> Unit = {}
) : ListAdapter<ToDo, TodosAdapter.ViewHolder>(ToDoDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), isNested, onItemClickListener)
    }

    class ViewHolder private constructor(private val binding: ItemTodoListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            toDo: ToDo,
            isInNestedRecycleView: Boolean,
            onItemClickListener: (ToDo) -> Unit
        ) {
            binding.isInNestedRecycleView = isInNestedRecycleView
            binding.toDo = toDo
            binding.root.setOnClickListener {
                onItemClickListener(toDo)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemTodoListBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

private class ToDoDiffCallback : DiffUtil.ItemCallback<ToDo>() {
    override fun areItemsTheSame(oldItem: ToDo, newItem: ToDo): Boolean {
        return oldItem.todoId == newItem.todoId
    }

    override fun areContentsTheSame(oldItem: ToDo, newItem: ToDo): Boolean {
        return oldItem == newItem
    }
}