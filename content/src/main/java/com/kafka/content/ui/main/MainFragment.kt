package com.kafka.content.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.data.base.extensions.debug
import com.kafka.content.R
import com.kafka.ui_common.base.BaseFragment
import com.kafka.ui_common.extensions.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainFragment : BaseFragment() {
    private var currentNavController: LiveData<NavController>? = null
    private val mainViewModel: MainViewModel by viewModels()
    private val dynamicFeatureViewModel: DynamicFeatureViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBottomNavigationBar()
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

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        val navGraphIds = listOf(
            R.navigation.nav_graph_home,
            R.navigation.nav_graph_search,
            R.navigation.nav_graph_library,
            R.navigation.nav_graph_profile
        )

        val controller = bottom_navigation_view.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = childFragmentManager,
            containerId = R.id.nav_host_fragment,
            intent = requireActivity().intent
        )

        controller.observe(viewLifecycleOwner, Observer { navController ->
//            setupActionBarWithNavController(requireActivity() as AppCompatActivity, navController)
        })
        currentNavController = controller
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onBackPressed(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }
}
