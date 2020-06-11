package com.kafka.ui.player

import androidx.compose.Composable
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.SimpleImage
import androidx.ui.foundation.Text
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.layout.*
import androidx.ui.layout.ColumnScope.gravity
import androidx.ui.material.Card
import androidx.ui.material.Surface
import androidx.ui.res.imageResource
import androidx.ui.unit.dp
import com.kafka.data.extensions.getRandomAuthorResource
import com.kafka.ui.*
import com.kafka.ui.R

@Composable
fun MiniPlayer(playerData: PlayerData?, modifier: Modifier = Modifier.None) {
    Surface(modifier = modifier + Modifier.fillMaxWidth(), elevation = 12.dp, color = colors().surface) {
        Row(modifier = Modifier.padding(20.dp).gravity(Alignment.CenterHorizontally)) {
            Card(modifier = Modifier.preferredSize(40.dp), shape = RoundedCornerShape(6.dp), elevation = 0.dp) {
                SimpleImage(image = imageResource(id = getRandomAuthorResource()))
            }
            Column(modifier = Modifier.paddingHV(horizontal = 12.dp).weight(1f)) {
                Text(text = playerData?.title ?: "aah ko chaahiye ik umr", style = typography().subtitle2)
                Text(text = playerData?.subtitle ?: "Mirza Ghalib", style = typography().caption)
            }

            MiniPlayerControls(modifier = Modifier.gravity(Alignment.CenterHorizontally))
        }
    }
}

@Composable
fun MiniPlayerControls(modifier: Modifier) {
    Row(modifier = modifier) {
        VectorImage(id = R.drawable.ic_pause)
        Spacer(modifier = Modifier.preferredWidth(24.dp))
        VectorImage(id = R.drawable.ic_skip_forward)
    }
}
