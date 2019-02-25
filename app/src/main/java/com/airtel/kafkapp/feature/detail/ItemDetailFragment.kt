package com.airtel.kafkapp.feature.detail

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.core.os.bundleOf
import androidx.transition.TransitionInflater
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.airtel.data.entities.Item
import com.airtel.kafkapp.R
import com.airtel.kafkapp.databinding.FragmentItemDetailBinding
import com.airtel.kafkapp.extensions.show
import com.airtel.kafkapp.feature.MainActivity
import com.airtel.kafkapp.feature.common.DataBindingMvRxFragment
import com.airtel.kafkapp.feature.home.detailId
import com.airtel.kafkapp.feature.reviews.BookReviewFragment
import com.airtel.kafkapp.ui.SharedElementHelper
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

    private val viewModel: ItemDetailViewModel by fragmentViewModel()
    private val controller = ItemDetailController(object : ItemDetailController.Callbacks {
        override fun onItemClicked(item: Item, sharedElements: SharedElementHelper) {
            detailId = item.itemId
            (activity as MainActivity).launchDetailFragment(sharedElements)
        }

        override fun onReviewsClicked() {
            BookReviewFragment().show(this@ItemDetailFragment)
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
        }
    }

    @Parcelize
    data class Arguments(val id: String) : Parcelable
}
