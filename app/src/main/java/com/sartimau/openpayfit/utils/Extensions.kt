package com.sartimau.openpayfit.utils

import android.widget.ImageView
import com.sartimau.openpayfit.BuildConfig
import com.sartimau.openpayfit.R
import com.squareup.picasso.Picasso

fun ImageView.loadImage(path: String) {
    Picasso.with(context)
        .load("${BuildConfig.IMAGES_BASE_URL}$path")
        .placeholder(R.mipmap.movie_placeholder)
        .fit()
        .centerCrop()
        .into(this)
}
