package com.yllxh.tourassistant.screens.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.yllxh.tourassistant.adapter.PathsAdapter
import com.yllxh.tourassistant.data.source.local.database.entity.Path
import com.yllxh.tourassistant.databinding.FragmentPathsTabBinding
import com.yllxh.tourassistant.utils.observe
import com.yllxh.tourassistant.screens.main.MainFragmentDirections.actionMainFragmentToEditPathFragment as toEditPathFragment

class PathsTabFragment : Fragment() {
    private lateinit var binding: FragmentPathsTabBinding
    private val pathsAdapter by lazy {
        PathsAdapter {
            findNavController().navigate(toEditPathFragment(it))
        }
    }
    private val viewModel by lazy {
        ViewModelProvider(requireParentFragment()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPathsTabBinding.inflate(inflater)
        binding.pathsRecycleView.adapter = pathsAdapter
        binding.fab.setOnClickListener(::addNewPath)

        observe(viewModel.paths, pathsAdapter::submitList)

        return binding.root
    }

    private fun addNewPath(v: View) {
        findNavController().navigate(toEditPathFragment(Path()))
    }

}
