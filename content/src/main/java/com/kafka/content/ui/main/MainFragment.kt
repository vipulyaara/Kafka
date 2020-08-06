package com.kafka.content.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.data.base.extensions.debug
import com.kafka.content.R
import com.kafka.ui_common.Navigation
import com.kafka.ui_common.base.BaseFragment
import com.kafka.ui_common.navigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainFragment : BaseFragment() {
    private lateinit var navController: NavController
    private val mainViewModel: MainViewModel by viewModels()
    private val dynamicFeatureViewModel: DynamicFeatureViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpNavigation()
        initDynamicDelivery()
    }

    private fun initDynamicDelivery() {
        lifecycleScope.launchWhenCreated {
            dynamicFeatureViewModel.installReaderModule().collect {
                debug { "installing reader module: state - $it" }
                dynamicFeatureViewModel.showUserConfirmationDialog(requireActivity(), it)
            }
        }
    }

    private fun setUpNavigation() {
        navController = findNavController(requireActivity(), R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(bottom_navigation_view, navController)
        bottom_navigation_view.setOnNavigationItemSelectedListener {
            val navigation = when (it.itemId) {
                R.id.navigation_homepage -> Navigation.Homepage
                R.id.navigation_search -> Navigation.Search
                R.id.navigation_library -> Navigation.Library
                R.id.navigation_profile -> Navigation.Profile
                else -> error("Invalid bottom bar ID")
            }
            navController.navigate(navigation)
            debug { "Item selected $it" }
            true
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }
}
