package com.yllxh.tourassistant.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yllxh.tourassistant.screens.main.MainFragment
import com.yllxh.tourassistant.screens.main.MainViewModel
import com.yllxh.tourassistant.screens.main.PathsTabFragment
import com.yllxh.tourassistant.screens.main.PlacesTabFragment
import java.lang.IllegalArgumentException

class MainFragmentTabAdapter(
    fragment: MainFragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            PATHS -> PathsTabFragment()
            PLACES -> PlacesTabFragment()
            else -> throw IllegalArgumentException("There is not fragment for tab $position")
        }
    }

    companion object {
        private const val PATHS = 0
        private const val PLACES = 1
        private val tabs = mapOf(
            PATHS to "Paths",
            PLACES to "Places"
        )

        fun getTabName(position: Int): String {
            if (!tabs.containsKey(position))
                throw IllegalArgumentException("The is no name for the tab at position $position")

            return tabs[position] ?: error("")
        }
    }
}