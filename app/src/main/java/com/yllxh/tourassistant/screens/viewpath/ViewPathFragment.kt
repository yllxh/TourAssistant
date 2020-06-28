package com.yllxh.tourassistant.screens.viewpath

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.yllxh.tourassistant.R
import com.yllxh.tourassistant.adapter.SimplePlacesAdapter
import com.yllxh.tourassistant.databinding.FragmentViewPathBinding
import com.yllxh.tourassistant.utils.observe
import com.yllxh.tourassistant.screens.viewpath.ViewPathFragmentDirections.actionViewPathFragmentToEditPathFragment as toEditPathFragment

class ViewPathFragment : Fragment() {
    private lateinit var binding: FragmentViewPathBinding

    private val viewModel by lazy {
        val path = ViewPathFragmentArgs.fromBundle(requireArguments()).path
        val factory = ViewPathViewModelFactory(path, requireActivity().application)
        ViewModelProvider(this, factory).get(ViewPathViewModel::class.java)
    }

    private val simplePlacesAdapter by lazy { SimplePlacesAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewPathBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.simplePlacesRecycleView.adapter = simplePlacesAdapter

        observe(viewModel.path) { simplePlacesAdapter.submitList(it.places) }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.view_path_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_path_menu_item -> {
                findNavController().navigate(toEditPathFragment(viewModel.path.value!!))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}