package org.kafka.item.files

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kafka.data.entities.File
import org.kafka.common.Icons
import org.kafka.common.extensions.rememberStateWithLifecycle
import org.kafka.common.widgets.DefaultScaffold
import org.kafka.common.widgets.IconResource
import org.kafka.item.detail.TopBar
import org.kafka.navigation.LeafScreen.Reader
import org.kafka.navigation.LocalNavigator
import ui.common.theme.theme.textPrimary

@Composable
fun Files(viewModel: FilesViewModel = hiltViewModel()) {
    val viewState by rememberStateWithLifecycle(viewModel.state)
    val navigator = LocalNavigator.current

    DefaultScaffold(topBar = { TopBar() }) {
        LazyColumn(modifier = Modifier) {
            items(viewState.files, key = { it.id }) {
                File(
                    it = it,
                    isLoading = viewState.isLoading,
                    startDownload = { viewModel.downloadFile(it.id) },
                    openReader = {
                        navigator.navigate(Reader.createRoute(viewModel.downloadState.value.toString()))
                    }
                )
            }
        }
    }
}

@Composable
private fun File(it: File, isLoading: Boolean, startDownload: () -> Unit, openReader: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { openReader() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = it.title.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.textPrimary,
                modifier = Modifier
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = it.extension + " - " + it.size.orEmpty(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
            )

            if (isLoading) {
                LinearProgressIndicator()
            }
        }

        IconButton(onClick = startDownload) {
            IconResource(imageVector = Icons.Download, tint = MaterialTheme.colorScheme.primary)
        }
    }

}
