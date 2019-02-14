package com.airtel.kafkapp.feature.detail

import android.os.Bundle
import android.view.View
import com.airtel.data.entities.Item
import com.airtel.data.entities.ItemDetail
import com.airtel.data.model.data.Resource
import com.airtel.kafka.ServiceRequest
import com.airtel.kafka.observe
import com.airtel.kafkapp.R
import com.airtel.kafkapp.databinding.FragmentBookDetailBinding
import com.airtel.kafkapp.extensions.viewModel
import com.airtel.kafkapp.feature.common.DataBindingFragment
import com.airtel.kafkapp.feature.reviews.BookReviewFragment
import kotlinx.android.synthetic.main.fragment_book_detail.*

/**
 * @author Vipul Kumar; dated 27/12/18.
 *
 * Fragment to host detail page.
 */
class ItemDetailFragment :
    DataBindingFragment<FragmentBookDetailBinding>(
        R.layout.fragment_book_detail
    ) {
    private val viewModel: ItemDetailViewModel by viewModel()
    private val controller = ItemDetailController(
        object : ItemDetailController.Callbacks {
            override fun onReviewsClicked() {
                BookReviewFragment().show(activity?.supportFragmentManager, "")
            }
        }
    )

    private val viewState = ItemDetailViewState()

    private val contentId by lazy { arguments?.getString("itemId") }

    private lateinit var request: ServiceRequest<Resource<ItemDetail>>
    private lateinit var creatorRequest: ServiceRequest<Resource<List<Item>>>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvDetail.apply {
            setController(controller)
        }

        request = viewModel.itemDetailRequest(contentId).apply {
            observe(this@ItemDetailFragment, ::onContentDetailFetched)
            execute()
        }
    }

    private fun onContentDetailFetched(it: Resource<ItemDetail>?) {
        it?.let { resource ->
            logger.d("Detail status ${resource.status} error ${resource.error} data ${resource.data?.title}")
            resource.data?.let { itemDetail ->
                controller.setData(viewState.also { it.itemDetail = itemDetail })

                creatorRequest = viewModel.itemsByCreatorRequest(itemDetail.creator).apply {
                    observe(this@ItemDetailFragment, ::onContentByAuthorFetched)
                    execute()
                }
            }
        }
    }

    private fun onContentByAuthorFetched(it: Resource<List<Item>>?) {
        it?.let { resource ->
            resource.data?.let { data -> controller.setData(viewState.also { it.itemsByCreator = data }) }
        }
    }
}
