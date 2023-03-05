package com.sartimau.openpayfit.data.database

import com.sartimau.openpayfit.data.database.dao.KnownForDao
import com.sartimau.openpayfit.data.database.dao.PopularMovieDao
import com.sartimau.openpayfit.data.database.dao.PopularPersonDao
import com.sartimau.openpayfit.data.database.dao.RecommendedMovieDao
import com.sartimau.openpayfit.data.database.dao.TopRatedMovieDao
import com.sartimau.openpayfit.data.mapper.mapToDataBaseKnownFor
import com.sartimau.openpayfit.data.mapper.mapToDataBasePopularMovie
import com.sartimau.openpayfit.data.mapper.mapToDataBasePopularPerson
import com.sartimau.openpayfit.data.mapper.mapToDataBaseRecommendedMovie
import com.sartimau.openpayfit.data.mapper.mapToDataBaseTopRatedMovie
import com.sartimau.openpayfit.data.mapper.mapToLocalPopularMovieList
import com.sartimau.openpayfit.data.mapper.mapToLocalPopularPersonList
import com.sartimau.openpayfit.data.mapper.mapToLocalRecommendedMovieList
import com.sartimau.openpayfit.data.mapper.mapToLocalTopRatedMovieList
import com.sartimau.openpayfit.domain.database.TheMovieDBRepository
import com.sartimau.openpayfit.domain.entity.Movie
import com.sartimau.openpayfit.domain.entity.PopularPerson
import com.sartimau.openpayfit.domain.utils.CoroutineResult

class TheMovieDBRepositoryImpl(
    private val popularPersonDao: PopularPersonDao,
    private val knownForDao: KnownForDao,
    private val popularMovieDao: PopularMovieDao,
    private val topRatedMovieDao: TopRatedMovieDao,
    private val recommendedMovieDao: RecommendedMovieDao,
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

    override suspend fun insertPopularMovieToDB(movies: List<Movie>) {
        movies.forEach { movie ->
            popularMovieDao.insertPopularMovie(movie.mapToDataBasePopularMovie())
        }
    }

    override suspend fun getDBPopularMovie(): CoroutineResult<List<Movie>> =
        popularMovieDao.getDBPopularMovies().let {
            if (it.isNotEmpty()) {
                CoroutineResult.Success(it.mapToLocalPopularMovieList())
            } else {
                CoroutineResult.Failure(Exception())
            }
        }

    override suspend fun insertTopRatedMovieToDB(movies: List<Movie>) {
        movies.forEach { movie ->
            topRatedMovieDao.insertTopRatedMovie(movie.mapToDataBaseTopRatedMovie())
        }
    }

    override suspend fun getDBTopRatedMovie(): CoroutineResult<List<Movie>> =
        topRatedMovieDao.getDBTopRatedMovies().let {
            if (it.isNotEmpty()) {
                CoroutineResult.Success(it.mapToLocalTopRatedMovieList())
            } else {
                CoroutineResult.Failure(Exception())
            }
        }

    override suspend fun insertRecommendedMoviesToDB(movieId: Int, movies: List<Movie>) {
        movies.forEach { movie ->
            recommendedMovieDao.insertRecommendedMovie(movie.mapToDataBaseRecommendedMovie(movieId))
        }
    }

    override suspend fun getDBGetRecommendedMoviesById(movieId: Int): CoroutineResult<List<Movie>> =
        recommendedMovieDao.getDBRecommendedMoviesById(movieId).let {
            if (it.isNotEmpty()) {
                CoroutineResult.Success(it.mapToLocalRecommendedMovieList())
            } else {
                CoroutineResult.Failure(Exception())
            }
        }
}
