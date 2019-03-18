package com.kafka.user.feature.search

import android.os.Bundle
import android.view.View
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.kafka.data.entities.Item
import com.kafka.user.R
import com.kafka.user.databinding.FragmentHomeBinding
import com.kafka.user.feature.MainActivity
import com.kafka.user.feature.common.DataBindingMvRxFragment
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * @author Vipul Kumar; dated 02/02/19.
 */
class SearchFragment : DataBindingMvRxFragment<FragmentHomeBinding>(
    R.layout.fragment_home
) {

    private val viewModel: SearchViewModel by fragmentViewModel()
    private val controller = SearchController( object : SearchController.Callbacks {
        override fun onBookClicked(view: View, item: Item) {
            (activity as MainActivity).launchDetailFragment(null)
        }
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        toolbar?.visibility = View.GONE
        rvHome.apply {
            setController(controller)
        }
    }

    override fun invalidate() {
        withState(viewModel) {
            controller.setData(it)
        }
    }
}
