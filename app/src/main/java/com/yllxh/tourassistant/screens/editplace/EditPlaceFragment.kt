package com.yllxh.tourassistant.screens.editplace

import android.os.Bundle
import android.view.*
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.yllxh.tourassistant.R
import com.yllxh.tourassistant.adapter.TodosAdapter

import com.yllxh.tourassistant.data.source.local.database.entity.Place
import com.yllxh.tourassistant.data.source.local.database.entity.ToDo
import com.yllxh.tourassistant.databinding.FragmentEditPlaceBinding
import com.yllxh.tourassistant.utils.observe
import com.yllxh.tourassistant.screens.editplace.EditPlaceFragmentDirections.actionEditPlaceFragmentToAddTodoFragment as toAddTodoFragment
import com.yllxh.tourassistant.screens.editplace.EditPlaceFragmentDirections.actionEditPlaceFragmentToAddTodoFragment as toAddTodoFragment
import com.yllxh.tourassistant.screens.editplace.EditPlaceFragmentDirections.actionEditPlaceFragmentToSelectPlaceFragment2 as toSelectPlaceFragment

class EditPlaceFragment : Fragment() {

    private lateinit var binding: FragmentEditPlaceBinding
    private val selectedPlace: Place by lazy {
        EditPlaceFragmentArgs.fromBundle(requireArguments()).place
    }

    private val viewModel by lazy {
        val factory = EditPlaceViewModelFactory(selectedPlace, requireActivity().application)
        ViewModelProvider(this, factory).get(EditPlaceViewModel::class.java)
    }
    private val adapter by lazy {
        TodosAdapter { findNavController().navigate(toAddTodoFragment(it)) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditPlaceBinding.inflate(inflater)

        binding.place = selectedPlace
        binding.editLatLng.setOnClickListener {
            findNavController().navigate(toSelectPlaceFragment(extractPlaceInfoFromLayout()))
        }
        binding.addTodoButton.setOnClickListener {
            findNavController().navigate(toAddTodoFragment(ToDo(placeUsedId = selectedPlace.placeId)))
        }
        binding.placeTodoRecycleView.adapter = adapter

        observe(viewModel.todos, adapter::submitList)
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_place_menu_item -> {
                viewModel.saveChangesToPlace(extractPlaceInfoFromLayout())
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit_place_menu, menu)
    }

    private fun extractPlaceInfoFromLayout(): Place = with(binding) {
        return Place(selectedPlace.placeId,
            placeNameEditText.text.toString(),
            importanceSeekBar.progress,
            selectedPlace.location.also {
                it.city = cityEditText.text.toString()
                it.country = countryEditText.text.toString()
            })
    }

}
