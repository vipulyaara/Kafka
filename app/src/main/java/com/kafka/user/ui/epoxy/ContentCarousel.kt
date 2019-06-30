package com.kafka.user.ui.epoxy

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.ModelView

/**
 * @author Vipul Kumar; dated 03/02/19.
 */
@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ContentCarousel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Carousel(context, attrs, defStyleAttr) {

//    override fun createLayoutManager(): LayoutManager {
//        return LinearLayoutManager(context, RecyclerView.VERTICAL, false)
//    }
}
