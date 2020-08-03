package com.kafka.content.ui.language

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.kafka.content.R
import com.kafka.content.ui.library.LibraryController
import com.kafka.content.ui.library.LibraryViewModel
import com.kafka.ui_common.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_library.*
import javax.inject.Inject

@AndroidEntryPoint
class LanguageFragment : BaseFragment() {
    private val languageViewModel: LanguageViewModel by viewModels()
    @Inject lateinit var languageController: LanguageController
    private val navController by lazy { findNavController() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            setController(languageController)
        }

        languageViewModel.liveData.observe(viewLifecycleOwner, Observer {
            languageController.setData(it)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_language, container, false)
    }

    override fun onResume() {
        super.onResume()
        languageViewModel.updateLanguages()
    }
}
