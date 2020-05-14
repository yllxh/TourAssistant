package com.yllxh.tourassistant.data.source.local.database.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yllxh.tourassistant.data.model.Location
import kotlinx.android.parcel.Parcelize
import java.lang.IllegalArgumentException

@Parcelize
@Entity(tableName = "place_table")
data class Place(
    @PrimaryKey(autoGenerate = true)
    val placeId: Long = 0,
    var name: String = "N/A",
    private var _importance: Int = 1,
    @Embedded val location: Location = Location()
): Parcelable {

    var importance: Int
    get() = _importance
    set(value) {
        if (value < 1 || value > 10)
            throw IllegalArgumentException("Importance is $value, but should be between 1 and 10.")

        _importance = value
    }
}