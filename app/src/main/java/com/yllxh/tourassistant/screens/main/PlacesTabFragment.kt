package com.yllxh.tourassistant.screens.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.yllxh.tourassistant.adapter.PlacesAdapter
import com.yllxh.tourassistant.data.source.local.database.entity.Place
import com.yllxh.tourassistant.databinding.FragmentPlacesTabBinding
import com.yllxh.tourassistant.utils.observe
import com.yllxh.tourassistant.screens.main.MainFragmentDirections.actionMainFragmentToEditPlaceFragment as toEditPlaceFragment
import com.yllxh.tourassistant.screens.main.MainFragmentDirections.actionMainFragmentToSelectPlaceFragment as toSelectPlaceFragment

class PlacesTabFragment : Fragment() {

    private lateinit var binding: FragmentPlacesTabBinding

    private val viewModel by lazy {
        ViewModelProvider(requireParentFragment()).get(MainViewModel::class.java)
    }

    private val placesAdapter by lazy {
        PlacesAdapter {
            findNavController().navigate(toEditPlaceFragment(it))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlacesTabBinding.inflate(inflater)
        binding.placesRecycleView.adapter = placesAdapter
        binding.fab.setOnClickListener {
            findNavController().navigate(toSelectPlaceFragment(Place()))
        }

        observe(viewModel.places, placesAdapter::submitList)

        return binding.root
    }
}

