package com.sartimau.openpayfit.domain.database

import com.sartimau.openpayfit.domain.entity.PopularPerson
import com.sartimau.openpayfit.domain.utils.CoroutineResult

interface TheMovieDBRepository {
    suspend fun insertPopularPersonsToDB(popularPersons: List<PopularPerson>)
    suspend fun getDBPopularPersons(): CoroutineResult<List<PopularPerson>>
}
