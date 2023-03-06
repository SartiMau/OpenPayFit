package com.sartimau.openpayfit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sartimau.openpayfit.domain.entity.Movie
import com.sartimau.openpayfit.domain.usecase.GetPopularMovieListUseCase
import com.sartimau.openpayfit.domain.usecase.GetRecommendedMoviesListByIdUseCase
import com.sartimau.openpayfit.domain.usecase.GetTopRatedMovieListUseCase
import com.sartimau.openpayfit.domain.utils.CoroutineResult
import com.sartimau.openpayfit.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getPopularMovieListUseCase: GetPopularMovieListUseCase,
    private val getTopRatedMovieListUseCase: GetTopRatedMovieListUseCase,
    private val getRecommendedMoviesListByIdUseCase: GetRecommendedMoviesListByIdUseCase
) : BaseViewModel() {

    private var mutableState = MutableLiveData<MoviesData>()
    val state: LiveData<MoviesData> = mutableState

    private var isPopularExpanded = false
    private var isTopRatedExpanded = false
    private var isRecommendedExpanded = false

    fun fetchMovies() = viewModelScope.launch {
        loading.value = true

        withContext(Dispatchers.IO) { async { fetchPopularMovies() } }.await()
        withContext(Dispatchers.IO) { async { fetchTopRatedMovies() } }.await()

        loading.value = false
    }

    private suspend fun fetchPopularMovies() =
        withContext(Dispatchers.IO) { getPopularMovieListUseCase() }.let { result ->
            when (result) {
                is CoroutineResult.Success -> {
                    if (result.data.isNotEmpty()) {
                        mutableState.postValue(MoviesData(state = MoviesState.SHOW_POPULAR_MOVIES, popularMovies = result.data))
                    } else {
                        mutableState.postValue(MoviesData(state = MoviesState.POPULAR_EMPTY_STATE))
                    }
                }
                is CoroutineResult.Failure -> {
                    mutableState.postValue(MoviesData(state = MoviesState.ON_ERROR, exception = result.exception))
                }
            }
        }

    private suspend fun fetchTopRatedMovies() =
        withContext(Dispatchers.IO) { getTopRatedMovieListUseCase() }.let { result ->
            when (result) {
                is CoroutineResult.Success -> {
                    if (result.data.isNotEmpty()) {
                        mutableState.postValue(MoviesData(state = MoviesState.SHOW_TOP_RATED_MOVIES, topRatedMovies = result.data))
                        fetchRecommendationsMovies(result.data.maxBy { it.popularity })
                    } else {
                        mutableState.postValue(MoviesData(state = MoviesState.TOP_RATED_EMPTY_STATE))
                    }
                }
                is CoroutineResult.Failure -> {
                    mutableState.postValue(MoviesData(state = MoviesState.ON_ERROR, exception = result.exception))
                }
            }
        }

    private suspend fun fetchRecommendationsMovies(mostRatedMovie: Movie) =
        withContext(Dispatchers.IO) { getRecommendedMoviesListByIdUseCase(mostRatedMovie.id) }.let { result ->
            when (result) {
                is CoroutineResult.Success -> {
                    if (result.data.isNotEmpty()) {
                        mutableState.postValue(
                            MoviesData(state = MoviesState.SHOW_RECOMMENDED_MOVIES, recommendationMovies = result.data)
                        )
                    } else {
                        mutableState.postValue(MoviesData(state = MoviesState.RECOMMENDED_EMPTY_STATE))
                    }
                }
                is CoroutineResult.Failure -> {
                    mutableState.postValue(MoviesData(state = MoviesState.ON_ERROR, exception = result.exception))
                }
            }
        }

    fun onPopularExpandableClicked() {
        if (isPopularExpanded) {
            isPopularExpanded = false
            mutableState.value = MoviesData(state = MoviesState.COLLAPSE_CARD, moviesSection = MoviesSection.POPULAR)
        } else {
            isPopularExpanded = true
            mutableState.value = MoviesData(state = MoviesState.EXPAND_CARD, moviesSection = MoviesSection.POPULAR)
        }
    }

    fun onTopRatedExpandableClicked() {
        if (isTopRatedExpanded) {
            isTopRatedExpanded = false
            mutableState.value = MoviesData(state = MoviesState.COLLAPSE_CARD, moviesSection = MoviesSection.TOP_RATED)
        } else {
            isTopRatedExpanded = true
            mutableState.value = MoviesData(state = MoviesState.EXPAND_CARD, moviesSection = MoviesSection.TOP_RATED)
        }
    }

    fun onRecommendedExpandableClicked() {
        if (isRecommendedExpanded) {
            isRecommendedExpanded = false
            mutableState.value = MoviesData(state = MoviesState.COLLAPSE_CARD, moviesSection = MoviesSection.RECOMMENDED)
        } else {
            isRecommendedExpanded = true
            mutableState.value = MoviesData(state = MoviesState.EXPAND_CARD, moviesSection = MoviesSection.RECOMMENDED)
        }
    }

    data class MoviesData(
        val state: MoviesState,
        val popularMovies: List<Movie> = emptyList(),
        val topRatedMovies: List<Movie> = emptyList(),
        val recommendationMovies: List<Movie> = emptyList(),
        val moviesSection: MoviesSection = MoviesSection.POPULAR,
        val exception: Exception? = null
    )

    enum class MoviesState {
        SHOW_POPULAR_MOVIES,
        SHOW_TOP_RATED_MOVIES,
        SHOW_RECOMMENDED_MOVIES,
        ON_ERROR,
        POPULAR_EMPTY_STATE,
        TOP_RATED_EMPTY_STATE,
        RECOMMENDED_EMPTY_STATE,
        EXPAND_CARD,
        COLLAPSE_CARD
    }

    enum class MoviesSection {
        POPULAR,
        TOP_RATED,
        RECOMMENDED
    }
}
