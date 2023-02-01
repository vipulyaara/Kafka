package com.kafka.reader

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.kafka.common.Icons
import org.kafka.common.extensions.elevation
import org.kafka.common.widgets.IconButton
import org.kafka.common.widgets.IconResource
import org.kafka.navigation.LocalNavigator
import org.kafka.navigation.Navigator
import ui.common.theme.theme.Dimens

@Composable
fun ReaderScreen(viewModel: ReaderViewModel = hiltViewModel()) {
    val downloadItem by viewModel.downloadItem.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar(scrollState) },
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
        ) {
            if (downloadItem != null) {
                if (downloadItem!!.file.localUri != null) {
                    ReaderView(uri = downloadItem!!.file.localUri?.toUri()!!)
                } else {
                    LinearProgressIndicator(
                        progress = downloadItem!!.downloadInfo.progress,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun TextReaderView(text: String) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Text(
            modifier = Modifier.padding(Dimens.Spacing16),
            text = text,
            textAlign = TextAlign.Justify,
            style = MaterialTheme.typography.displayMedium
        )
    }
}

@Composable
private fun TopBar(
    scrollState: ScrollState,
    navigator: Navigator = LocalNavigator.current
) {
    org.kafka.ui.components.material.TopBar(
        navigationIcon = {
            IconButton(
                onClick = { navigator.goBack() },
                modifier = Modifier.padding(Dimens.Spacing08)
            ) {
                IconResource(imageVector = Icons.Back)
            }
        },
        elevation = remember { scrollState.elevation.value }
    )
}
