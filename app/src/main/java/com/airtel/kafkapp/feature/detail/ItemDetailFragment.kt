package com.airtel.kafkapp.feature.detail

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.core.os.bundleOf
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.airtel.data.entities.Item
import com.airtel.kafkapp.R
import com.airtel.kafkapp.databinding.FragmentHomeBinding
import com.airtel.kafkapp.extensions.show
import com.airtel.kafkapp.feature.MainActivity
import com.airtel.kafkapp.feature.common.DataBindingMvRxFragment
import com.airtel.kafkapp.feature.home.detailId
import com.airtel.kafkapp.feature.reviews.BookReviewFragment
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * @author Vipul Kumar; dated 27/12/18.
 *
 * Fragment to host detail page.
 */
class ItemDetailFragment : DataBindingMvRxFragment<FragmentHomeBinding>(
    R.layout.fragment_home
) {
    companion object {
        @JvmStatic
        fun create(id: String): ItemDetailFragment {
            return ItemDetailFragment().apply {
                arguments = bundleOf(MvRx.KEY_ARG to Arguments(id))
            }
        }
    }

    private val viewModel: ItemDetailViewModel by fragmentViewModel()
    private val controller = ItemDetailController(object : ItemDetailController.Callbacks {
        override fun onItemClicked(item: Item) {
            detailId = item.itemId
            (activity as MainActivity).launchDetailFragment()
        }

        override fun onReviewsClicked() {
            BookReviewFragment().show(this@ItemDetailFragment)
        }
    })

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
