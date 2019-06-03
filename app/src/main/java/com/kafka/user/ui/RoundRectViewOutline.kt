package com.kafka.user.ui

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import com.kafka.user.R

object RoundRectViewOutline : ViewOutlineProvider() {
    override fun getOutline(view: View, outline: Outline) {
        val radius = view.resources.getDimension(R.dimen.image_round_rect_radius)
        outline.setRoundRect(0, 0, view.width, view.height, radius)
    }
}
