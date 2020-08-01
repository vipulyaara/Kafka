package com.kafka.content.ui.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.kafka.content.R
import com.kafka.ui_common.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_player.*

/**
 * @author Vipul Kumar; dated 02/02/19.
 */
@AndroidEntryPoint
class PlayerFragment : BaseFragment() {
    private val viewModel: PlayerViewModel by viewModels()
    private val playerController = PlayerController()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            setController(playerController)
        }

        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            playerController.setData(it)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_player, container, false)
    }
}
