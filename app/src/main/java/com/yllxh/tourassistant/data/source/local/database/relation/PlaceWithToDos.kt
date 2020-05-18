package com.yllxh.tourassistant.data.source.local.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.yllxh.tourassistant.data.source.local.database.entity.PlaceDB
import com.yllxh.tourassistant.data.source.local.database.entity.ToDoDB


data class PlaceWithToDos(
    @Embedded val placeDB: PlaceDB,
    @Relation(
        parentColumn = "placeId",
        entityColumn = "placeUsedId"
    )
    val toDos: List<ToDoDB>
)