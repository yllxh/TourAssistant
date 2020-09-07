package com.yllxh.tourassistant.data.source.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.yllxh.tourassistant.data.source.local.database.entity.crossreference.PathPlaceCrossRef

@Dao
interface CrossReferenceDao {

    @Delete
    fun removeCrossRefs(crossRef: List<PathPlaceCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCrossRefs(crossRefs: List<PathPlaceCrossRef>)
}

