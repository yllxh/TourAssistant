package com.yllxh.tourassistant.screens.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.yllxh.tourassistant.adapter.TodosAdapter
import com.yllxh.tourassistant.data.source.local.database.entity.ToDo
import com.yllxh.tourassistant.databinding.FragmentTodoTabBinding
import com.yllxh.tourassistant.utils.observe
import com.yllxh.tourassistant.screens.main.MainFragmentDirections.actionMainFragmentToAddTodoFragment as toAddTodoFragment

class TodoTabFragment : Fragment() {
    private lateinit var binding: FragmentTodoTabBinding
    private val viewModel by lazy {
        ViewModelProvider(requireParentFragment()).get(MainViewModel::class.java)
    }
    private val adapter by lazy {
        TodosAdapter {
            findNavController().navigate(toAddTodoFragment(it))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoTabBinding.inflate(inflater, container, false)
        binding.todosRecycleView.adapter = adapter
        binding.fab.setOnClickListener {
            findNavController().navigate(toAddTodoFragment(ToDo()))
        }

        observe(viewModel.toDos, adapter::submitList)
        return binding.root
    }

}
