package org.rekhta.item.detail

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemDetail
import org.kafka.common.Icons
import org.kafka.common.extensions.rememberStateWithLifecycle
import org.kafka.common.shareText
import org.kafka.common.widgets.*
import org.rekhta.navigation.LocalNavigator
import org.rekhta.ui.components.progress.FullScreenProgressBar
import org.rekhta.ui_common_compose.shadowMaterial
import ui.common.theme.theme.iconPrimary
import ui.common.theme.theme.textPrimary
import ui.common.theme.theme.textSecondary
import ui.common.theme.theme.white

@Composable
fun ItemDetail(viewModel: ItemDetailViewModel = hiltViewModel()) {
    val state by rememberStateWithLifecycle(viewModel.state)
    val snackbarState = SnackbarHostState()
    val context = LocalContext.current

    LaunchedEffect(state.message) {
        state.message?.let { snackbarState.showSnackbar(it.message) }
    }

    DefaultScaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar() },
        color = colorScheme.surface,
        snackbarHost = { RekhtaSnackbarHost(hostState = snackbarState) }
    ) {
        Box(Modifier.fillMaxSize()) {
            FullScreenMessage(state.message, state.isFullScreenError)
            FullScreenProgressBar(show = state.isLoading && state.itemDetail == null)
            state.itemDetail?.let {
                ItemDetail(
                    itemDetail = it,
                    relatedItems = state.itemsByCreator,
                    isFavorite = state.isFavorite,
                    toggleFavorite = { viewModel.updateFavorite() },
                    shareText = { context.shareText(viewModel.shareItemText()) }
                )
            }
        }
    }
}

@Composable
fun ItemDetail(
    itemDetail: ItemDetail,
    relatedItems: List<Item>?,
    isFavorite: Boolean,
    toggleFavorite: () -> Unit,
    shareText: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        ItemDescription(itemDetail)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            FavoriteIcon(isFavorite = isFavorite) { toggleFavorite() }
            ShareIcon(onClicked = shareText)
            CallToAction(text = itemDetail.callToAction, modifier = Modifier.weight(0.5f)) {

            }
        }

        relatedItems?.let {
            Spacer(modifier = Modifier.height(24.dp))
            RelatedContent(items = it)
        }
    }
}

@Composable
private fun ItemDescription(itemDetail: ItemDetail) {
    LoadImage(
        data = itemDetail.coverImage,
        modifier = Modifier
            .size(196.dp, 248.dp)
            .shadowMaterial(12.dp, RoundedCornerShape(4.dp))
    )
    Spacer(Modifier.height(24.dp))

    Text(
        text = itemDetail.title.orEmpty(),
        style = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center),
        color = colorScheme.textPrimary,
        modifier = Modifier.padding(horizontal = 24.dp)
    )

    Spacer(Modifier.height(4.dp))

    Text(
        text = itemDetail.creator.orEmpty(),
        style = MaterialTheme.typography.titleSmall.copy(textAlign = TextAlign.Center),
        color = colorScheme.primary,
        modifier = Modifier.padding(horizontal = 24.dp)
    )

    Text(
        text = itemDetail.ratingText(colorScheme.secondary) +
                AnnotatedString(itemDetail.description.orEmpty()),
        style = MaterialTheme.typography.labelMedium,
        color = colorScheme.textSecondary,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(24.dp)
    )
}

@Composable
fun CallToAction(text: String, modifier: Modifier = Modifier, onClicked: () -> Unit) {
    Box(
        modifier = modifier
            .shadowMaterial(12.dp, RoundedCornerShape(2.dp))
            .background(colorScheme.background)
            .clickable { onClicked() }
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.titleSmall,
            color = colorScheme.textPrimary,
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 14.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
private fun FavoriteIcon(
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
    onClicked: () -> Unit
) {
    val background by animateColorAsState(if (isFavorite) colorScheme.primary else colorScheme.background)
    val iconTint by animateColorAsState(if (isFavorite) colorScheme.white else colorScheme.iconPrimary)
    val icon = if (isFavorite) Icons.HeartFilled else Icons.Heart

    Box(
        modifier = modifier
            .size(50.dp)
            .shadowMaterial(12.dp, CircleShape)
            .background(background)
            .clickable { onClicked() }
    ) {
        IconButton(onClick = onClicked, modifier = Modifier.align(Alignment.Center)) {
            IconResource(imageVector = icon, tint = iconTint)
        }
    }
}

@Composable
private fun ShareIcon(
    modifier: Modifier = Modifier,
    onClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .size(50.dp)
            .shadowMaterial(12.dp, CircleShape)
            .background(colorScheme.background)
            .clickable { onClicked() }
    ) {
        IconButton(onClick = onClicked, modifier = Modifier.align(Alignment.Center)) {
            IconResource(imageVector = Icons.Share)
        }
    }
}

@Composable
private fun TopBar() {
    val navigator = LocalNavigator.current
    SmallTopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = { navigator.back() }) {
                IconResource(imageVector = Icons.Back, tint = colorScheme.primary)
            }
        },
        actions = {
            IconButton(onClick = { }) {
                IconResource(imageVector = Icons.Sun, tint = colorScheme.primary)
            }
        }
    )
}
