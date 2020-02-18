package com.kafka.user.ui

import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.onScrollElevate(toolbar: Toolbar?) {
    onScrolled { toolbar?.isSelected = canScrollVertically(-1) }
}

fun RecyclerView.onScrolled(onScrolled: () -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            onScrolled.invoke()
        }
    })
}