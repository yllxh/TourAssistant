package com.yllxh.tourassistant.screens.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = MainRepository(app.applicationContext)

    val paths = repository.paths
    val places = repository.places
    val toDos = repository.toDos
}