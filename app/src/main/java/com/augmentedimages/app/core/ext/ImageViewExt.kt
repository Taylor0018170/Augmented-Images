package com.augmentedimages.app.core.ext

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

fun ImageView.showWithoutCache(path: String) {
    Glide.with(this)
        .load(path)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .into(this)
}