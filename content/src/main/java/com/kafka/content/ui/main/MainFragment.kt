package com.kafka.content.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.kafka.content.R
import com.kafka.content.databinding.FragmentMainBinding
import com.kafka.content.ui.language.LanguageFragment
import com.kafka.content.ui.language.LanguageViewModel
import com.kafka.ui_common.base.BaseFragment
import com.kafka.ui_common.extensions.setupToolbar
import com.kafka.ui_common.extensions.setupWithNavController
import com.kafka.ui_common.extensions.toggleNightMode
import com.kafka.ui_common.extensions.viewBinding
import com.kafka.ui_common.navigation.Navigation
import com.kafka.ui_common.navigation.navigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment() {
    private val binding by viewBinding(FragmentMainBinding::bind)
    private var currentNavController: LiveData<NavController>? = null
    private val languageViewModel: LanguageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setupToolbar(R.menu.menu_master) {
            when (it?.itemId) {
                R.id.menu_dark_mode -> binding.toolbar.toggleNightMode(requireActivity(), it.itemId)
            }
        }

        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val navGraphIds = listOf(
            R.navigation.nav_graph_home,
            R.navigation.nav_graph_search,
            R.navigation.nav_graph_library,
            R.navigation.nav_graph_profile
        )

        val controller = binding.bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = childFragmentManager,
            containerId = R.id.nav_host_fragment,
            intent = requireActivity().intent
        )

        controller.observe(viewLifecycleOwner, { navController ->
//            setupActionBarWithNavController(requireActivity() as AppCompatActivity, navController)
        })
        currentNavController = controller
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        if (!languageViewModel.areLanguagesSelected()) {
            currentNavController?.value!!.navigate(Navigation.LanguageSelection)
        }
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navigationHandled = currentNavController?.value?.navigateUp() ?: false
                if (!navigationHandled) {
                    if (binding.bottomNavigationView.selectedItemId == R.id.nav_graph_home) {
                        requireActivity().finish()
                    } else {
                        binding.bottomNavigationView.selectedItemId = R.id.nav_graph_home
                    }
                }
            }
        })
    }
}
