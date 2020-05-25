package com.yllxh.tourassistant.data.source.local.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.lang.IllegalArgumentException

@Parcelize
@Entity(tableName = "todo_table")
data class ToDo(
    @PrimaryKey(autoGenerate = true)
    val todoId: Long = 0,
    val placeUsedId: Long = 0,
    var note: String = "N/A",
    private var _importance: Int = 5
) : Parcelable {

    var importance : Int
        get() = _importance
        set(value) {
            if (value > 10 || value < 1)
                throw IllegalArgumentException("Importance should be between 1 and 10.")

            _importance = value
        }
}
