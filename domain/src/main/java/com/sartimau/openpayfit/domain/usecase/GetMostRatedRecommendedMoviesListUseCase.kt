package com.sartimau.openpayfit.domain.usecase

import com.sartimau.openpayfit.domain.database.TheMovieDBRepository
import com.sartimau.openpayfit.domain.entity.Movie
import com.sartimau.openpayfit.domain.service.TheMovieDBService
import com.sartimau.openpayfit.domain.utils.CoroutineResult
import javax.inject.Inject

interface GetRecommendedMoviesListByIdUseCase {
    suspend operator fun invoke(movieId: Int): CoroutineResult<List<Movie>>
}

class GetRecommendedMoviesListByIdUseCaseImpl @Inject constructor(
    private val theMovieDBService: TheMovieDBService,
    private val theMovieDBRepository: TheMovieDBRepository
) : GetRecommendedMoviesListByIdUseCase {
    override suspend operator fun invoke(movieId: Int): CoroutineResult<List<Movie>> {
        return when (val serviceResult = theMovieDBService.getRecommendedMoviesListById(movieId)) {
            is CoroutineResult.Success -> {
                theMovieDBRepository.insertRecommendedMoviesToDB(movieId, serviceResult.data)
                theMovieDBRepository.getDBGetRecommendedMoviesById(movieId)
            }
            is CoroutineResult.Failure -> {
                theMovieDBRepository.getDBGetRecommendedMoviesById(movieId)
            }
        }
    }
}
