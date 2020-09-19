package com.kafka.content.ui.language

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.kafka.content.R
import com.kafka.content.databinding.FragmentLanguageBinding
import com.kafka.ui_common.base.BaseFragment
import com.kafka.ui_common.extensions.addScrollbarElevationView
import com.kafka.ui_common.extensions.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LanguageFragment : BaseFragment() {
    private val binding by viewBinding(FragmentLanguageBinding::bind)
    private val languageViewModel: LanguageViewModel by viewModels()
    @Inject lateinit var languageController: LanguageController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            setController(languageController)
            addScrollbarElevationView(binding.shadowView)
        }

        languageViewModel.liveData.observe(viewLifecycleOwner, Observer {
            languageController.setData(it)
        })

        binding.fabDone.setOnClickListener {
            languageViewModel.onDoneClicked()
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_language, container, false)
    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return FrameLayout(requireContext()).apply {
//            layoutParams =
//                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//
//            composeLanguageSelection()
//        }
//    }

    override fun onResume() {
        super.onResume()
        languageViewModel.updateLanguages()
    }
}
