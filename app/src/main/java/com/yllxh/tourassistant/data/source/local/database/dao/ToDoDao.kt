package com.yllxh.tourassistant.data.source.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yllxh.tourassistant.data.source.local.database.entity.ToDoDB

@Dao
interface ToDoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertToDo(toDo: ToDoDB)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllToDo(toDos: List<ToDoDB>)

    @Query("SELECT * FROM todo_table WHERE todoId = :todoId")
    fun getToDo(todoId: Long): LiveData<ToDoDB>

    @Update
    fun updateToDo(toDo: ToDoDB)

    @Delete
    fun deleteToDo(toDo: ToDoDB)


}