package com.augmentedimages.app.core.ext

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import java.io.IOException

fun Uri.toBitmap(context: Context): Bitmap? {
    return try {
        context.contentResolver.openInputStream(this)?.use {
            BitmapFactory.decodeStream(it)
        }
    } catch (e: IOException) {
        Log.e("ImageArFragment", "IOException while loading augmented image from storage", e)
        null
    }
}