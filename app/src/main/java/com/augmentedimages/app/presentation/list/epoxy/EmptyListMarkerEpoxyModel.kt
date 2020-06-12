package com.augmentedimages.app.presentation.list.epoxy

import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.augmentedimages.app.R
import com.augmentedimages.app.core.epoxy.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.item_empty_markers)
abstract class EmptyListMarkerEpoxyModel :
    EpoxyModelWithHolder<EmptyListMarkerEpoxyModel.Holder>() {

    class Holder : KotlinEpoxyHolder() {

    }
}