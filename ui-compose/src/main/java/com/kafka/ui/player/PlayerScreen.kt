package com.kafka.ui.player

import android.view.ViewGroup
import androidx.compose.Composable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.Image
import androidx.ui.foundation.Text
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.shape.corner.CircleShape
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.ScaleFit
import androidx.ui.layout.*
import androidx.ui.layout.ColumnScope.gravity
import androidx.ui.material.Card
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Surface
import androidx.ui.res.imageResource
import androidx.ui.text.style.TextOverflow
import androidx.ui.unit.dp
import com.kafka.data.entities.filterMp3
import com.kafka.data.extensions.getRandomAuthorResource
import com.kafka.ui.*
import com.kafka.ui.R

fun ViewGroup.composePlayerScreen(
    lifecycleOwner: LifecycleOwner,
    state: LiveData<PlayerViewState>,
    actioner: (PlayerAction) -> Unit
): Any = setContentWithLifecycle(lifecycleOwner) {
    val viewState = observe(state)
    if (viewState != null) {
        MaterialThemeFromAndroidTheme(context) {
            PlayerScreen(viewState, actioner)
        }
    }
}

@Composable
fun PlayerScreen(viewState: PlayerViewState, actioner: (PlayerAction) -> Unit) {
    Surface(color = colors().background) {
        VerticalScroller {
            Column {
                PlayerNowPlaying(modifier = Modifier.gravity(ColumnAlign.Center).padding(top = 64.dp))
                PlayerControls(Modifier.gravity(ColumnAlign.Center).paddingHV(vertical = 24.dp))
                Surface(color = colors().surface) {
                    Column {
                        Spacer(modifier = Modifier.padding(top = 12.dp))
                        Card(
                            modifier = Modifier.gravity(ColumnAlign.Center).preferredSize(56.dp, 4.dp),
                            color = colors().background
                        ) {}
                        Spacer(modifier = Modifier.padding(top = 24.dp))
                        PlayerQueue(
                            files = viewState.itemDetail?.files?.filterMp3() ?: arrayListOf(),
                            actioner = actioner
                        )
                    }
                }
//            MiniPlayer(modifier = Modifier.None, playerData = viewState.playerData)
        }
        }
    }
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
            modifier = Modifier.paddingHV(horizontal = 20.dp).padding(top = 24.dp).gravity(ColumnAlign.Center)
        )

        Text(
            text = "by Mirza Ghalib",
            style = MaterialTheme.typography.h3.alignCenter(),
            modifier = Modifier.paddingHV(
                horizontal = 20.dp,
                vertical = 2.dp
            ) + Modifier.gravity(ColumnAlign.Center)
        )
    }
}

@Composable
fun PlayerControls(modifier: Modifier) {
    Row(modifier = modifier + Modifier.padding(24.dp).gravity(ColumnAlign.Center)) {
        val iconModifier = Modifier.paddingHV(horizontal = 12.dp).gravity(ColumnAlign.Center).weight(1f)
        VectorImage(id = R.drawable.ic_heart_sign, modifier = iconModifier)
        VectorImage(id = R.drawable.ic_step_backward, modifier = iconModifier)
        Card(shape = CircleShape, color = colors().secondary, modifier = iconModifier) {
            Box(modifier = Modifier.padding(12.dp)) {
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
        modifier = Modifier.preferredSize(196.dp, 196.dp).gravity(ColumnAlign.Center),
        shape = RoundedCornerShape(5.dp),
        elevation = 0.dp,
        color = colors().background
    ) {
        val image = getRandomAuthorResource()
        Image(
            asset = imageResource(id = image),
            scaleFit = ScaleFit.FillHeight
        )
    }
}
