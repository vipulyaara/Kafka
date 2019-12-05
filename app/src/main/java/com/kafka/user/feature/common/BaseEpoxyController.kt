package com.kafka.user.feature.common

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyController

/**
 * @author Vipul Kumar; dated 16/02/19.
 */
abstract class BaseEpoxyController : EpoxyController() {
    public override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        // This will force all models to be unbound and their views recycled once the RecyclerView is no longer in use. We need this so resources
        // are properly released, listeners are detached, and views can be returned to view pools (if applicable).
        if (recyclerView.layoutManager is LinearLayoutManager) {
            (recyclerView.layoutManager as LinearLayoutManager).recycleChildrenOnDetach = true
        }
    }
}
