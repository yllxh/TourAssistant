package com.yllxh.tourassistant.screens.editplace

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.yllxh.tourassistant.R

import com.yllxh.tourassistant.data.source.local.database.entity.PlaceDB
import com.yllxh.tourassistant.databinding.FragmentEditPlaceBinding
import com.yllxh.tourassistant.screens.editplace.EditPlaceFragmentDirections.actionEditPlaceFragmentToMainFragment as toMainFragment
import com.yllxh.tourassistant.screens.editplace.EditPlaceFragmentDirections.actionEditPlaceFragmentToSelectPlaceFragment2 as toSelectPlaceFragment

class EditPlaceFragment : Fragment() {

    private lateinit var binding: FragmentEditPlaceBinding
    private val place by lazy {
        EditPlaceFragmentArgs.fromBundle(requireArguments()).place
    }

    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(EditPlaceViewModel::class.java)
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

        binding.place = place
        binding.editLatLng.setOnClickListener {
            findNavController().navigate(toSelectPlaceFragment(place))
        }
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_place_menu_item -> {
                viewModel.saveChangesToPlace(extractPlaceInfoFromLayout())
                findNavController().navigate(toMainFragment())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit_place_menu, menu)
    }

    private fun extractPlaceInfoFromLayout(): PlaceDB {
        with(binding) {
            return PlaceDB(place!!.placeId)
                .also {
                    it.name = placeNameEditText.text.toString()
                    it.importance = importanceEditText.text
                        .toString()
                        .toInt()
                        .let {return@let if (it < 1) 1 else it}
                    it.location.city = cityEditText.text.toString()
                    it.location.country = countryEditText.text.toString()
                    it.location.latitude = latEditText.text.toString().toDouble()
                    it.location.longitude = lngEditText.text.toString().toDouble()
                }
        }
    }
}
