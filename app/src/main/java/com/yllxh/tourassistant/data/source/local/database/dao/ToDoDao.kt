package com.yllxh.tourassistant.data.source.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yllxh.tourassistant.data.source.local.database.entity.ToDo

@Dao
interface ToDoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertToDo(toDo: ToDo)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllToDo(toDos: List<ToDo>)

    @Query("SELECT * FROM todo_table WHERE todoId = :todoId")
    fun getToDo(todoId: Long): LiveData<ToDo>

    @Query("SELECT * FROM todo_table")
    fun getAllToDos(): LiveData<List<ToDo>>

    @Update
    fun updateToDo(toDo: ToDo)

    @Delete
    fun deleteToDo(toDo: ToDo)


}