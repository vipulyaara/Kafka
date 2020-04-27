package com.kafka.ui.player

import android.view.ViewGroup
import androidx.compose.Composable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.AdapterList
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.SimpleImage
import androidx.ui.foundation.Text
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.layout.ColumnScope.gravity
import androidx.ui.material.Card
import androidx.ui.material.Surface
import androidx.ui.res.imageResource
import androidx.ui.unit.dp
import com.kafka.data.entities.filterMp3
import com.kafka.data.extensions.getRandomAuthorResource
import com.kafka.data.model.item.File
import com.kafka.ui.*
import com.kafka.ui.R
import com.kafka.ui.widget.ButtonSmall

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
        Stack {
            PlayerQueue(files = viewState.itemDetail?.files?.filterMp3() ?: arrayListOf(), actioner = actioner)
            MiniPlayer(modifier = Modifier.gravity(Alignment.BottomCenter), playerData = viewState.playerData)
        }
    }
}

@Composable
fun PlayerControls() {
    Row(modifier = Modifier.padding(24.dp)) {
        VectorImage(id = R.drawable.ic_pause)
        Spacer(modifier = Modifier.preferredWidth(24.dp))
        VectorImage(id = R.drawable.ic_skip_forward)
    }
}

@Composable
fun AlbumHeader(viewState: PlayerViewState) {
    Card(modifier = Modifier.fillMaxWidth(), color = colors().background) {
        Row(modifier = Modifier.padding(20.dp).gravity(ColumnAlign.Center)) {
            Card(modifier = Modifier.preferredSize(48.dp), shape = RoundedCornerShape(2.dp), elevation = 0.dp) {
                SimpleImage(image = imageResource(id = getRandomAuthorResource()))
            }
            Column(modifier = Modifier.paddingHV(horizontal = 12.dp)) {
                Text(text = viewState.itemDetail?.title ?: "", style = typography().subtitle1)
                Text(text = viewState.itemDetail?.creator ?: "", style = typography().body2)
            }
        }
    }
}
