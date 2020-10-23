import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ProvideTextStyle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonConstants
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.kafka.ui.theme.KafkaTheme

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonShape,
    border: BorderStroke? = null,
    contentColor: Color = KafkaTheme.colors.primary,
    disabledContentColor: Color = KafkaTheme.colors.primary,
    contentPadding: PaddingValues = ButtonConstants.DefaultContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        shape = shape,
        color = Color.Transparent,
        contentColor = if (enabled) contentColor else disabledContentColor,
        border = border,
        modifier = modifier
            .clip(shape)
            .clickable(
                onClick = onClick,
                enabled = enabled,
            )
    ) {
        ProvideTextStyle(
            value = MaterialTheme.typography.button
        ) {
            Row(
                Modifier
                    .defaultMinSizeConstraints(
                        minWidth = ButtonConstants.DefaultMinWidth,
                        minHeight = ButtonConstants.DefaultMinHeight
                    )
                    .fillMaxWidth()
                    .padding(contentPadding),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                children = content
            )
        }
    }
}

private val ButtonShape = RoundedCornerShape(percent = 50)
