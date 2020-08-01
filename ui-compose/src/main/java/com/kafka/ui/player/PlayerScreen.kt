package com.kafka.ui.player

import android.view.ViewGroup
import androidx.compose.Composable
import androidx.compose.state
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.ui.core.Alignment
import androidx.ui.core.ContentScale
import androidx.ui.core.Modifier
import androidx.ui.foundation.*
import androidx.ui.foundation.shape.corner.CircleShape
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.layout.*
import androidx.ui.layout.ColumnScope.gravity
import androidx.ui.material.Card
import androidx.ui.material.DrawerState
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Surface
import androidx.ui.res.imageResource
import androidx.ui.text.style.TextOverflow
import androidx.ui.unit.dp
import com.kafka.data.extensions.getRandomAuthorResource
import com.kafka.ui.*
import com.kafka.ui.R
import com.kafka.content.ui.PlayerCommand
import com.kafka.ui.widget.FakeSeekBar
import dev.chrisbanes.accompanist.mdctheme.MaterialThemeFromMdcTheme

fun ViewGroup.composePlayerScreen(
    lifecycleOwner: LifecycleOwner,
    state: LiveData<com.kafka.content.ui.player.PlayerViewState>,
    actioner: (com.kafka.content.ui.PlayerCommand) -> Unit
): Any = setContentWithLifecycle(lifecycleOwner) {
    val viewState = observe(state)
    if (viewState != null) {
        MaterialThemeFromMdcTheme {
            PlayerScreen(viewState, actioner)
        }
    }
}

@Composable
fun PlayerScreen(viewState: com.kafka.content.ui.player.PlayerViewState, actioner: (com.kafka.content.ui.PlayerCommand) -> Unit) {
    val (state, onStateChange) = state { DrawerState.Closed }

    VerticalScroller {
        Column(modifier = Modifier.fillMaxSize()) {
            PlayerNowPlaying(
                modifier = Modifier.gravity(Alignment.CenterHorizontally).padding(top = 24.dp),
                playerData = viewState.playerData
            )
            PlayerControls(
                Modifier.gravity(Alignment.CenterHorizontally).padding(vertical = 12.dp),
                playerData = viewState.playerData,
                actioner = actioner
            )

            FakeSeekBar(duration = 300)
            
            Spacer(modifier = Modifier.height(64.dp))

            PlayerQueue(files = viewState.itemDetail?.files ?: emptyList(), actioner = {})

            Spacer(modifier = Modifier.height(64.dp))
        }
    }

//    BottomDrawerLayout(
//        drawerState = state,
//        onStateChange = onStateChange,
//        drawerContent = { QueBottomDrawer(viewState, actioner) },
//        bodyContent = {
//
//        })
}

@Composable
fun PlayerNowPlaying(modifier: Modifier, playerData: com.kafka.content.ui.player.PlayerData?) {
    Column(modifier = modifier) {
        ImageCover()

        Text(
            text = playerData?.title ?: "",
            style = MaterialTheme.typography.h2.alignCenter(),
            color = colors().onPrimary,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 20.dp).padding(top = 24.dp).gravity(Alignment.CenterHorizontally)
        )

        Text(
            text = playerData?.subtitle ?: "",
            style = MaterialTheme.typography.subtitle2.alignCenter(),
            color = colors().secondary,
            modifier = Modifier.padding(
                horizontal = 20.dp,
                vertical = 2.dp
            ) + Modifier.gravity(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun PlayerControls(modifier: Modifier, playerData: com.kafka.content.ui.player.PlayerData?, actioner: (com.kafka.content.ui.PlayerCommand) -> Unit) {
    val playIcon = if (playerData?.isPlaying == true) R.drawable.ic_pause else R.drawable.ic_play
    Row(modifier = modifier + Modifier.padding(24.dp).gravity(Alignment.CenterHorizontally)) {
        val iconModifier = Modifier.padding(horizontal = 12.dp).gravity(Alignment.CenterHorizontally).weight(1f)
        VectorImage(id = R.drawable.ic_heart_sign, modifier = iconModifier)
        VectorImage(id = R.drawable.ic_step_backward, modifier = iconModifier)
        Card(shape = CircleShape, color = colors().secondary, modifier = iconModifier) {
            Box(modifier = Modifier.padding(13.dp).clickable(onClick = { actioner(com.kafka.content.ui.PlayerCommand.ToggleCurrent) })) {
                VectorImage(id = playIcon, tint = colors().background)
            }
        }
        VectorImage(id = R.drawable.ic_skip_forward, modifier = iconModifier)
        VectorImage(id = R.drawable.ic_shuffle, modifier = iconModifier)
    }
}

@Composable
fun ImageCover() {
    Card(
        modifier = Modifier.preferredSize(256.dp, 256.dp).gravity(Alignment.CenterHorizontally),
        shape = RoundedCornerShape(5.dp),
        elevation = 24.dp,
        color = colors().background
    ) {
        val image = getRandomAuthorResource()
        Image(
            asset = imageResource(id = image),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun QueBottomDrawer(
    viewState: com.kafka.content.ui.player.PlayerViewState,
    actioner: (com.kafka.content.ui.PlayerCommand) -> Unit
) {
    Surface(color = colors().surface) {
        Column {
            Spacer(modifier = Modifier.padding(top = 12.dp))
            Card(
                modifier = Modifier.gravity(Alignment.CenterHorizontally).preferredSize(56.dp, 4.dp),
                color = colors().background
            ) {}
            Spacer(modifier = Modifier.padding(top = 24.dp))
            PlayerQueue(
                files = viewState.itemDetail?.files?.filterMp3() ?: arrayListOf(),
                actioner = actioner
            )
        }
    }
}
