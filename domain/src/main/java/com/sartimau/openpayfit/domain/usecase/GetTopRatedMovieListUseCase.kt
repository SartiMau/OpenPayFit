package com.sartimau.openpayfit.domain.usecase

import com.sartimau.openpayfit.domain.database.TheMovieDBRepository
import com.sartimau.openpayfit.domain.entity.Movie
import com.sartimau.openpayfit.domain.service.TheMovieDBService
import com.sartimau.openpayfit.domain.utils.CoroutineResult
import javax.inject.Inject

interface GetTopRatedMovieListUseCase {
    suspend operator fun invoke(): CoroutineResult<List<Movie>>
}

class GetTopRatedMovieListUseCaseImpl @Inject constructor(
    private val theMovieDBService: TheMovieDBService,
    private val theMovieDBRepository: TheMovieDBRepository
) : GetTopRatedMovieListUseCase {
    override suspend operator fun invoke(): CoroutineResult<List<Movie>> {
        return when (val serviceResult = theMovieDBService.getTopRatedMovieList()) {
            is CoroutineResult.Success -> {
                theMovieDBRepository.insertTopRatedMovieToDB(serviceResult.data)
                theMovieDBRepository.getDBTopRatedMovie()
            }
            is CoroutineResult.Failure -> {
                theMovieDBRepository.getDBTopRatedMovie()
            }
        }
    }
}
