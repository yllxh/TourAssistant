package com.yllxh.tourassistant.data.source.local.database.entity.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.yllxh.tourassistant.data.source.local.database.entity.Path
import com.yllxh.tourassistant.data.source.local.database.entity.Place
import com.yllxh.tourassistant.data.source.local.database.entity.crossreference.PathPlaceCrossRef

data class PlaceWithPaths(
    @Embedded val place: Place,
    @Relation(
        parentColumn = "placeId",
        entityColumn = "pathId",
        associateBy = Junction(PathPlaceCrossRef::class)
    )
    val paths: List<Path>
)