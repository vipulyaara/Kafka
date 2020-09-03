package com.kafka.content.ui.language

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.kafka.content.R
import com.kafka.ui_common.base.BaseFragment
import com.kafka.ui_common.extensions.addScrollbarElevationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_language.*
import javax.inject.Inject

@AndroidEntryPoint
class LanguageFragment : BaseFragment() {
    private val languageViewModel: LanguageViewModel by viewModels()
    @Inject lateinit var languageController: LanguageController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            setController(languageController)
            addScrollbarElevationView(shadowView)
        }

        languageViewModel.liveData.observe(viewLifecycleOwner, Observer {
            languageController.setData(it)
        })

        fabDone.setOnClickListener {
            languageViewModel.onDOneClicked()
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_language, container, false)
    }

    override fun onResume() {
        super.onResume()
        languageViewModel.updateLanguages()
    }
}
