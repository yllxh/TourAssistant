package com.yllxh.tourassistant.screens.editpatth

import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider

import com.yllxh.tourassistant.adapter.SimplePlacesAdapter
import com.yllxh.tourassistant.data.source.local.database.entity.Path
import com.yllxh.tourassistant.data.source.local.database.entity.Place
import com.yllxh.tourassistant.data.source.local.database.relation.PathWithPlaces
import com.yllxh.tourassistant.databinding.DialogSelectPlacesBinding
import com.yllxh.tourassistant.utils.observe

class SelectPlacesDialog : DialogFragment() {
    private lateinit var binding: DialogSelectPlacesBinding
    private val selectedPlaces by lazy {
        (requireArguments().getParcelable(KEY) as PathWithPlaces?)!!.path.places
    }
    private val simplePlacesAdapter by lazy {
        SimplePlacesAdapter(usesItemSelection = true)
            .apply {
                setSelectedItems(selectedPlaces)
            }
    }
    private val viewModel by lazy {
        ViewModelProvider(this).get(SelectPlacesDialogViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogSelectPlacesBinding.inflate(inflater)

        binding.recyclerView.adapter = simplePlacesAdapter
        binding.addSelectedButton.setOnClickListener {
            val pathWithPlaces = PathWithPlaces(places = simplePlacesAdapter.getSelectedItems())

            simplePlacesAdapter.onDoneUsingSelection()
            targetFragment?.onActivityResult(
                SELECT_PLACES_DIALOG_REQUEST,
                RESULT_OK,
                Intent().apply {
                    putExtra(EXTRA_KEY, pathWithPlaces)
                }
            )
            dialog?.dismiss()
        }

        observe(viewModel.places, simplePlacesAdapter::submitList)

        return binding.root
    }


    companion object {
        private const val KEY = "SelectPlacesDialog"
        private const val EXTRA_KEY = "EXTRAS_KEY"
        const val TAG: String = KEY
        const val SELECT_PLACES_DIALOG_REQUEST = 101

        fun newInstance(fragment: Fragment, alreadySelected: List<Place>?): SelectPlacesDialog {
            val pathWithPlaces = PathWithPlaces(places = alreadySelected ?: listOf())
            return SelectPlacesDialog().apply {
                setTargetFragment(fragment, SELECT_PLACES_DIALOG_REQUEST)
                arguments = Bundle().apply { putParcelable(KEY, pathWithPlaces) }
            }

        }

        fun extractData(intent: Intent?): List<Place> {
            return (intent?.extras?.get(EXTRA_KEY) as PathWithPlaces).path.places
        }
    }

}
