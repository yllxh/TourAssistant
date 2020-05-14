package com.yllxh.tourassistant.screens.editpatth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.yllxh.tourassistant.R
import com.yllxh.tourassistant.adapter.SimplePlacesAdapter
import com.yllxh.tourassistant.data.source.local.database.entity.Path
import com.yllxh.tourassistant.databinding.FragmentEditPathBinding
import com.yllxh.tourassistant.utils.observe

class EditPathFragment : Fragment() {
    private lateinit var binding: FragmentEditPathBinding
    private val adapter by lazy { SimplePlacesAdapter() }
    private val path by lazy {
        EditPathFragmentArgs.fromBundle(requireArguments()).path
    }
    private val viewModel by lazy {
        val factory = EditPathViewModelFactory(path, requireActivity().application)

        ViewModelProvider(this, factory).get(EditPathViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditPathBinding.inflate(inflater)
        binding.simplePlacesRecycleView.adapter = adapter
        binding.viewModel = viewModel

        binding.addRemovePalcesButton.setOnClickListener(::onAddRemovePlaces)

        observe(viewModel.path) { adapter.submitList(it.places) }

        return binding.root
    }

    private fun onAddRemovePlaces(v: View) {
        SelectPlacesDialog.newInstance(this, viewModel.path.value?.places)
            .show(parentFragmentManager, SelectPlacesDialog.TAG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK
            && requestCode == SelectPlacesDialog.SELECT_PLACES_DIALOG_REQUEST
        ) {
            val selectedPlaces = SelectPlacesDialog.extractData(data)
            viewModel.setSelectedPlaces(selectedPlaces)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit_path_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_path_menu_item -> {
                val editedPath = extractPathInfoFromLayout()
                viewModel.saveChangesToPath(editedPath)
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun extractPathInfoFromLayout(): Path {
        return Path().apply {
            name = binding.placeNameEditText.text.toString()
        }
    }

}
