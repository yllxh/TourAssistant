package com.yllxh.tourassistant.data.source.local.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.yllxh.tourassistant.data.source.local.database.entity.Place
import com.yllxh.tourassistant.data.source.local.database.entity.ToDo


data class PlaceWithToDos(
    @Embedded val place: Place,
    @Relation(
        parentColumn = "placeId",
        entityColumn = "placeUsedId"
    )
    val toDos: List<ToDo>
)