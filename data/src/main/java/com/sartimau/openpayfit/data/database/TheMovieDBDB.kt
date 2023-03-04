package com.sartimau.openpayfit.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sartimau.openpayfit.data.database.dao.KnownForDao
import com.sartimau.openpayfit.data.database.dao.PopularPersonDao
import com.sartimau.openpayfit.data.database.entity.KnownForEntity
import com.sartimau.openpayfit.data.database.entity.PopularPersonEntity

@Database(
    entities = [
        PopularPersonEntity::class,
        KnownForEntity::class
    ],
    version = 1
)
abstract class TheMovieDBDB : RoomDatabase() {
    abstract fun popularPersonDao(): PopularPersonDao
    abstract fun knownForDao(): KnownForDao
}
