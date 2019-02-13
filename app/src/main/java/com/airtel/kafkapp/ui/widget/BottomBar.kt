package com.airtel.kafkapp.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.airtel.kafkapp.R

/**
 * @author Vipul Kumar; dated 04/02/19.
 */
class BottomBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {

    }

    fun addItem(tabItem: TabItem) {
        val views = inflate(context, R.layout.item_bottom_bar, null)
        addView(views)
        (views as ViewGroup).getChildAt(0).findViewById<ImageView>(R.id.ivIcon)
            .setImageResource(tabItem.icon)
    }
}
