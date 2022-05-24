package org.kafka.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import org.kafka.common.extensions.alignCenter

@Composable
fun LoginCard(modifier: Modifier = Modifier, onCardClicked: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(16.dp)
            .background(MaterialTheme.colorScheme.onSurface, RoundedCornerShape(12.dp))
            .clickable { onCardClicked() }
    ) {
        Column(
            modifier = Modifier
                .padding(36.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                style = MaterialTheme.typography.titleSmall.alignCenter(),
                text = "Login to make Rekhta yours",
                color = MaterialTheme.colorScheme.background
            )

            Button(
                modifier = Modifier.padding(top = 24.dp),
                shape = RoundedCornerShape(24.dp),
                onClick = onCardClicked,
                contentPadding = PaddingValues(vertical = 12.dp, horizontal = 24.dp)
            ) {
                Text(
                    style = MaterialTheme.typography.titleSmall.alignCenter(),
                    text = "Login to Rekhta"
                )
            }
        }
    }
}
