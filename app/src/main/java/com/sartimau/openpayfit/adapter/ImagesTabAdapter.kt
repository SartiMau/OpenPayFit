package com.sartimau.openpayfit.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sartimau.openpayfit.databinding.CardImageBinding

class ImagesTabAdapter(private val imagesList: List<Bitmap>) :
    RecyclerView.Adapter<ImagesTabViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesTabViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardImageBinding.inflate(inflater, parent, false)
        return ImagesTabViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImagesTabViewHolder, position: Int) {
        val imageItem: Bitmap = imagesList[position]
        holder.bind(imageItem)
    }

    override fun getItemCount(): Int = imagesList.size
}

class ImagesTabViewHolder(private val binding: CardImageBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(imageItem: Bitmap) {
        binding.image.setImageBitmap(imageItem)
    }
}
