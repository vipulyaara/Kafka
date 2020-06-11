package com.kafka.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.kafka.ui.player.composePlayerScreen
import com.kafka.ui_common.BaseFragment

/**
 * @author Vipul Kumar; dated 02/02/19.
 */
class PlayerFragment : BaseFragment() {
    private val viewModel: PlayerViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.viewState.observe(viewLifecycleOwner, Observer { })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FrameLayout(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            composePlayerScreen(viewLifecycleOwner, viewModel.viewState, viewModel::submitAction)
        }
    }
}
