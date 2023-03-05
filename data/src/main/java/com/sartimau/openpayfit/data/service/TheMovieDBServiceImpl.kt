package com.sartimau.openpayfit.data.service

import com.sartimau.openpayfit.data.mapper.mapToLocalMovieList
import com.sartimau.openpayfit.data.mapper.mapToLocalPopularPersonList
import com.sartimau.openpayfit.data.service.api.TheMovieDBApi
import com.sartimau.openpayfit.domain.entity.Movie
import com.sartimau.openpayfit.domain.entity.PopularPerson
import com.sartimau.openpayfit.domain.service.TheMovieDBService
import com.sartimau.openpayfit.domain.utils.CoroutineResult
import javax.inject.Inject

class TheMovieDBServiceImpl @Inject constructor(private val theMovieDBApi: TheMovieDBApi) : TheMovieDBService {

    override suspend fun getPopularPersonList(): CoroutineResult<List<PopularPerson>> {
        try {
            val callResponse = theMovieDBApi.getPopularPersonList()
            val response = callResponse.execute()
            if (response.isSuccessful) response.body()?.let {
                return CoroutineResult.Success(it.mapToLocalPopularPersonList())
            }
        } catch (e: Exception) {
            return CoroutineResult.Failure(Exception())
        }
        return CoroutineResult.Failure(Exception())
    }

    override suspend fun getPopularMovieList(): CoroutineResult<List<Movie>> {
        try {
            val callResponse = theMovieDBApi.getPopularMovieList()
            val response = callResponse.execute()
            if (response.isSuccessful) response.body()?.let {
                return CoroutineResult.Success(it.mapToLocalMovieList())
            }
        } catch (e: Exception) {
            return CoroutineResult.Failure(Exception())
        }
        return CoroutineResult.Failure(Exception())
    }

    override suspend fun getTopRatedMovieList(): CoroutineResult<List<Movie>> {
        try {
            val callResponse = theMovieDBApi.getTopRatedMovieList()
            val response = callResponse.execute()
            if (response.isSuccessful) response.body()?.let {
                return CoroutineResult.Success(it.mapToLocalMovieList())
            }
        } catch (e: Exception) {
            return CoroutineResult.Failure(Exception())
        }
        return CoroutineResult.Failure(Exception())
    }

    override suspend fun getRecommendedMoviesListById(movieId: Int): CoroutineResult<List<Movie>> {
        try {
            val callResponse = theMovieDBApi.getRecommendedMoviesListById(movieId)
            val response = callResponse.execute()
            if (response.isSuccessful) response.body()?.let {
                return CoroutineResult.Success(it.mapToLocalMovieList())
            }
        } catch (e: Exception) {
            return CoroutineResult.Failure(Exception())
        }
        return CoroutineResult.Failure(Exception())
    }
}
