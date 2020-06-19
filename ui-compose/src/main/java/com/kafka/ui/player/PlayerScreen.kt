package com.kafka.ui.player

import android.view.ViewGroup
import androidx.compose.Composable
import androidx.compose.state
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.ui.core.Alignment
import androidx.ui.core.ContentScale
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.Image
import androidx.ui.foundation.Text
import androidx.ui.foundation.shape.corner.CircleShape
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.layout.*
import androidx.ui.layout.ColumnScope.gravity
import androidx.ui.material.*
import androidx.ui.res.imageResource
import androidx.ui.text.style.TextOverflow
import androidx.ui.unit.dp
import com.kafka.data.entities.filterMp3
import com.kafka.data.extensions.getRandomAuthorResource
import com.kafka.ui.*
import com.kafka.ui.R
import dev.chrisbanes.accompanist.mdctheme.MaterialThemeFromMdcTheme

fun ViewGroup.composePlayerScreen(
    lifecycleOwner: LifecycleOwner,
    state: LiveData<PlayerViewState>,
    actioner: (PlayerAction) -> Unit
): Any = setContentWithLifecycle(lifecycleOwner) {
    val viewState = observe(state)
    if (viewState != null) {
        MaterialThemeFromMdcTheme {
            PlayerScreen(viewState, actioner)
        }
    }
}

@Composable
fun PlayerScreen(viewState: PlayerViewState, actioner: (PlayerAction) -> Unit) {
    val (state, onStateChange) = state { DrawerState.Closed }
    BottomDrawerLayout(
        drawerState = state,
        onStateChange = onStateChange,
        drawerContent = { QueBottomDrawer(viewState, actioner) },
        bodyContent = {
            Stack(modifier = Modifier.fillMaxSize()) {
                Column {
                    PlayerNowPlaying(modifier = Modifier.gravity(Alignment.CenterHorizontally).padding(top = 64.dp))
                    PlayerControls(Modifier.gravity(Alignment.CenterHorizontally).paddingHV(vertical = 24.dp))
                }
                MiniPlayer(modifier = Modifier.gravity(Alignment.BottomCenter), playerData = viewState.playerData)
            }
        })

}

@Composable
fun PlayerNowPlaying(modifier: Modifier) {
    Column(modifier = modifier) {
        ImageCover()

        Text(
            text = "Aah ko chahiye ik umr",
            style = MaterialTheme.typography.h2.alignCenter(),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.paddingHV(horizontal = 20.dp).padding(top = 24.dp).gravity(Alignment.CenterHorizontally)
        )

        Text(
            text = "by Mirza Ghalib",
            style = MaterialTheme.typography.subtitle2.alignCenter().copy(color = colors().primary),
            modifier = Modifier.paddingHV(
                horizontal = 20.dp,
                vertical = 2.dp
            ) + Modifier.gravity(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun PlayerControls(modifier: Modifier) {
    Row(modifier = modifier + Modifier.padding(24.dp).gravity(Alignment.CenterHorizontally)) {
        val iconModifier = Modifier.paddingHV(horizontal = 12.dp).gravity(Alignment.CenterHorizontally).weight(1f)
        VectorImage(id = R.drawable.ic_heart_sign, modifier = iconModifier)
        VectorImage(id = R.drawable.ic_step_backward, modifier = iconModifier)
        Card(shape = CircleShape, color = colors().secondary, modifier = iconModifier) {
            Box(modifier = Modifier.padding(13.dp)) {
                VectorImage(id = R.drawable.ic_pause, tint = colors().background)
            }
        }
        VectorImage(id = R.drawable.ic_skip_forward, modifier = iconModifier)
        VectorImage(id = R.drawable.ic_shuffle, modifier = iconModifier)
    }
}

@Composable
fun ImageCover() {
    Card(
        modifier = Modifier.preferredSize(196.dp, 196.dp).gravity(Alignment.CenterHorizontally),
        shape = RoundedCornerShape(5.dp),
        elevation = 0.dp,
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
    viewState: PlayerViewState,
    actioner: (PlayerAction) -> Unit
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
