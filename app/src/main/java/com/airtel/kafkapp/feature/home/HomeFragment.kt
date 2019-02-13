package com.airtel.kafkapp.feature.home

import android.os.Bundle
import android.view.View
import com.airtel.data.data.db.entities.Book
import com.airtel.data.model.data.Resource
import com.airtel.kafka.ServiceRequest
import com.airtel.kafka.observe
import com.airtel.kafkapp.R
import com.airtel.kafkapp.databinding.FragmentHomeBinding
import com.airtel.kafkapp.extensions.viewModel
import com.airtel.kafkapp.feature.MainActivity
import com.airtel.kafkapp.feature.common.DataBindingFragment
import com.airtel.kafkapp.ui.ListItemSharedElementHelper
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * @author Vipul Kumar; dated 02/02/19.
 */
class HomeFragment : DataBindingFragment<FragmentHomeBinding>(
    R.layout.fragment_home
) {
    private val viewModel: BooksViewModel by viewModel()
    private lateinit var request: ServiceRequest<Resource<List<Book>>>

    private lateinit var listSharedElementHelper: ListItemSharedElementHelper

    private val controller = HomeController(
        object : HomeController.Callbacks {
            override fun onBookClicked(viewHolderId: View, item: Book) {
                (activity as MainActivity).launchDetailFragment()
            }
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvHome.apply {
            setController(controller)
            listSharedElementHelper = ListItemSharedElementHelper(this)
        }

        request = viewModel.suggestedContent("kafka").apply {
            observe(this@HomeFragment, ::onBooksFetched)
            execute()
        }
    }

    private fun onBooksFetched(it: Resource<List<Book>>?) {
        it?.let {
            logger.d("Books status ${it.status} error ${it.error} data ${it.data}")
            controller.setData(it.data)
        }
    }
}
