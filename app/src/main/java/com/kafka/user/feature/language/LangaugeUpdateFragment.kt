package com.kafka.user.feature.language

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.kafka.data.entities.Item
import com.kafka.user.R
import com.kafka.user.config.NightModeManager
import com.kafka.user.databinding.FragmentHomeBinding
import com.kafka.user.feature.common.DataBindingMvRxFragment
import com.kafka.user.feature.home.HomepageController
import com.kafka.user.feature.home.HomepageViewModel
import com.kafka.user.feature.home.NavigationViewModel
import com.kafka.user.feature.home.detailId
import com.kafka.user.feature.home.detailName
import com.kafka.user.feature.home.detailUrl
import com.kafka.user.ui.SharedElementHelper
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_language_update.*

/**
 * @author Vipul Kumar; dated 02/02/19.
 *
 * Fragment used to update content language preference.
 */

class LangaugeUpdateFragment : DataBindingMvRxFragment<FragmentHomeBinding>(
    R.layout.fragment_language_update
) {

    private val viewModel: LanguageViewModel by fragmentViewModel()
    private val controller = LanguageController(object : LanguageController.Callbacks {
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvLanguage.apply {
            setController(controller)
        }
    }

    override fun invalidate() {
        withState(viewModel) {
            controller.setData(it)
        }
    }
}
