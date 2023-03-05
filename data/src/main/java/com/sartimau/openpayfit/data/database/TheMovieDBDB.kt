package com.sartimau.openpayfit.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sartimau.openpayfit.data.database.dao.KnownForDao
import com.sartimau.openpayfit.data.database.dao.PopularMovieDao
import com.sartimau.openpayfit.data.database.dao.PopularPersonDao
import com.sartimau.openpayfit.data.database.dao.RecommendedMovieDao
import com.sartimau.openpayfit.data.database.dao.TopRatedMovieDao
import com.sartimau.openpayfit.data.database.entity.KnownForEntity
import com.sartimau.openpayfit.data.database.entity.MovieEntity
import com.sartimau.openpayfit.data.database.entity.PopularMovieEntity
import com.sartimau.openpayfit.data.database.entity.PopularPersonEntity
import com.sartimau.openpayfit.data.database.entity.RecommendedMovieEntity
import com.sartimau.openpayfit.data.database.entity.TopRatedMovieEntity

@Database(
    entities = [
        PopularPersonEntity::class,
        KnownForEntity::class,
        MovieEntity::class,
        PopularMovieEntity::class,
        TopRatedMovieEntity::class,
        RecommendedMovieEntity::class
    ],
    version = 1
)
abstract class TheMovieDBDB : RoomDatabase() {
    abstract fun popularPersonDao(): PopularPersonDao
    abstract fun knownForDao(): KnownForDao
    abstract fun popularMovieDao(): PopularMovieDao
    abstract fun topRatedMovieDao(): TopRatedMovieDao
    abstract fun recommendedMovieDao(): RecommendedMovieDao
}
