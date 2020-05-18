package com.yllxh.tourassistant.data.source.local.database.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.yllxh.tourassistant.data.source.local.database.entity.PathDB
import com.yllxh.tourassistant.data.source.local.database.entity.PlaceDB
import com.yllxh.tourassistant.data.source.local.database.entity.crossreference.PathPlaceCrossRef

data class PlaceWithPaths(
    @Embedded val placeDB: PlaceDB,
    @Relation(
        parentColumn = "placeId",
        entityColumn = "pathId",
        associateBy = Junction(PathPlaceCrossRef::class)
    )
    val paths: List<PathDB>
)