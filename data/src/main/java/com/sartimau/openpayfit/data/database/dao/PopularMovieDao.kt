package com.sartimau.openpayfit.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.sartimau.openpayfit.data.database.entity.PopularMovieEntity
import com.sartimau.openpayfit.data.database.entity.PopularMovieEntityComplete

@Dao
interface PopularMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPopularMovie(popularMovieEntity: PopularMovieEntity)

    @Transaction
    @Query("SELECT * FROM popular_movie")
    fun getDBPopularMovies(): List<PopularMovieEntityComplete>
}
