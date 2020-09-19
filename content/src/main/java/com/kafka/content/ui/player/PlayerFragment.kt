package com.kafka.content.ui.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.kafka.content.R
import com.kafka.content.databinding.FragmentItemDetailBinding
import com.kafka.content.databinding.FragmentPlayerBinding
import com.kafka.ui_common.base.BaseFragment
import com.kafka.ui_common.extensions.addScrollbarElevationView
import com.kafka.ui_common.extensions.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 02/02/19.
 */
@AndroidEntryPoint
class PlayerFragment : BaseFragment() {
    private val binding by viewBinding(FragmentPlayerBinding::bind)
    private val viewModel: PlayerViewModel by viewModels()
    @Inject lateinit var playerController: PlayerController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            setController(playerController)
            addScrollbarElevationView(binding.shadowView)
            playerController.playerActioner = viewModel.pendingActions
        }

        viewModel.liveData.observe(viewLifecycleOwner, Observer {
            playerController.setData(it)
        })

        requireArguments().getString("item_id")?.let {
            viewModel.observeItemDetail(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_player, container, false)
    }
}
