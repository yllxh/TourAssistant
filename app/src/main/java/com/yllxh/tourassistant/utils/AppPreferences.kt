package com.yllxh.tourassistant.utils

import android.content.Context
import androidx.preference.PreferenceManager

class AppPreferences private constructor() {

    companion object {

        private const val LAST_SELECTED_TAB: String = "LAST_SELECTED_TAB"
        private const val DEFAULT_SELECTED_TAB: Int = 0


        fun Context.getLastSelectedTab(): Int {
            PreferenceManager.getDefaultSharedPreferences(this).apply {

                if (contains(LAST_SELECTED_TAB)) {
                    return getInt(LAST_SELECTED_TAB, DEFAULT_SELECTED_TAB)
                }

                edit().apply {
                    putInt(LAST_SELECTED_TAB, DEFAULT_SELECTED_TAB)
                    apply()
                }
                return DEFAULT_SELECTED_TAB
            }
        }

        fun Context.setLastSelectedTab(position: Int) {
            PreferenceManager.getDefaultSharedPreferences(this).apply {
                edit().apply {
                    putInt(LAST_SELECTED_TAB, position)
                    apply()
                }
            }
        }
    }
}