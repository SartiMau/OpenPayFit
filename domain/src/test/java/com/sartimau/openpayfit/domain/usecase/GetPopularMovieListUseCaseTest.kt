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
class GetPopularMovieListUseCaseTest {

    @MockK
    private lateinit var theMovieDBService: TheMovieDBService
    @MockK
    private lateinit var theMovieDBRepository: TheMovieDBRepository

    private lateinit var getPopularMovieListUseCase: GetPopularMovieListUseCase

    @Before
    fun init() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        MockKAnnotations.init(this, relaxUnitFun = true)

        getPopularMovieListUseCase = GetPopularMovieListUseCaseImpl(theMovieDBService, theMovieDBRepository)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `if use case returns success`() {
        runTest(UnconfinedTestDispatcher()) {
            val movieList = listOf<Movie>()

            coEvery { theMovieDBService.getPopularMovieList() } returns CoroutineResult.Success(movieList)
            coEvery { theMovieDBRepository.getDBPopularMovie() } returns CoroutineResult.Success(movieList)

            val result = getPopularMovieListUseCase()

            coVerify { theMovieDBService.getPopularMovieList() }
            coVerify { theMovieDBRepository.insertPopularMovieToDB(movieList) }
            coVerify { theMovieDBRepository.getDBPopularMovie() }

            assertEquals(movieList, (result as CoroutineResult.Success).data)
        }
    }

    @Test
    fun `if use case returns fail`() {
        runTest(UnconfinedTestDispatcher()) {
            val movieList = listOf<Movie>()
            val exception = Exception()

            coEvery { theMovieDBService.getPopularMovieList() } returns CoroutineResult.Failure(exception)
            coEvery { theMovieDBRepository.getDBPopularMovie() } returns CoroutineResult.Success(movieList)

            val result = getPopularMovieListUseCase()

            coVerify { theMovieDBService.getPopularMovieList() }
            coVerify { theMovieDBRepository.getDBPopularMovie() }

            assertEquals(movieList, (result as CoroutineResult.Success).data)
        }
    }
}
