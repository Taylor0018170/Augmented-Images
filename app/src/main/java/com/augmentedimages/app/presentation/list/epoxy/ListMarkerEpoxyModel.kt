package com.augmentedimages.app.presentation.list.epoxy

import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.augmentedimages.app.R
import com.augmentedimages.app.core.epoxy.KotlinEpoxyHolder
import com.augmentedimages.app.core.ext.onClick
import com.augmentedimages.app.core.ext.showWithoutCache

@EpoxyModelClass(layout = R.layout.item_marker)
abstract class ListMarkerEpoxyModel : EpoxyModelWithHolder<ListMarkerEpoxyModel.Holder>() {

    @EpoxyAttribute lateinit var markerPath: String
    @EpoxyAttribute lateinit var markerName: String
    @EpoxyAttribute lateinit var editListener: () -> Unit
    @EpoxyAttribute lateinit var removeListener: () -> Unit

    override fun bind(holder: Holder) {
        super.bind(holder)
        with(holder) {
            imgPreview.showWithoutCache(markerPath)
            tvName.text = markerName
            btnEdit.onClick {
                editListener.invoke()
            }
            btnRemove.onClick {
                removeListener.invoke()
            }
        }
    }

    class Holder : KotlinEpoxyHolder() {
        val imgPreview by bind<AppCompatImageView>(R.id.imgPreview)
        val tvName by bind<AppCompatTextView>(R.id.tvName)
        val btnEdit by bind<AppCompatImageButton>(R.id.btnEdit)
        val btnRemove by bind<AppCompatImageButton>(R.id.btnRemove)
    }
}