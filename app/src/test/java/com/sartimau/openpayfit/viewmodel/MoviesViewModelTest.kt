package com.sartimau.openpayfit.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sartimau.openpayfit.domain.entity.Movie
import com.sartimau.openpayfit.domain.usecase.GetPopularMovieListUseCase
import com.sartimau.openpayfit.domain.usecase.GetRecommendedMoviesListByIdUseCase
import com.sartimau.openpayfit.domain.usecase.GetTopRatedMovieListUseCase
import com.sartimau.openpayfit.domain.utils.CoroutineResult
import com.sartimau.openpayfit.testObserver
import com.sartimau.openpayfit.viewmodel.MoviesViewModel.MoviesSection
import com.sartimau.openpayfit.viewmodel.MoviesViewModel.MoviesState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class MoviesViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var getPopularMovieListUseCase: GetPopularMovieListUseCase

    @MockK
    private lateinit var getTopRatedMovieListUseCase: GetTopRatedMovieListUseCase

    @MockK
    private lateinit var getRecommendedMoviesListByIdUseCase: GetRecommendedMoviesListByIdUseCase

    private val movie = Movie(
        id = MOVIE_ID_LOW_POPULARITY,
        adult = false,
        backdropPath = MOVIE_BACKDROP_PATH,
        originalLanguage = MOVIE_ORIGINAL_LANGUAGE,
        originalTitle = MOVIE_ORIGINAL_TITLE,
        overview = MOVIE_OVERVIEW,
        popularity = MOVIE_LOW_POPULARITY,
        posterPath = MOVIE_PROFILE_PATH,
        releaseDate = MOVIE_RELEASE_DATE,
        title = MOVIE_TITLE,
        video = true,
        voteAverage = MOVIE_VOTE_AVERAGE,
        voteCount = MOVIE_VOTE_COUNT
    )
    private val mostRatedMovie = Movie(
        id = MOVIE_ID_HIGH_POPULARITY,
        adult = false,
        backdropPath = MOVIE_BACKDROP_PATH,
        originalLanguage = MOVIE_ORIGINAL_LANGUAGE,
        originalTitle = MOVIE_ORIGINAL_TITLE,
        overview = MOVIE_OVERVIEW,
        popularity = MOVIE_HIGH_POPULARITY,
        posterPath = MOVIE_PROFILE_PATH,
        releaseDate = MOVIE_RELEASE_DATE,
        title = MOVIE_TITLE,
        video = true,
        voteAverage = MOVIE_VOTE_AVERAGE,
        voteCount = MOVIE_VOTE_COUNT
    )

    private lateinit var viewModel: MoviesViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        MockKAnnotations.init(this, relaxUnitFun = true)

        viewModel = MoviesViewModel(getPopularMovieListUseCase, getTopRatedMovieListUseCase, getRecommendedMoviesListByIdUseCase)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetch movies success test`() {
        runTest(UnconfinedTestDispatcher()) {
            val loadingLiveDataTest = viewModel.isLoading().testObserver()
            val liveDataTest = viewModel.state.testObserver()

            val popularMovieList = listOf(movie)
            val topRatedMovieList = listOf(movie, mostRatedMovie)
            val recommendedMovieList = listOf(movie)

            coEvery { getPopularMovieListUseCase() } returns CoroutineResult.Success(popularMovieList)
            coEvery { getTopRatedMovieListUseCase() } returns CoroutineResult.Success(topRatedMovieList)
            coEvery { getRecommendedMoviesListByIdUseCase(MOVIE_ID_HIGH_POPULARITY) } returns CoroutineResult.Success(recommendedMovieList)

            viewModel.fetchMovies().join()

            assertTrue(loadingLiveDataTest.observedValues[0]!!)
            assertEquals(MoviesState.SHOW_POPULAR_MOVIES, liveDataTest.observedValues[0]?.state)
            assertEquals(popularMovieList, liveDataTest.observedValues[0]?.popularMovies)
            assertEquals(MoviesState.SHOW_TOP_RATED_MOVIES, liveDataTest.observedValues[1]?.state)
            assertEquals(topRatedMovieList, liveDataTest.observedValues[1]?.topRatedMovies)
            assertEquals(MoviesState.SHOW_RECOMMENDED_MOVIES, liveDataTest.observedValues[2]?.state)
            assertEquals(recommendedMovieList, liveDataTest.observedValues[2]?.recommendationMovies)
            assertFalse(loadingLiveDataTest.observedValues[1]!!)
        }
    }

    @Test
    fun `fetch movies success popular empty test`() {
        runTest(UnconfinedTestDispatcher()) {
            val loadingLiveDataTest = viewModel.isLoading().testObserver()
            val liveDataTest = viewModel.state.testObserver()

            val popularMovieList = listOf<Movie>()
            val topRatedMovieList = listOf(movie, mostRatedMovie)
            val recommendedMovieList = listOf(movie)

            coEvery { getPopularMovieListUseCase() } returns CoroutineResult.Success(popularMovieList)
            coEvery { getTopRatedMovieListUseCase() } returns CoroutineResult.Success(topRatedMovieList)
            coEvery { getRecommendedMoviesListByIdUseCase(MOVIE_ID_HIGH_POPULARITY) } returns CoroutineResult.Success(recommendedMovieList)

            viewModel.fetchMovies().join()

            assertTrue(loadingLiveDataTest.observedValues[0]!!)
            assertEquals(MoviesState.POPULAR_EMPTY_STATE, liveDataTest.observedValues[0]?.state)
            assertEquals(popularMovieList, liveDataTest.observedValues[0]?.popularMovies)
            assertEquals(MoviesState.SHOW_TOP_RATED_MOVIES, liveDataTest.observedValues[1]?.state)
            assertEquals(topRatedMovieList, liveDataTest.observedValues[1]?.topRatedMovies)
            assertEquals(MoviesState.SHOW_RECOMMENDED_MOVIES, liveDataTest.observedValues[2]?.state)
            assertEquals(recommendedMovieList, liveDataTest.observedValues[2]?.recommendationMovies)
            assertFalse(loadingLiveDataTest.observedValues[1]!!)
        }
    }

    @Test
    fun `fetch movies success recommended empty test`() {
        runTest(UnconfinedTestDispatcher()) {
            val loadingLiveDataTest = viewModel.isLoading().testObserver()
            val liveDataTest = viewModel.state.testObserver()

            val popularMovieList = listOf(movie)
            val topRatedMovieList = listOf(movie, mostRatedMovie)
            val recommendedMovieList = listOf<Movie>()

            coEvery { getPopularMovieListUseCase() } returns CoroutineResult.Success(popularMovieList)
            coEvery { getTopRatedMovieListUseCase() } returns CoroutineResult.Success(topRatedMovieList)
            coEvery { getRecommendedMoviesListByIdUseCase(MOVIE_ID_HIGH_POPULARITY) } returns CoroutineResult.Success(recommendedMovieList)

            viewModel.fetchMovies().join()

            assertTrue(loadingLiveDataTest.observedValues[0]!!)
            assertEquals(MoviesState.SHOW_POPULAR_MOVIES, liveDataTest.observedValues[0]?.state)
            assertEquals(popularMovieList, liveDataTest.observedValues[0]?.popularMovies)
            assertEquals(MoviesState.SHOW_TOP_RATED_MOVIES, liveDataTest.observedValues[1]?.state)
            assertEquals(topRatedMovieList, liveDataTest.observedValues[1]?.topRatedMovies)
            assertEquals(MoviesState.RECOMMENDED_EMPTY_STATE, liveDataTest.observedValues[2]?.state)
            assertEquals(recommendedMovieList, liveDataTest.observedValues[2]?.recommendationMovies)
            assertFalse(loadingLiveDataTest.observedValues[1]!!)
        }
    }

    @Test
    fun `fetch movies success top rated empty test`() {
        runTest(UnconfinedTestDispatcher()) {
            val loadingLiveDataTest = viewModel.isLoading().testObserver()
            val liveDataTest = viewModel.state.testObserver()

            val popularMovieList = listOf(movie)
            val topRatedMovieList = listOf<Movie>()

            coEvery { getPopularMovieListUseCase() } returns CoroutineResult.Success(popularMovieList)
            coEvery { getTopRatedMovieListUseCase() } returns CoroutineResult.Success(topRatedMovieList)

            viewModel.fetchMovies().join()

            assertTrue(loadingLiveDataTest.observedValues[0]!!)
            assertEquals(MoviesState.SHOW_POPULAR_MOVIES, liveDataTest.observedValues[0]?.state)
            assertEquals(popularMovieList, liveDataTest.observedValues[0]?.popularMovies)
            assertEquals(MoviesState.TOP_RATED_EMPTY_STATE, liveDataTest.observedValues[1]?.state)
            assertEquals(topRatedMovieList, liveDataTest.observedValues[1]?.topRatedMovies)
            assertFalse(loadingLiveDataTest.observedValues[1]!!)
        }
    }

    @Test
    fun `fetch movies failure popular test`() {
        runTest(UnconfinedTestDispatcher()) {
            val loadingLiveDataTest = viewModel.isLoading().testObserver()
            val liveDataTest = viewModel.state.testObserver()

            val exception = Exception()
            val topRatedMovieList = listOf(movie, mostRatedMovie)
            val recommendedMovieList = listOf(movie)

            coEvery { getPopularMovieListUseCase() } returns CoroutineResult.Failure(exception)
            coEvery { getTopRatedMovieListUseCase() } returns CoroutineResult.Success(topRatedMovieList)
            coEvery { getRecommendedMoviesListByIdUseCase(MOVIE_ID_HIGH_POPULARITY) } returns CoroutineResult.Success(recommendedMovieList)

            viewModel.fetchMovies().join()

            assertTrue(loadingLiveDataTest.observedValues[0]!!)
            assertEquals(MoviesState.ON_ERROR, liveDataTest.observedValues[0]?.state)
            assertEquals(exception, liveDataTest.observedValues[0]?.exception)
            assertEquals(MoviesState.SHOW_TOP_RATED_MOVIES, liveDataTest.observedValues[1]?.state)
            assertEquals(topRatedMovieList, liveDataTest.observedValues[1]?.topRatedMovies)
            assertEquals(MoviesState.SHOW_RECOMMENDED_MOVIES, liveDataTest.observedValues[2]?.state)
            assertEquals(recommendedMovieList, liveDataTest.observedValues[2]?.recommendationMovies)
            assertFalse(loadingLiveDataTest.observedValues[1]!!)
        }
    }

    @Test
    fun `fetch movies failure recommended test`() {
        runTest(UnconfinedTestDispatcher()) {
            val loadingLiveDataTest = viewModel.isLoading().testObserver()
            val liveDataTest = viewModel.state.testObserver()

            val exception = Exception()
            val popularMovieList = listOf(movie)
            val topRatedMovieList = listOf(movie, mostRatedMovie)

            coEvery { getPopularMovieListUseCase() } returns CoroutineResult.Success(popularMovieList)
            coEvery { getTopRatedMovieListUseCase() } returns CoroutineResult.Success(topRatedMovieList)
            coEvery { getRecommendedMoviesListByIdUseCase(MOVIE_ID_HIGH_POPULARITY) } returns CoroutineResult.Failure(exception)

            viewModel.fetchMovies().join()

            assertTrue(loadingLiveDataTest.observedValues[0]!!)
            assertEquals(MoviesState.SHOW_POPULAR_MOVIES, liveDataTest.observedValues[0]?.state)
            assertEquals(popularMovieList, liveDataTest.observedValues[0]?.popularMovies)
            assertEquals(MoviesState.SHOW_TOP_RATED_MOVIES, liveDataTest.observedValues[1]?.state)
            assertEquals(topRatedMovieList, liveDataTest.observedValues[1]?.topRatedMovies)
            assertEquals(MoviesState.ON_ERROR, liveDataTest.observedValues[2]?.state)
            assertEquals(exception, liveDataTest.observedValues[2]?.exception)
            assertFalse(loadingLiveDataTest.observedValues[1]!!)
        }
    }

    @Test
    fun `fetch movies failure top rated test`() {
        runTest(UnconfinedTestDispatcher()) {
            val loadingLiveDataTest = viewModel.isLoading().testObserver()
            val liveDataTest = viewModel.state.testObserver()

            val popularMovieList = listOf(movie)
            val exception = Exception()

            coEvery { getPopularMovieListUseCase() } returns CoroutineResult.Success(popularMovieList)
            coEvery { getTopRatedMovieListUseCase() } returns CoroutineResult.Failure(exception)

            viewModel.fetchMovies().join()

            assertTrue(loadingLiveDataTest.observedValues[0]!!)
            assertEquals(MoviesState.SHOW_POPULAR_MOVIES, liveDataTest.observedValues[0]?.state)
            assertEquals(popularMovieList, liveDataTest.observedValues[0]?.popularMovies)
            assertEquals(MoviesState.ON_ERROR, liveDataTest.observedValues[1]?.state)
            assertEquals(exception, liveDataTest.observedValues[1]?.exception)
            assertFalse(loadingLiveDataTest.observedValues[1]!!)
        }
    }

    @Test
    fun `on popular expandable clicked test`() {
        val liveDataTest = viewModel.state.testObserver()

        viewModel.onPopularExpandableClicked()

        assertEquals(MoviesState.EXPAND_CARD, liveDataTest.observedValues[0]?.state)
        assertEquals(MoviesSection.POPULAR, liveDataTest.observedValues[0]?.moviesSection)

        viewModel.onPopularExpandableClicked()

        assertEquals(MoviesState.COLLAPSE_CARD, liveDataTest.observedValues[1]?.state)
        assertEquals(MoviesSection.POPULAR, liveDataTest.observedValues[1]?.moviesSection)
    }

    @Test
    fun `on top rated expandable clicked test`() {
        val liveDataTest = viewModel.state.testObserver()

        viewModel.onTopRatedExpandableClicked()

        assertEquals(MoviesState.EXPAND_CARD, liveDataTest.observedValues[0]?.state)
        assertEquals(MoviesSection.TOP_RATED, liveDataTest.observedValues[0]?.moviesSection)

        viewModel.onTopRatedExpandableClicked()

        assertEquals(MoviesState.COLLAPSE_CARD, liveDataTest.observedValues[1]?.state)
        assertEquals(MoviesSection.TOP_RATED, liveDataTest.observedValues[1]?.moviesSection)
    }

    @Test
    fun `on recommended expandable clicked test`() {
        val liveDataTest = viewModel.state.testObserver()

        viewModel.onRecommendedExpandableClicked()

        assertEquals(MoviesState.EXPAND_CARD, liveDataTest.observedValues[0]?.state)
        assertEquals(MoviesSection.RECOMMENDED, liveDataTest.observedValues[0]?.moviesSection)

        viewModel.onRecommendedExpandableClicked()

        assertEquals(MoviesState.COLLAPSE_CARD, liveDataTest.observedValues[1]?.state)
        assertEquals(MoviesSection.RECOMMENDED, liveDataTest.observedValues[1]?.moviesSection)
    }

    companion object {
        private const val MOVIE_ID_LOW_POPULARITY = 9292
        private const val MOVIE_ID_HIGH_POPULARITY = 19515
        private const val MOVIE_BACKDROP_PATH = "MOVIE_BACKDROP_PATH"
        private const val MOVIE_ORIGINAL_LANGUAGE = "MOVIE_ORIGINAL_LANGUAGE"
        private const val MOVIE_ORIGINAL_TITLE = "MOVIE_ORIGINAL_TITLE"
        private const val MOVIE_OVERVIEW = "MOVIE_OVERVIEW"
        private const val MOVIE_LOW_POPULARITY = 52.43
        private const val MOVIE_HIGH_POPULARITY = 543.87
        private const val MOVIE_PROFILE_PATH = "POPULAR_PERSON_PROFILE_PATH"
        private const val MOVIE_RELEASE_DATE = "MOVIE_RELEASE_DATE"
        private const val MOVIE_TITLE = "MOVIE_TITLE"
        private const val MOVIE_VOTE_AVERAGE = 253.7
        private const val MOVIE_VOTE_COUNT = 1000
    }
}
