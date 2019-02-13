package com.airtel.kafkapp.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.airtel.data.data.db.entities.Book

class ListItemSharedElementHelper(
    private val recyclerView: RecyclerView,
    private val viewFinder: (View) -> View = { it }
) {
    fun createForItem(item: Book, transitionName: String): SharedElementHelper {
        return createForId(item.generateStableId(), transitionName)
    }

    fun createForId(viewHolderId: Long, transitionName: String): SharedElementHelper {
        val sharedElementHelper = SharedElementHelper()
        addSharedElement(sharedElementHelper, viewHolderId, transitionName)
        return sharedElementHelper
    }

    fun createForView(view: View, transitionName: String): SharedElementHelper {
        val sharedElementHelper = SharedElementHelper()
        sharedElementHelper.addSharedElement(view, transitionName)
        return sharedElementHelper
    }

    fun createForItems(items: List<Book>?): SharedElementHelper {
        val sharedElementHelper = SharedElementHelper()
        items?.forEach {
            val id = it.id
            addSharedElement(sharedElementHelper, it.generateStableId(), id.toString())
        }
        return sharedElementHelper
    }

    private fun addSharedElement(
        helper: SharedElementHelper,
        viewHolderId: Long,
        transitionName: String
    ) {
        recyclerView.findViewHolderForItemId(viewHolderId)?.also {
            helper.addSharedElement(viewFinder(it.itemView), transitionName)
        }
    }
}
