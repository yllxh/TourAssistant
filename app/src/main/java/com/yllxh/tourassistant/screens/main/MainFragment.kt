package com.yllxh.tourassistant.screens.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.yllxh.tourassistant.adapter.MainFragmentTabAdapter

import com.yllxh.tourassistant.databinding.FragmentMainBinding
import com.yllxh.tourassistant.utils.AppPreferences.Companion.getLastSelectedTab
import com.yllxh.tourassistant.utils.AppPreferences.Companion.setLastSelectedTab
import com.yllxh.tourassistant.utils.onTabSelected
import com.yllxh.tourassistant.utils.selectTabAt

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.apply {
            pager.adapter = MainFragmentTabAdapter(this@MainFragment)
            TabLayoutMediator(tabLayout, pager) { tab, position ->
                tab.text = MainFragmentTabAdapter.getTabName(position)
            }.attach()
            tabLayout.selectTabAt(requireContext().getLastSelectedTab())
            tabLayout.onTabSelected { requireContext().setLastSelectedTab(it) }
        }

        return binding.root
    }
}