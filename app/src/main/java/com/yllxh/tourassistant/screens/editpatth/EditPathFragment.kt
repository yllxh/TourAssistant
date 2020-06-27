package com.yllxh.tourassistant.screens.editpatth

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
import com.yllxh.tourassistant.screens.editpatth.EditPathFragmentDirections.actionEditPathFragmentToSelectPlacesFragment as toSelectPlacesFragment

class EditPathFragment : Fragment() {
    private lateinit var binding: FragmentEditPathBinding
    private val simplePlacesAdapter by lazy { SimplePlacesAdapter() }

    private val selectedPath by lazy {
        EditPathFragmentArgs.fromBundle(requireArguments()).path
    }
    private val viewModel by lazy {
        val factory = EditPathViewModelFactory(selectedPath, requireActivity().application)

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
        binding = FragmentEditPathBinding.inflate(inflater, container, false)
        binding.simplePlacesRecycleView.adapter = simplePlacesAdapter
        binding.viewModel = viewModel

        binding.addRemovePalcesButton.setOnClickListener{
            findNavController().navigate(toSelectPlacesFragment(selectedPath))
        }

        observe(viewModel.path) { simplePlacesAdapter.submitList(it.places) }

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit_path_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_path_menu_item -> {
                val editedPath = extractPathInfoFromLayout()
                viewModel.savePath(editedPath)
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun extractPathInfoFromLayout(): Path {
        return selectedPath.apply {
            name = binding.placeNameEditText.text.toString()
        }
    }

}
