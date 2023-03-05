package com.sartimau.openpayfit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sartimau.openpayfit.R
import com.sartimau.openpayfit.databinding.CardShowBinding
import com.sartimau.openpayfit.domain.entity.KnownFor
import com.sartimau.openpayfit.utils.loadImage

class ProfileTabAdapter(private val showList: List<KnownFor>) :
    RecyclerView.Adapter<ProfileTabViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileTabViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardShowBinding.inflate(inflater, parent, false)
        return ProfileTabViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileTabViewHolder, position: Int) {
        val showItem: KnownFor = showList[position]
        holder.bind(showItem)
    }

    override fun getItemCount(): Int = showList.size
}

class ProfileTabViewHolder(private val binding: CardShowBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(showItem: KnownFor) {
        binding.showImage.loadImage(showItem.posterPath)
        binding.showId.text = binding.root.context.getString(R.string.id, showItem.id.toString())
        binding.showPopularity.text = showItem.voteAverage.toString()
        binding.showName.text = if (showItem.mediaType == MEDIA_TYPE_MOVIE) showItem.title else showItem.name
        binding.showOverview.text = showItem.overview
    }

    companion object {
        private const val MEDIA_TYPE_MOVIE = "movie"
    }
}
