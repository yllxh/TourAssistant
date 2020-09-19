package com.yllxh.tourassistant.data.source.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yllxh.tourassistant.data.source.local.database.dao.CrossReferenceDao
import com.yllxh.tourassistant.data.source.local.database.dao.PathDao
import com.yllxh.tourassistant.data.source.local.database.dao.PlaceDao
import com.yllxh.tourassistant.data.source.local.database.dao.ToDoDao
import com.yllxh.tourassistant.data.source.local.database.entity.Path
import com.yllxh.tourassistant.data.source.local.database.entity.Place
import com.yllxh.tourassistant.data.source.local.database.entity.ToDo
import com.yllxh.tourassistant.data.source.local.database.entity.crossreference.PathPlaceCrossRef

@Database(
    entities = [
        Path::class,
        Place::class,
        ToDo::class,
        PathPlaceCrossRef::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract val pathDao: PathDao
    abstract val placeDao: PlaceDao
    abstract val toDoDao: ToDoDao
    abstract val crossRefDao: CrossReferenceDao


    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "DATABASE_NAME"
                    )
                        .addMigrations(migration_1_2)
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }

        private val migration_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE place_table ADD COLUMN isPrimary INTEGER NOT NULL DEFAULT 1"
                )
            }
        }
    }
}