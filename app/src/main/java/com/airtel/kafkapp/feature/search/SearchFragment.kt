package com.airtel.kafkapp.feature.search

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.airtel.data.entities.Item
import com.airtel.kafkapp.R
import com.airtel.kafkapp.databinding.FragmentHomeBinding
import com.airtel.kafkapp.feature.MainActivity
import com.airtel.kafkapp.feature.common.BaseActivity
import com.airtel.kafkapp.feature.common.BaseEpoxyController
import com.airtel.kafkapp.feature.common.DataBindingMvRxFragment
import com.airtel.kafkapp.feature.home.HomepageController
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

        toolbar?.visibility = View.GONE
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
