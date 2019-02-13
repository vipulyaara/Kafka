package com.airtel.kafkapp.feature.detail

import android.os.Bundle
import android.view.View
import com.airtel.data.data.db.entities.Book
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
class BookDetailFragment :
    DataBindingFragment<FragmentBookDetailBinding>(
        R.layout.fragment_book_detail
    ) {
    private val viewModel: BookDetailViewModel by viewModel()
    private val controller = BookDetailController(
        object : BookDetailController.Callbacks {
            override fun onReviewsClicked() {
                BookReviewFragment().show(activity?.supportFragmentManager, "")
            }
        }
    )

    private val contentId by lazy { arguments?.getString("contentId") }

    private lateinit var request: ServiceRequest<Resource<Book>>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvDetail.apply {
            setController(controller)
        }

        request = viewModel.contentDetailRequest(contentId).apply {
            observe(this@BookDetailFragment, ::onContentDetailFetched)
            execute()
        }
    }

    private fun onContentDetailFetched(it: Resource<Book>?) {
        it?.let {
            logger.d("Detail status ${it.status} error ${it.error} data ${it.data}")
            controller.setData(it.data)
        }
    }
}
