package com.augmentedimages.app.core.ext

import android.content.Context
import com.augmentedimages.app.data.model.MarkerModel
import java.io.File

fun Context.renameTo(file: File, name: String): MarkerModel {
    val fileNew = File(filesDir, name)
    file.renameTo(fileNew)
    return MarkerModel(System.currentTimeMillis(), fileNew.absolutePath, fileNew.name)
}