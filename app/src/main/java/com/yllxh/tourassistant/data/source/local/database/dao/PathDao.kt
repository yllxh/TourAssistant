package com.yllxh.tourassistant.data.source.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yllxh.tourassistant.data.source.local.database.entity.Path
import com.yllxh.tourassistant.data.source.local.database.entity.crossreference.PathPlaceCrossRef
import com.yllxh.tourassistant.data.source.local.database.relation.PathWithPlaces

@Dao
interface PathDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPath(path: Path): Long

//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    fun insertAllPaths(paths: List<Path>)

    @Query("SELECT * FROM path_table")
    fun getAllPaths(): LiveData<List<Path>>

    @Transaction
    @Query("SELECT * FROM path_table")
    fun getAllPathsWithPlaces(): LiveData<List<PathWithPlaces>>


    @Transaction
    @Query("SELECT * FROM path_table WHERE pathId = :pathId")
    fun getPath(pathId: Long): LiveData<PathWithPlaces>

    @Update
    fun updatePath(path: Path)
//
//    @Update
//    fun updateAllPaths(paths: List<Path>)
//
//    @Delete
//    fun deletePath(path: Path)
//
//    @Delete
//    fun deleteAllPaths(paths: List<Path>)
//
//    @Transaction
//    @Query("SELECT * FROM path_table WHERE pathId = :pathId")
//    fun getPathWithPlaces(pathId: Long): LiveData<PathWithPlaces>
//
//    @Update
//    fun updatePathWithPlaces(path: Path, places: List<Place>)
}