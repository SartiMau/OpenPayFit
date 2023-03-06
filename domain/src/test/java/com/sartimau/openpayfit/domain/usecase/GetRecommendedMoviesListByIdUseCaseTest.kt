package com.sartimau.openpayfit.domain.usecase

import com.sartimau.openpayfit.domain.database.TheMovieDBRepository
import com.sartimau.openpayfit.domain.entity.Movie
import com.sartimau.openpayfit.domain.service.TheMovieDBService
import com.sartimau.openpayfit.domain.utils.CoroutineResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class GetRecommendedMoviesListByIdUseCaseTest {

    @MockK
    private lateinit var theMovieDBService: TheMovieDBService
    @MockK
    private lateinit var theMovieDBRepository: TheMovieDBRepository

    private lateinit var getRecommendedMoviesListByIdUseCase: GetRecommendedMoviesListByIdUseCase

    @Before
    fun init() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        MockKAnnotations.init(this, relaxUnitFun = true)

        getRecommendedMoviesListByIdUseCase = GetRecommendedMoviesListByIdUseCaseImpl(theMovieDBService, theMovieDBRepository)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `if use case returns success`() {
        runTest(UnconfinedTestDispatcher()) {
            val movieList = listOf<Movie>()

            coEvery { theMovieDBService.getRecommendedMoviesListById(MOVIE_ID) } returns CoroutineResult.Success(movieList)
            coEvery { theMovieDBRepository.getDBGetRecommendedMoviesById(MOVIE_ID) } returns CoroutineResult.Success(movieList)

            val result = getRecommendedMoviesListByIdUseCase(MOVIE_ID)

            coVerify { theMovieDBService.getRecommendedMoviesListById(MOVIE_ID) }
            coVerify { theMovieDBRepository.insertRecommendedMoviesToDB(MOVIE_ID, movieList) }
            coVerify { theMovieDBRepository.getDBGetRecommendedMoviesById(MOVIE_ID) }

            assertEquals(movieList, (result as CoroutineResult.Success).data)
        }
    }

    @Test
    fun `if use case returns fail`() {
        runTest(UnconfinedTestDispatcher()) {
            val movieList = listOf<Movie>()
            val exception = Exception()

            coEvery { theMovieDBService.getRecommendedMoviesListById(MOVIE_ID) } returns CoroutineResult.Failure(exception)
            coEvery { theMovieDBRepository.getDBGetRecommendedMoviesById(MOVIE_ID) } returns CoroutineResult.Success(movieList)

            val result = getRecommendedMoviesListByIdUseCase(MOVIE_ID)

            coVerify { theMovieDBService.getRecommendedMoviesListById(MOVIE_ID) }
            coVerify { theMovieDBRepository.getDBGetRecommendedMoviesById(MOVIE_ID) }

            assertEquals(movieList, (result as CoroutineResult.Success).data)
        }
    }

    companion object {
        private const val MOVIE_ID = 9292
    }
}
