package com.sartimau.openpayfit.domain.service

import com.sartimau.openpayfit.domain.entity.Movie
import com.sartimau.openpayfit.domain.entity.PopularPerson
import com.sartimau.openpayfit.domain.utils.CoroutineResult

interface TheMovieDBService {
    suspend fun getPopularPersonList(): CoroutineResult<List<PopularPerson>>

    suspend fun getPopularMovieList(): CoroutineResult<List<Movie>>
    suspend fun getTopRatedMovieList(): CoroutineResult<List<Movie>>
    suspend fun getRecommendedMoviesListById(movieId: Int): CoroutineResult<List<Movie>>
}
