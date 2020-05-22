package com.yllxh.tourassistant.data.source.local.database.relation

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.yllxh.tourassistant.data.source.local.database.entity.Path
import com.yllxh.tourassistant.data.source.local.database.entity.Place
import com.yllxh.tourassistant.data.source.local.database.entity.crossreference.PathPlaceCrossRef
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PathWithPlaces(
    @Embedded val path: Path = Path(),
    @Relation(
        parentColumn = "pathId",
        entityColumn = "placeId",
        associateBy = Junction(PathPlaceCrossRef::class)
    )
    private val places: List<Place> = listOf()
) : Parcelable {
    init {
        path.places = places
    }
}