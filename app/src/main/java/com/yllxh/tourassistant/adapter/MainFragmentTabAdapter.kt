package com.yllxh.tourassistant.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yllxh.tourassistant.screens.main.*
import java.lang.IllegalArgumentException

class MainFragmentTabAdapter(
    fragment: MainFragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            PATHS -> PathsTabFragment()
            PLACES -> PlacesTabFragment()
            TODOS -> TodoTabFragment()
            else -> throw IllegalArgumentException("There is not fragment for tab $position")
        }
    }

    companion object {
        private const val PATHS = 0
        private const val PLACES = 1
        private const val TODOS = 2
        private val tabs = mapOf(
            PATHS to "Paths",
            PLACES to "Places",
            TODOS to "To Do"
        )

        fun getTabName(position: Int): String {
            if (!tabs.containsKey(position))
                throw IllegalArgumentException("The is no name for the tab at position $position")

            return tabs[position] ?: error("")
        }
    }
}