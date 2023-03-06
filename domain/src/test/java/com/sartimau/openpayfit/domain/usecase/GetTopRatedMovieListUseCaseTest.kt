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
class GetTopRatedMovieListUseCaseTest {

    @MockK
    private lateinit var theMovieDBService: TheMovieDBService
    @MockK
    private lateinit var theMovieDBRepository: TheMovieDBRepository

    private lateinit var getTopRatedMovieListUseCase: GetTopRatedMovieListUseCase

    @Before
    fun init() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        MockKAnnotations.init(this, relaxUnitFun = true)

        getTopRatedMovieListUseCase = GetTopRatedMovieListUseCaseImpl(theMovieDBService, theMovieDBRepository)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `if use case returns success`() {
        runTest(UnconfinedTestDispatcher()) {
            val movieList = listOf<Movie>()

            coEvery { theMovieDBService.getTopRatedMovieList() } returns CoroutineResult.Success(movieList)
            coEvery { theMovieDBRepository.getDBTopRatedMovie() } returns CoroutineResult.Success(movieList)

            val result = getTopRatedMovieListUseCase()

            coVerify { theMovieDBService.getTopRatedMovieList() }
            coVerify { theMovieDBRepository.insertTopRatedMovieToDB(movieList) }
            coVerify { theMovieDBRepository.getDBTopRatedMovie() }

            assertEquals(movieList, (result as CoroutineResult.Success).data)
        }
    }

    @Test
    fun `if use case returns fail`() {
        runTest(UnconfinedTestDispatcher()) {
            val movieList = listOf<Movie>()
            val exception = Exception()

            coEvery { theMovieDBService.getTopRatedMovieList() } returns CoroutineResult.Failure(exception)
            coEvery { theMovieDBRepository.getDBTopRatedMovie() } returns CoroutineResult.Success(movieList)

            val result = getTopRatedMovieListUseCase()

            coVerify { theMovieDBService.getTopRatedMovieList() }
            coVerify { theMovieDBRepository.getDBTopRatedMovie() }

            assertEquals(movieList, (result as CoroutineResult.Success).data)
        }
    }
}
