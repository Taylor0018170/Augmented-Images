package com.augmentedimages.app.core.ext

import android.view.View

fun View.onClick(listener: ((View) -> Unit)?) {
    this.setOnClickListener { listener?.invoke(it) }
}