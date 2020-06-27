package com.yllxh.tourassistant.data.source.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yllxh.tourassistant.data.source.local.database.entity.Place
import com.yllxh.tourassistant.data.source.local.database.entity.relation.PlaceWithToDos


@Dao
interface PlaceDao {

    @Insert
    fun insertPlace(place: Place): Long

    @Query("SELECT * FROM place_table ORDER BY placeId DESC")
    fun getAllPlaces(): LiveData<List<Place>>

    @Update
    fun updatePlace(place: Place)
//
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    fun insertAllPlaces(places: List<Place>)
//
    @Transaction
    @Query("SELECT * FROM place_table ORDER BY placeId DESC")
    fun getAllPlacesWithToDos(): LiveData<List<PlaceWithToDos>>
//
//    @Transaction
//    @Query("SELECT * FROM place_table WHERE placeId = :placeId")
//    fun getPlaceWithToDos(placeId: Long): LiveData<PlaceWithToDos>
//
//    @Transaction
//    @Query("SELECT * FROM place_table")
//    fun getAllPlacesWithPaths(): LiveData<List<PlaceWithPaths>>
//
//    @Transaction
//    @Query("SELECT * FROM place_table WHERE placeId = :placeId")
//    fun getPlaceWithPaths(placeId: Long): LiveData<PlaceWithPaths>
//
//
//    @Query("SELECT * FROM place_table WHERE placeId = :placeId")
//    fun getPlace(placeId: Long): LiveData<Place>
//
//
//    @Update
//    fun updateAllPlaces(places: List<Place>)
//
//    @Delete
//    fun deletePlace(place: Place)
//
//    @Delete
//    fun deleteAllPlaces(place: List<Place>)
}