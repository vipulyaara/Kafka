package com.airtel.kafkapp.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.airtel.kafkapp.R

/**
 * @author Vipul Kumar; dated 04/02/19.
 */
class BottomBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    var items = arrayListOf<TabItem>()

    init {

    }

    fun addItem(tabItem: TabItem) {
        val childView = inflate(context, R.layout.item_bottom_bar, null)
        childView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )
        childView.setOnClickListener { onItemClicked() }
        items.add(tabItem.also { it.view = childView })
        addView(childView)
        childView.updateLayoutParams<LinearLayout.LayoutParams> { weight = 1f }
        (childView as ViewGroup).getChildAt(0)
            .findViewById<ImageView>(com.airtel.kafkapp.R.id.ivIcon)
            .setImageResource(tabItem.icon)

        markSelected()
    }

    fun onItemClicked() {
        markSelected()
    }

    private fun markSelected() {
        items.forEach {
            if (it.isSelected) {
                it.view?.findViewById<ImageView>(R.id.ivIcon)
                    ?.setColorFilter(ContextCompat.getColor(context, R.color.icon_tint))
            } else {
                it.view?.findViewById<ImageView>(R.id.ivIcon)
                    ?.setColorFilter(ContextCompat.getColor(context, R.color.icon_tint_light))
            }
        }
    }
}
