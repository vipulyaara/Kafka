package com.kafka.content.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kafka.content.R
import com.kafka.content.databinding.FragmentItemDetailBinding
import com.kafka.content.databinding.FragmentLibraryBinding
import com.kafka.ui_common.navigation.Navigation
import com.kafka.ui_common.base.BaseFragment
import com.kafka.ui_common.extensions.viewBinding
import com.kafka.ui_common.navigation.navigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow

@AndroidEntryPoint
class LibraryFragment : BaseFragment() {
    private val binding by viewBinding(FragmentLibraryBinding::bind)
    private val libraryViewModel: LibraryViewModel by viewModels()
    private val libraryController = LibraryController()
    private val navController by lazy { findNavController() }
    private val actioner = Channel<LibraryAction>(Channel.BUFFERED)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            setController(libraryController)
            libraryController.actioner = actioner
        }

        libraryViewModel.liveData.observe(viewLifecycleOwner, Observer {
            libraryController.setData(it)
        })

        lifecycleScope.launchWhenCreated {
            actioner.consumeAsFlow().collect { action ->
                when (action) {
                    is LibraryAction.ItemDetailAction -> navController.navigate(Navigation.ItemDetail(action.item.itemId))
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_library, container, false)
    }
}
