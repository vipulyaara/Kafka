package com.kafka.reader.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import com.kafka.reader.R
import com.kafka.ui_common.base.BaseFragment
import com.pdftron.pdf.config.ViewerConfig
import com.pdftron.pdf.controls.DocumentActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReaderFragment : BaseFragment() {
    private val viewModel: ReaderViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val config = ViewerConfig.Builder()
            .openUrlCachePath(requireContext().cacheDir.absolutePath)
            .fullscreenModeEnabled(true)
            .multiTabEnabled(false)
            .documentEditingEnabled(false)
            .longPressQuickMenuEnabled(true)
            .toolbarTitle("")
            .showSearchView(true)
            .build()
        DocumentActivity.openDocument(requireContext(), requireArguments().getString("item_url")?.toUri(), config)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_webview, container, false)
    }
}
