package com.sartimau.openpayfit.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.sartimau.openpayfit.data.database.entity.RecommendedMovieEntity
import com.sartimau.openpayfit.data.database.entity.RecommendedMovieEntityComplete

@Dao
interface RecommendedMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecommendedMovie(recommendedMovieEntity: RecommendedMovieEntity)

    @Transaction
    @Query("SELECT * FROM recommended_movie WHERE originalMovieId = :movieId")
    fun getDBRecommendedMoviesById(movieId: Int): List<RecommendedMovieEntityComplete>
}
