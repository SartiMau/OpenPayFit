package com.sartimau.openpayfit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sartimau.openpayfit.R
import com.sartimau.openpayfit.databinding.CardShowBinding
import com.sartimau.openpayfit.domain.entity.Movie
import com.sartimau.openpayfit.utils.loadImage

class MovieTabAdapter(private val movieList: List<Movie>) : RecyclerView.Adapter<MovieTabViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieTabViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardShowBinding.inflate(inflater, parent, false)
        return MovieTabViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieTabViewHolder, position: Int) {
        val movieList: Movie = movieList[position]
        holder.bind(movieList)
    }

    override fun getItemCount(): Int = movieList.size
}

class MovieTabViewHolder(private val binding: CardShowBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movieItem: Movie) {
        binding.showImage.loadImage(movieItem.posterPath)
        binding.showId.text = binding.root.context.getString(R.string.id, movieItem.id.toString())
        binding.showPopularity.text = movieItem.voteAverage.toString()
        binding.showName.text = movieItem.title
        binding.showOverview.text = movieItem.overview
    }
}
