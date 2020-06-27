package com.yllxh.tourassistant.screens.selectplaces

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.yllxh.tourassistant.R
import com.yllxh.tourassistant.adapter.SimplePlacesAdapter
import com.yllxh.tourassistant.databinding.FragmentSelectPlacesBinding
import com.yllxh.tourassistant.utils.observe
import com.yllxh.tourassistant.screens.selectplaces.SelectPlacesFragmentDirections.actionSelectPlacesFragmentToEditPathFragment as toEditPathFragment

class SelectPlacesFragment : Fragment() {
    private lateinit var binding: FragmentSelectPlacesBinding
    private val path by lazy {
        SelectPlacesFragmentArgs.fromBundle(requireArguments()).path
    }
    private val simplePlacesAdapter by lazy {
        SimplePlacesAdapter(usesItemSelection = true).apply {
            setSelectedItems(viewModel.selectedPlaces ?: path.places)
            if (viewModel.selectedPlaces == null) {
                viewModel.setSelectedItems(path.places)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.setSelectedItems(simplePlacesAdapter.getSelectedItems())
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(SelectPlacesViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectPlacesBinding.inflate(inflater, container, false)
        binding.recyclerView.adapter = simplePlacesAdapter

        observe(viewModel.places, simplePlacesAdapter::submitList)

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.save_selected_place -> {
                val selectedPlaces = simplePlacesAdapter.getSelectedItems()

                findNavController().navigate(toEditPathFragment(path.copy(places = selectedPlaces)))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.select_path_menu, menu)
    }
}
