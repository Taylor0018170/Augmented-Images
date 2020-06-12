package com.augmentedimages.app.presentation.list.epoxy

import com.airbnb.epoxy.EpoxyController
import com.augmentedimages.app.data.model.MarkerModel

class ListMarkersController : EpoxyController() {

    private var items = ArrayList<MarkerModel>()
    private lateinit var listener: ListMarkersListener

    override fun buildModels() {
        if (items.isEmpty()) {
            emptyListMarker {
                id("".hashCode())
            }
        } else {
            items.forEach { item ->
                listMarker {
                    id(item.hashCode())
                    markerPath(item.markerPath)
                    markerName(item.markerName)
                    editListener {
                        listener.itemEdit(
                            items.indexOfFirst { it.markerPath == item.markerPath },
                            item
                        )
                    }
                    removeListener {
                        listener.itemRemoved(
                            items.indexOfFirst { it.markerPath == item.markerPath },
                            item
                        )
                    }
                }
            }
        }
    }

    fun setItems(items: List<MarkerModel>) {
        this.items.addAll(items)
        requestModelBuild()
    }

    fun setListener(listener: ListMarkersListener) {
        this.listener = listener
    }

    fun updateList(position: Int) {
        items.removeAt(position)
        requestModelBuild()
    }

    fun updateList(position: Int, item: MarkerModel) {
        var pos = position
        items.apply {
            find { it.markerName == item.markerName }?.let {
                items.remove(it)
                pos -= 1
            }
            removeAt(pos)
            add(pos, item)
        }
        requestModelBuild()
    }

    fun updateList(item: MarkerModel) {
        items.add(item)
        requestModelBuild()
    }

    interface ListMarkersListener {
        fun itemEdit(position: Int, item: MarkerModel)
        fun itemRemoved(position: Int, item: MarkerModel)
    }
}