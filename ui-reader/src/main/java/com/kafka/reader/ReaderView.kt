package com.kafka.reader

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.pspdfkit.configuration.activity.PdfActivityConfiguration
import com.pspdfkit.configuration.activity.UserInterfaceViewMode
import com.pspdfkit.jetpack.compose.DocumentView
import com.pspdfkit.jetpack.compose.ExperimentalPSPDFKitApi
import com.pspdfkit.jetpack.compose.rememberDocumentState

@OptIn(ExperimentalPSPDFKitApi::class)
@Composable
fun ReaderView(uri: Uri) {
    Surface {
        val hideInterfaceElements by remember { mutableStateOf(false) }
        val context = LocalContext.current

        val pdfActivityConfiguration = getPdfConfiguration(hideInterfaceElements, context)
        val documentState = rememberDocumentState(uri, pdfActivityConfiguration)

        DocumentView(
            documentState = documentState,
            modifier = Modifier
                .height(100.dp)
                .padding(16.dp)
        )
    }
}

@Composable
private fun getPdfConfiguration(
    hideInterfaceElements: Boolean,
    context: Context
) = remember(hideInterfaceElements) {
    val userInterfaceViewMode =
        if (hideInterfaceElements) UserInterfaceViewMode.USER_INTERFACE_VIEW_MODE_HIDDEN
        else UserInterfaceViewMode.USER_INTERFACE_VIEW_MODE_AUTOMATIC

    PdfActivityConfiguration.Builder(context)
        .setUserInterfaceViewMode(userInterfaceViewMode)
        .build()
}
