package com.kafka.ui_common.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Dimension
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.*

@ModelView(saveViewState = true, autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class KafkaCarousel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : Carousel(context, attrs, defStyle) {

    @Dimension(unit = Dimension.PX)
    @set:ModelProp
    var itemWidth: Int = 0

    override fun createLayoutManager(): LayoutManager {
        return LinearLayoutManager(context,  LinearLayoutManager.HORIZONTAL, false)
    }

    override fun getSnapHelperFactory(): Nothing? = null

    override fun onChildAttachedToWindow(child: View) {
        check(!(itemWidth > 0 && numViewsToShowOnScreen > 0)) {
            "Can't use itemWidth and numViewsToShowOnScreen together"
        }
        if (itemWidth > 0) {
            val childLayoutParams = child.layoutParams
            childLayoutParams.width = itemWidth
        }
        super.onChildAttachedToWindow(child)
    }
}

inline fun <T> KafkaCarouselModelBuilder.withModelsFrom(
    items: List<T>,
    modelBuilder: (T) -> EpoxyModel<*>
) {
    models(items.map { modelBuilder(it) })
}

inline fun <T> CarouselModelBuilder.withModelsFrom(
    items: List<T>,
    modelBuilder: (T) -> EpoxyModel<*>
) {
    models(items.map { modelBuilder(it) })
}
