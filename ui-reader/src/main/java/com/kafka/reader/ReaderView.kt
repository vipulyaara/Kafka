package com.kafka.reader

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.pspdfkit.configuration.activity.PdfActivityConfiguration
import com.pspdfkit.configuration.activity.UserInterfaceViewMode
import com.pspdfkit.configuration.page.PageLayoutMode
import com.pspdfkit.configuration.page.PageScrollDirection
import com.pspdfkit.configuration.page.PageScrollMode
import com.pspdfkit.configuration.theming.ThemeMode
import com.pspdfkit.jetpack.compose.DocumentView
import com.pspdfkit.jetpack.compose.ExperimentalPSPDFKitApi
import com.pspdfkit.jetpack.compose.rememberDocumentState
import org.kafka.common.animation.Delayed

@OptIn(ExperimentalPSPDFKitApi::class)
@Composable
fun ReaderView(uri: Uri) {
    Surface {
        val hideInterfaceElements by remember { mutableStateOf(true) }
        val context = LocalContext.current

        val pdfActivityConfiguration = getPdfConfiguration(hideInterfaceElements, context)
        val documentState = rememberDocumentState(uri, pdfActivityConfiguration)

        Delayed(delayMillis = 500) {
            DocumentView(
                documentState = documentState,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun getPdfConfiguration(
    hideInterfaceElements: Boolean,
    context: Context,
    theme: ThemeMode = if (isSystemInDarkTheme()) ThemeMode.NIGHT else ThemeMode.DEFAULT
) = remember(hideInterfaceElements) {
    val userInterfaceViewMode =
        if (hideInterfaceElements) UserInterfaceViewMode.USER_INTERFACE_VIEW_MODE_HIDDEN
        else UserInterfaceViewMode.USER_INTERFACE_VIEW_MODE_AUTOMATIC

    PdfActivityConfiguration.Builder(context)
        .setUserInterfaceViewMode(userInterfaceViewMode)
        .scrollMode(PageScrollMode.CONTINUOUS)
        .scrollDirection(PageScrollDirection.VERTICAL)
        .layoutMode(PageLayoutMode.SINGLE)
        .disableAnnotationList()
        .themeMode(theme)
        .hideNavigationButtons()
        .hideThumbnailGrid()
        .disableAnnotationEditing()
        .disableBookmarkList()
        .disableBookmarkEditing()
        .enableSearch()
        .build()
}
