package com.kafka.ui_common.ui

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.ModelView

@ModelView(saveViewState = true, autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class GridCarouselHorizontal @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : Carousel(context, attrs, defStyle) {

    override fun createLayoutManager(): LayoutManager {
        return StaggeredGridLayoutManager(2, GridLayoutManager.HORIZONTAL)
    }

    override fun getSnapHelperFactory(): Nothing? = null
}

