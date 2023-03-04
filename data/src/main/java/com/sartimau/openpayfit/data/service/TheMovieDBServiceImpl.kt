package com.sartimau.openpayfit.data.service

import com.sartimau.openpayfit.data.mapper.mapToLocalPopularPersonList
import com.sartimau.openpayfit.data.service.api.TheMovieDBApi
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
}
