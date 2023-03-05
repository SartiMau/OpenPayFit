package com.sartimau.openpayfit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sartimau.openpayfit.R
import com.sartimau.openpayfit.adapter.MovieTabAdapter
import com.sartimau.openpayfit.databinding.ExpandableCardShowBinding
import com.sartimau.openpayfit.databinding.FragmentMoviesBinding
import com.sartimau.openpayfit.dialog.ErrorDialog
import com.sartimau.openpayfit.domain.entity.Movie
import com.sartimau.openpayfit.utils.loadImage
import com.sartimau.openpayfit.viewmodel.MoviesViewModel
import com.sartimau.openpayfit.viewmodel.MoviesViewModel.MoviesData
import com.sartimau.openpayfit.viewmodel.MoviesViewModel.MoviesSection.POPULAR
import com.sartimau.openpayfit.viewmodel.MoviesViewModel.MoviesSection.RECOMMENDED
import com.sartimau.openpayfit.viewmodel.MoviesViewModel.MoviesSection.TOP_RATED
import com.sartimau.openpayfit.viewmodel.MoviesViewModel.MoviesState.COLLAPSE_CARD
import com.sartimau.openpayfit.viewmodel.MoviesViewModel.MoviesState.EMPTY_STATE
import com.sartimau.openpayfit.viewmodel.MoviesViewModel.MoviesState.EXPAND_CARD
import com.sartimau.openpayfit.viewmodel.MoviesViewModel.MoviesState.ON_ERROR
import com.sartimau.openpayfit.viewmodel.MoviesViewModel.MoviesState.SHOW_POPULAR_MOVIES
import com.sartimau.openpayfit.viewmodel.MoviesViewModel.MoviesState.SHOW_RECOMMENDED_MOVIES
import com.sartimau.openpayfit.viewmodel.MoviesViewModel.MoviesState.SHOW_TOP_RATED_MOVIES
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private val viewModel: MoviesViewModel by viewModels()

    private lateinit var binding: FragmentMoviesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe({ lifecycle }, ::updateUi)
    }

    override fun onResume() {
        super.onResume()

        viewModel.fetchMovies()
    }

    private fun updateUi(data: MoviesData) {
        when (data.state) {
            SHOW_POPULAR_MOVIES -> fulfillCard(binding.popularCard, data.popularMovies) { viewModel.onPopularExpandableClicked() }
            SHOW_TOP_RATED_MOVIES -> {
                binding.recommendedMovieList.text =
                    getString(R.string.recommended_movie_list, data.topRatedMovies.maxBy { it.voteAverage }.title)
                fulfillCard(binding.topRatedCard, data.topRatedMovies) { viewModel.onTopRatedExpandableClicked() }
            }
            SHOW_RECOMMENDED_MOVIES -> fulfillCard(
                binding.recommendedCard,
                data.recommendationMovies
            ) { viewModel.onRecommendedExpandableClicked() }
            ON_ERROR -> ErrorDialog.newInstance().show(childFragmentManager, ErrorDialog.TAG)
            EXPAND_CARD -> expandCard(data.moviesSection)
            COLLAPSE_CARD -> collapseCard(data.moviesSection)
            EMPTY_STATE -> TODO()
        }
    }

    private fun fulfillCard(expandableCardShowBinding: ExpandableCardShowBinding, moviesList: List<Movie>, onExpandClick: () -> Unit) {
        val movie = moviesList[0]
        expandableCardShowBinding.showImage.loadImage(movie.posterPath)
        expandableCardShowBinding.showId.text = binding.root.context.getString(R.string.id, movie.id.toString())
        expandableCardShowBinding.showPopularity.text = movie.voteAverage.toString()
        expandableCardShowBinding.showName.text = movie.title
        expandableCardShowBinding.showOverview.text = movie.overview

        expandableCardShowBinding.recycler.apply {
            adapter = MovieTabAdapter(moviesList.subList(0, moviesList.size - 1))
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }

        expandableCardShowBinding.expandableText.setOnClickListener { onExpandClick() }
    }

    private fun expandCard(section: MoviesViewModel.MoviesSection) {
        when (section) {
            POPULAR -> {
                binding.popularCard.recycler.visibility = View.VISIBLE
                binding.popularCard.expandableText.text = getString(R.string.see_less)
            }
            TOP_RATED -> {
                binding.topRatedCard.recycler.visibility = View.VISIBLE
                binding.topRatedCard.expandableText.text = getString(R.string.see_less)
            }
            RECOMMENDED -> {
                binding.recommendedCard.recycler.visibility = View.VISIBLE
                binding.recommendedCard.expandableText.text = getString(R.string.see_less)
            }
        }
    }

    private fun collapseCard(section: MoviesViewModel.MoviesSection) {
        when (section) {
            POPULAR -> {
                binding.popularCard.recycler.visibility = View.GONE
                binding.popularCard.expandableText.text = getString(R.string.see_more)
            }
            TOP_RATED -> {
                binding.topRatedCard.recycler.visibility = View.GONE
                binding.topRatedCard.expandableText.text = getString(R.string.see_more)
            }
            RECOMMENDED -> {
                binding.recommendedCard.recycler.visibility = View.GONE
                binding.recommendedCard.expandableText.text = getString(R.string.see_more)
            }
        }
    }
}
