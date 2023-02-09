package com.kafka.reader

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.TextFile
import com.kafka.reader.pdf.PdfReader
import com.kafka.reader.text.TextReader
import org.kafka.common.extensions.elevation
import org.kafka.common.image.Icons
import org.kafka.common.widgets.IconButton
import org.kafka.common.widgets.IconResource
import org.kafka.navigation.LocalNavigator
import org.kafka.navigation.Navigator
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.material.TopBar
import ui.common.theme.theme.Dimens

@Composable
fun ReaderScreen(viewModel: ReaderViewModel = hiltViewModel()) {
    val viewState by viewModel.readerState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val file = viewState.textFile

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar(scrollState) }
    ) { padding ->
        ProvideScaffoldPadding(padding = padding) {
            when (file?.type) {
                TextFile.Type.PDF -> {
                    PdfReader(fileId = file.id)
                }

                TextFile.Type.TXT -> {
                    TextReader(fileId = file.id)
                }

                else -> {
                    viewState.download?.downloadInfo?.let { DownloadProgress(it) }
                }
            }
        }
    }
}



@Composable
private fun TopBar(
    scrollState: ScrollState,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigator: Navigator = LocalNavigator.current
) {
    TopBar(
        navigationIcon = {
            IconButton(
                onClick = { navigator.goBack() },
                modifier = Modifier.padding(Dimens.Spacing08)
            ) {
                IconResource(imageVector = Icons.Back, tint = MaterialTheme.colorScheme.primary)
            }
        },
        containerColor = Color.Transparent,
        elevation = remember { scrollState.elevation.value },
        scrollBehavior = scrollBehavior
    )
}
