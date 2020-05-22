package com.yllxh.tourassistant.utils

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Extension function for the [LifecycleOwner] class, to allow them to
 * observe [LiveData] with a more readable syntax.
 *
 * @param liveData          The LiveData which is to be observed.
 * @param block             The block of code to be run each time the LiveData is updated.
 */
fun <T> Fragment.observe(
    liveData: LiveData<T>,
    block: (T) -> Unit
) {
    liveData.observe(viewLifecycleOwner, Observer(block))
}

fun View.setBackGroundColorTo(resId: Int){
    setBackgroundColor(ContextCompat.getColor(context, resId))
}

fun String.isEmptyOrBlank(): Boolean {
    return isEmpty() || isBlank()
}