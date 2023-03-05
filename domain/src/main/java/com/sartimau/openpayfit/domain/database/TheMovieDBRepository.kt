package com.sartimau.openpayfit.domain.database

import com.sartimau.openpayfit.domain.entity.Movie
import com.sartimau.openpayfit.domain.entity.PopularPerson
import com.sartimau.openpayfit.domain.utils.CoroutineResult

interface TheMovieDBRepository {
    suspend fun insertPopularPersonsToDB(popularPersons: List<PopularPerson>)
    suspend fun getDBPopularPersons(): CoroutineResult<List<PopularPerson>>

    suspend fun insertPopularMovieToDB(movies: List<Movie>)
    suspend fun getDBPopularMovie(): CoroutineResult<List<Movie>>

    suspend fun insertTopRatedMovieToDB(movies: List<Movie>)
    suspend fun getDBTopRatedMovie(): CoroutineResult<List<Movie>>

    suspend fun insertRecommendedMoviesToDB(movieId: Int, movies: List<Movie>)
    suspend fun getDBGetRecommendedMoviesById(movieId: Int): CoroutineResult<List<Movie>>
}
