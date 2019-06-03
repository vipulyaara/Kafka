package com.kafka.user.feature.detail

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.transition.TransitionInflater
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.kafka.data.data.db.MiddlewareTypeConverters
import com.kafka.data.entities.Item
import com.kafka.data.model.item.File
import com.kafka.user.R
import com.kafka.user.databinding.FragmentItemDetailBinding
import com.kafka.user.extensions.show
import com.kafka.user.feature.common.DataBindingMvRxFragment
import com.kafka.user.feature.downloads.BookDownloadFragment
import com.kafka.user.feature.home.NavigationViewModel
import com.kafka.user.feature.home.detailId
import com.kafka.user.feature.home.detailName
import com.kafka.user.feature.home.detailUrl
import com.kafka.user.ui.SharedElementHelper
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_item_detail.*

/**
 * @author Vipul Kumar; dated 27/12/18.
 *
 * Fragment to host detail page.
 */
class ItemDetailFragment : DataBindingMvRxFragment<FragmentItemDetailBinding>(
    R.layout.fragment_item_detail
) {
    companion object {
        @JvmStatic
        fun create(): ItemDetailFragment {
            return ItemDetailFragment().apply {
                arguments = bundleOf(MvRx.KEY_ARG to Arguments(""))
            }
        }
    }

    private val navigator by lazy {
        ViewModelProviders.of(activity!!).get(NavigationViewModel::class.java)
    }

    private val viewModel: ItemDetailViewModel by fragmentViewModel()
    private var files: List<File>? = listOf()
    private val controller = ItemDetailController(object : ItemDetailController.Callbacks {
        override fun onItemClicked(item: Item, sharedElements: SharedElementHelper) {
            detailId = item.itemId
            detailName = item.itemId
            detailUrl = item.coverImage ?: ""
            navigator.showItemDetail(
                item,
                null
            )
        }

        override fun onDownloadClicked() {
            navigator.showDownloads()
        }

        override fun onProfileClicked() {
            navigator.showProfile()
        }

        override fun onReviewClicked() {
            navigator.showReviews()
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElementEnterTransition =
                TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvHome.apply {
            setController(controller)
        }
    }

    override fun invalidate() {
        withState(viewModel) {
            controller.setData(it)
            files = it.itemDetail?.files
        }
    }

    @Parcelize
    data class Arguments(val id: String) : Parcelable
}
