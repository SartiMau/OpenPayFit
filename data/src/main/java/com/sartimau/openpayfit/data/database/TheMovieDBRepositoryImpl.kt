package com.sartimau.openpayfit.data.database

import com.sartimau.openpayfit.data.database.dao.KnownForDao
import com.sartimau.openpayfit.data.database.dao.PopularPersonDao
import com.sartimau.openpayfit.data.mapper.mapToDataBaseKnownFor
import com.sartimau.openpayfit.data.mapper.mapToDataBasePopularPerson
import com.sartimau.openpayfit.data.mapper.mapToLocalPopularPersonList
import com.sartimau.openpayfit.domain.database.TheMovieDBRepository
import com.sartimau.openpayfit.domain.entity.PopularPerson
import com.sartimau.openpayfit.domain.utils.CoroutineResult

class TheMovieDBRepositoryImpl(
    private val popularPersonDao: PopularPersonDao,
    private val knownForDao: KnownForDao
) : TheMovieDBRepository {

    override suspend fun insertPopularPersonsToDB(popularPersons: List<PopularPerson>) {
        popularPersons.forEach { popularPerson ->
            popularPersonDao.insertPopularPerson(popularPerson.mapToDataBasePopularPerson())

            popularPerson.knownFor.forEach {
                knownForDao.insertKnownFor(it.mapToDataBaseKnownFor(popularPerson.id))
            }
        }
    }

    override suspend fun getDBPopularPersons(): CoroutineResult<List<PopularPerson>> =
        popularPersonDao.getDBPopularPersons().let {
            if (it.isNotEmpty()) {
                CoroutineResult.Success(it.mapToLocalPopularPersonList())
            } else {
                CoroutineResult.Failure(Exception())
            }
        }
}
