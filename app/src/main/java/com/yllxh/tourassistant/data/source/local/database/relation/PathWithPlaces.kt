package com.yllxh.tourassistant.data.source.local.database.relation

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.yllxh.tourassistant.data.source.local.database.entity.PathDB
import com.yllxh.tourassistant.data.source.local.database.entity.PlaceDB
import com.yllxh.tourassistant.data.source.local.database.entity.crossreference.PathPlaceCrossRef
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PathWithPlaces(
    @Embedded val path: PathDB = PathDB(),
    @Relation(
        parentColumn = "pathId",
        entityColumn = "placeId",
        associateBy = Junction(PathPlaceCrossRef::class)
    )
    private val places: List<PlaceDB> = listOf()
) : Parcelable {
    init {
        path.places = places
    }
}