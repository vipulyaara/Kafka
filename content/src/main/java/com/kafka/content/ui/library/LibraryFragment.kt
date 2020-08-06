package com.kafka.content.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import com.kafka.content.R
import com.kafka.ui_common.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_library.*

@AndroidEntryPoint
class LibraryFragment : BaseFragment() {
    private val libraryViewModel: LibraryViewModel by viewModels()
    private val libraryController = LibraryController()
    private val navController by lazy { findNavController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(
            MaterialSharedAxis.Z, /* forward = */ true)
        returnTransition = MaterialSharedAxis(
            MaterialSharedAxis.Z, /* forward = */ false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            setController(libraryController)
        }

        libraryViewModel.liveData.observe(viewLifecycleOwner, Observer {
            libraryController.setData(it)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_library, container, false)
    }
}
