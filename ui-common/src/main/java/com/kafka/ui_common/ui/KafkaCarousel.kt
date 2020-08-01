package com.kafka.ui_common.ui

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.CarouselModelBuilder
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.ModelView

@ModelView(saveViewState = true, autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class KafkaCarousel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : Carousel(context, attrs, defStyle) {

    override fun createLayoutManager(): LayoutManager {
        return LinearLayoutManager(context,  LinearLayoutManager.HORIZONTAL, false)
    }

    override fun getSnapHelperFactory(): Nothing? = null
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
