package com.sartimau.openpayfit.domain.service

import com.sartimau.openpayfit.domain.entity.PopularPerson
import com.sartimau.openpayfit.domain.utils.CoroutineResult

interface TheMovieDBService {
    suspend fun getPopularPersonList(): CoroutineResult<List<PopularPerson>>
}
