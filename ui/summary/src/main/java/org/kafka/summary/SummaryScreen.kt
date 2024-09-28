package org.kafka.summary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.jeziellago.compose.markdowntext.MarkdownText
import org.kafka.common.simpleClickable
import org.kafka.navigation.LocalNavigator
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.material.BackButton
import org.kafka.ui.components.material.TopBar
import org.kafka.ui.components.scaffoldPadding
import ui.common.theme.theme.Dimens

@Composable
fun SummaryScreen(viewModel: SummaryViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navigator = LocalNavigator.current

    Scaffold(topBar = {
        TopBar(
            containerColor = Color.Transparent,
            navigationIcon = { BackButton { navigator.goBack() } })
    }) { padding ->
        ProvideScaffoldPadding(padding = padding) {
            Summary(state = state, goToCreator = viewModel::goToCreator)
        }
    }
}

@Composable
private fun Summary(
    state: SummaryState,
    modifier: Modifier = Modifier,
    goToCreator: (String) -> Unit,
) {
    Column(
        modifier
            .verticalScroll(rememberScrollState())
            .padding(scaffoldPadding())
            .padding(horizontal = Dimens.Spacing24)
    ) {
        if (state.title.isNotEmpty()) {
            SummaryHeader(title = state.title, creator = state.creator, goToCreator = goToCreator)
        }

        if (state.summary != null) {
            MarkdownText(
                modifier = Modifier.padding(bottom = Dimens.Spacing24),
                markdown = state.summary.content,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                )
            )
        }
    }
}

@Composable
private fun SummaryHeader(title: String?, creator: String?, goToCreator: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title.orEmpty(),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(Dimens.Spacing04))

        if (creator != null) {
            Text(
                text = creator,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.simpleClickable { goToCreator(creator) }
            )
        }

        Spacer(modifier = Modifier.height(Dimens.Spacing24))

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(Dimens.Spacing24))
    }
}
