package org.rekhta.ui.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.brands.Facebook
import compose.icons.fontawesomeicons.brands.Google
import org.kafka.common.widgets.IconResource
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.brandBlue
import ui.common.theme.theme.brandRed

@Composable
fun SocialLogin(modifier: Modifier = Modifier, loginByEmail: () -> Unit) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "OR", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(Dimens.Spacing16))

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconResource(
                modifier = Modifier
                    .size(Dimens.Spacing44)
                    .clickable { loginByEmail() },
                imageVector = FontAwesomeIcons.Brands.Google,
                tint = brandRed
            )
            IconResource(
                modifier = Modifier.size(Dimens.Spacing44),
                imageVector = FontAwesomeIcons.Brands.Facebook,
                tint = brandBlue
            )
        }
    }
}
