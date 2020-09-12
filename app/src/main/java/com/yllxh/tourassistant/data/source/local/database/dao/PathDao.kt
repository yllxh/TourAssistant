package com.yllxh.tourassistant.data.source.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yllxh.tourassistant.data.source.local.database.entity.Path
import com.yllxh.tourassistant.data.source.local.database.entity.relation.PathWithPlaces

@Dao
interface PathDao {

    @Insert
    fun insertPath(path: Path): Long

    @Query("SELECT * FROM path_table ORDER BY pathId DESC")
    fun getAllPaths(): LiveData<List<Path>>

    @Transaction
    @Query("SELECT * FROM path_table ORDER BY pathId DESC")
    fun getAllPathsWithPlaces(): LiveData<List<PathWithPlaces>>

//
//    @Transaction
//    @Query("SELECT * FROM path_table WHERE pathId = :pathId")
//    fun getPath(pathId: Long): LiveData<PathWithPlaces>

    @Update
    fun updatePath(path: Path)

    @Query("SELECT * FROM path_table WHERE pathId = :pathId")
    fun getPath(pathId: Long): LiveData<PathWithPlaces>

    @Query("SELECT * FROM path_table WHERE pathId = :pathId")
    fun getRawPath(pathId: Long): PathWithPlaces

//    @Transaction
//    @Query("SELECT * FROM path_table WHERE pathId = :pathId")

    @Delete
    fun deletePath(path: Path)
}