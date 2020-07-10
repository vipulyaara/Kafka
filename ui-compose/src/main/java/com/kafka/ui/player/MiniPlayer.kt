package com.kafka.ui.player

import androidx.compose.Composable
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.foundation.clickable
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.layout.*
import androidx.ui.layout.ColumnScope.gravity
import androidx.ui.material.Card
import androidx.ui.material.Surface
import androidx.ui.unit.dp
import com.kafka.data.extensions.getRandomAuthorResource
import com.kafka.ui.*
import com.kafka.ui.R
import com.kafka.ui.actions.PlayerCommand
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun MiniPlayer(playerData: PlayerData, modifier: Modifier = Modifier, actioner: (PlayerCommand) -> Unit) {
    if (!playerData.isValid()) return
    Surface(modifier = modifier + Modifier.fillMaxWidth(), elevation = 12.dp, color = colors().background) {
        Row(modifier = Modifier.padding(20.dp).gravity(Alignment.CenterHorizontally)) {
            Card(modifier = Modifier.preferredSize(40.dp), shape = RoundedCornerShape(6.dp), elevation = 0.dp) {
                CoilImage(data = getRandomAuthorResource())
            }
            Column(modifier = Modifier.padding(horizontal = 12.dp).weight(1f)) {
                Text(text = playerData.title ?: "No title", style = typography().subtitle2, maxLines = 1)
                Text(text = playerData.subtitle ?: "No subtitle", style = typography().caption.alignCenter(), maxLines = 1)
            }

            MiniPlayerControls(
                modifier = Modifier.gravity(Alignment.CenterHorizontally),
                playerData = playerData,
                actioner = actioner
            )
        }
    }
}

@Composable
fun MiniPlayerControls(modifier: Modifier, playerData: PlayerData, actioner: (PlayerCommand) -> Unit) {
    Row(modifier = modifier) {
        VectorImage(modifier = Modifier.clickable(onClick = { actioner(PlayerCommand.ToggleCurrent) }), id = playerData.playIcon())
        Spacer(modifier = Modifier.preferredWidth(24.dp))
        VectorImage(id = R.drawable.ic_skip_forward)
    }
}
