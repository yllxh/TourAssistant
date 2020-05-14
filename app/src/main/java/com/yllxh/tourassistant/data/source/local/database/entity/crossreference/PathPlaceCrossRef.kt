package com.yllxh.tourassistant.data.source.local.database.entity.crossreference

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["pathId", "placeId"])
data class PathPlaceCrossRef(
    @ColumnInfo(index = true)
    val pathId: Long,
    @ColumnInfo(index = true)
    val placeId: Long
)