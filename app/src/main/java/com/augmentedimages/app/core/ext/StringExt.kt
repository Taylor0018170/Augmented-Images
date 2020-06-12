package com.augmentedimages.app.core.ext

import java.net.URLConnection

fun String?.isImageFile(): Boolean = this?.let {
    try {
        val mimeType: String = URLConnection.guessContentTypeFromName(this)
        return mimeType.startsWith("image")
    } catch (e: Throwable) {
        return@let false
    }
} ?: run {
    return false
}
