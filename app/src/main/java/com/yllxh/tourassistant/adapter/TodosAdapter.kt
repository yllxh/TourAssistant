package com.yllxh.tourassistant.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yllxh.tourassistant.data.source.local.database.entity.ToDo
import com.yllxh.tourassistant.databinding.TodoListItemBinding

class TodosAdapter(
    private val onItemClickListener: (ToDo) -> Unit
) : ListAdapter<ToDo, TodosAdapter.ViewHolder>(ToDoDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClickListener)
    }

    class ViewHolder private constructor(private val binding: TodoListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            toDo: ToDo,
            onItemClickListener: (ToDo) -> Unit
        ) {
            binding.todo = toDo
            binding.root.setOnClickListener {
                onItemClickListener(toDo)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TodoListItemBinding.inflate(layoutInflater, parent, false)
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