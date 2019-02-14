package com.airtel.kafkapp.feature.search

import android.os.Bundle
import android.view.View
import com.airtel.data.entities.Book
import com.airtel.kafkapp.R
import com.airtel.kafkapp.databinding.FragmentSearchBinding
import com.airtel.kafkapp.feature.common.DataBindingFragment
import kotlinx.android.synthetic.main.fragment_search.*

/**
 * @author Vipul Kumar; dated 02/02/19.
 */
class SearchFragment : DataBindingFragment<FragmentSearchBinding>(
    R.layout.fragment_search
) {
    private val controller = SearchController()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvSearch.apply {
            setController(controller)
        }

        controller.setData(Book(bookId = ""))
    }
}
