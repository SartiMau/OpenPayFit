package com.sartimau.openpayfit.domain.usecase

import com.sartimau.openpayfit.domain.database.TheMovieDBRepository
import com.sartimau.openpayfit.domain.entity.PopularPerson
import com.sartimau.openpayfit.domain.service.TheMovieDBService
import com.sartimau.openpayfit.domain.utils.CoroutineResult
import javax.inject.Inject

interface GetPopularPersonListUseCase {
    suspend operator fun invoke(): CoroutineResult<List<PopularPerson>>
}

class GetPopularPersonListUseCaseImpl @Inject constructor(
    private val theMovieDBService: TheMovieDBService,
    private val theMovieDBRepository: TheMovieDBRepository
) : GetPopularPersonListUseCase {
    override suspend operator fun invoke(): CoroutineResult<List<PopularPerson>> {
        return when (val serviceResult = theMovieDBService.getPopularPersonList()) {
            is CoroutineResult.Success -> {
                theMovieDBRepository.insertPopularPersonsToDB(serviceResult.data)
                theMovieDBRepository.getDBPopularPersons()
            }
            is CoroutineResult.Failure -> {
                theMovieDBRepository.getDBPopularPersons()
            }
        }
    }
}
