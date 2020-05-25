package com.yllxh.tourassistant.screens.addtodo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yllxh.tourassistant.data.source.local.database.AppDatabase
import com.yllxh.tourassistant.data.source.local.database.entity.ToDo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddToDoViewModel(app: Application) : AndroidViewModel(app) {
    private val toDoDao = AppDatabase.getInstance(getApplication()).toDoDao
    private lateinit var todo: ToDo

    fun setImportance(importance: Int) {
        todo.importance = importance
    }

    fun setNote(note: String) {
        todo.note = note
    }

    fun save() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            if (todo.todoId == 0L) {
                toDoDao.insertToDo(todo)
            } else {
                toDoDao.updateToDo(todo)
            }
        }
    }

    fun setTodo(todo: ToDo) {
        this.todo = todo
    }


}