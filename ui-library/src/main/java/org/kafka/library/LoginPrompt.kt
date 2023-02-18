package org.kafka.library

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.kafka.common.image.Icons
import org.kafka.navigation.LeafScreen
import org.kafka.navigation.LocalNavigator
import ui.common.theme.theme.Dimens

@Composable
internal fun LoginPrompt(modifier: Modifier = Modifier) {
    val navigator = LocalNavigator.current
    val openLogin: () -> Unit = {
        navigator.navigate(LeafScreen.Login.createRoute())
    }

    Surface(
        modifier = modifier.clickable { openLogin() },
        shape = RoundedCornerShape(Dimens.Spacing08),
        border = BorderStroke(Dimens.Spacing02, color = MaterialTheme.colorScheme.secondary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.Spacing12),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Create an account or log in to save your items",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Icon(
                imageVector = Icons.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
