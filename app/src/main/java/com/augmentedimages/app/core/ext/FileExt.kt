package com.augmentedimages.app.core.ext

import com.augmentedimages.app.data.model.MarkerModel
import java.io.File

fun File.getListImage(): List<MarkerModel> {
    val list = mutableListOf<MarkerModel>()
    walkTopDown().forEach {
        if (it.absolutePath.isImageFile())
            list.add(MarkerModel(System.currentTimeMillis(), it.absolutePath, it.name))
    }
    return list
}