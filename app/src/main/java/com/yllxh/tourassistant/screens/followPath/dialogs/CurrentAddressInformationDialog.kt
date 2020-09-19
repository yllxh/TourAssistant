package com.yllxh.tourassistant.screens.followPath.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.yllxh.tourassistant.data.source.local.database.entity.Place
import com.yllxh.tourassistant.databinding.DialogCurrentAddressInformationBinding

class CurrentAddressInformationDialog private constructor() : DialogFragment() {
    private lateinit var binding: DialogCurrentAddressInformationBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        binding = DialogCurrentAddressInformationBinding.inflate(
            LayoutInflater.from(requireContext()),
            null,
            false
        )

        binding.place = requireArguments()[PLACE_KEY] as Place

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
            .also(this::onCreate)
    }

    private fun onCreate(dialog: AlertDialog){
        binding.dismiss.setOnClickListener {
            dialog.cancel()
        }
    }

    companion object {
        private const val PLACE_KEY = "PLACE_KEY"
        const val TAG: String = "CurrentAddressInformationDialogTAG"

        fun newInstance(place: Place): CurrentAddressInformationDialog {

            return CurrentAddressInformationDialog().apply {
                arguments = Bundle().apply {

                    putParcelable(PLACE_KEY, place)
                }
            }
        }
    }

}