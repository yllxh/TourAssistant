package com.yllxh.tourassistant.data.source.local.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "path_table")
data class Path(
    @PrimaryKey(autoGenerate = true)
    var pathId: Long = 0,
    var name: String = "N/A",
    @Ignore
    var places: List<Place> = emptyList()
) : Parcelable