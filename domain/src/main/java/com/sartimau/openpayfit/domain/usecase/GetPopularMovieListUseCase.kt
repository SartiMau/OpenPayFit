package com.sartimau.openpayfit.domain.usecase

import com.sartimau.openpayfit.domain.database.TheMovieDBRepository
import com.sartimau.openpayfit.domain.entity.Movie
import com.sartimau.openpayfit.domain.service.TheMovieDBService
import com.sartimau.openpayfit.domain.utils.CoroutineResult
import javax.inject.Inject

interface GetPopularMovieListUseCase {
    suspend operator fun invoke(): CoroutineResult<List<Movie>>
}

class GetPopularMovieListUseCaseImpl @Inject constructor(
    private val theMovieDBService: TheMovieDBService,
    private val theMovieDBRepository: TheMovieDBRepository
) : GetPopularMovieListUseCase {
    override suspend operator fun invoke(): CoroutineResult<List<Movie>> {
        return when (val serviceResult = theMovieDBService.getPopularMovieList()) {
            is CoroutineResult.Success -> {
                theMovieDBRepository.insertPopularMovieToDB(serviceResult.data)
                theMovieDBRepository.getDBPopularMovie()
            }
            is CoroutineResult.Failure -> {
                theMovieDBRepository.getDBPopularMovie()
            }
        }
    }
}
