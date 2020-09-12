package com.yllxh.tourassistant.screens.addtodo

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yllxh.tourassistant.data.source.local.database.entity.ToDo
import java.lang.IllegalArgumentException

class AddToDoViewModelFactory (
    private val toDo: ToDo,
    private val app: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddToDoViewModel::class.java)) {
            return AddToDoViewModel(toDo, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}