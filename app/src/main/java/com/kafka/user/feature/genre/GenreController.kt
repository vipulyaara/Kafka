package com.kafka.user.feature.genre

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import com.airbnb.epoxy.Carousel
import com.kafka.data.entities.Item
import com.kafka.user.ItemBookAltBindingModel_
import com.kafka.user.ItemGenreBindingModel_
import com.kafka.user.extensions.getRandomAuthorResource
import com.kafka.user.extensions.withModelsFrom
import com.kafka.user.feature.common.BaseEpoxyController
import com.kafka.user.feature.profile.ProfileViewState
import com.kafka.user.itemCollectionDetail
import com.kafka.user.itemLoader
import com.kafka.user.ui.epoxy.gridContentCarousel

/**
 * @author Vipul Kumar; dated 19/01/19.
 */

class GenreController constructor(private val callbacks: Callbacks) :
    BaseEpoxyController<GenreViewState>() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun buildModels(viewState: GenreViewState) {
        if (viewState.isLoading) {
            buildLoadingState()
        } else {
            buildDataModels(viewState)
        }
    }

    private fun buildLoadingState() {
        itemLoader { id("loader") }
    }

    private fun buildDataModels(viewState: GenreViewState) {
        gridContentCarousel {
            id("carousel")
            spanCount = 2
            padding(Carousel.Padding.dp(12, 12))
            withModelsFrom(arrayListOf(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19)) {
                ItemGenreBindingModel_()
                    .id(it)
            }
        }
    }

    interface Callbacks {
        fun onBookClicked(view: View, item: Item)
        fun onBannerClicked()
    }
}
