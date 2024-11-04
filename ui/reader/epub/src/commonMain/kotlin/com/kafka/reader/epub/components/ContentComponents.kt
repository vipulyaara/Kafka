package com.kafka.reader.epub.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kafka.reader.epub.settings.ReaderSettings
import kafka.reader.core.models.ContentElement
import kafka.reader.core.models.TextStyle
import ui.common.theme.theme.Dimens

@Composable
fun TextElement(element: ContentElement.Text, settings: ReaderSettings) {
    val isHeading = element.style in TextStyle.Heading1..TextStyle.Heading6
    
    Text(
        text = element.content,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.Spacing16)
            .padding(
                vertical = if (isHeading) Dimens.Spacing24 else Dimens.Spacing08
            ),
        style = getHeadingStyle(element.style),
        fontFamily = settings.fontStyle.fontFamily,
        fontWeight = when (element.style) {
            TextStyle.Bold -> FontWeight.Bold
            in TextStyle.Heading1..TextStyle.Heading6 -> FontWeight.Bold
            else -> settings.fontStyle.fontWeight
        },
        fontStyle = when (element.style) {
            TextStyle.Italic -> FontStyle.Italic
            else -> FontStyle.Normal
        },
        fontSize = if (isHeading) getHeadingStyle(element.style).fontSize else settings.fontSize.fontSize,
        lineHeight = if (isHeading) getHeadingStyle(element.style).lineHeight else settings.fontSize.lineHeight,
        textAlign = if (isHeading) TextAlign.Start else TextAlign.Justify
    )
}

@Composable
private fun getHeadingStyle(style: TextStyle) = when (style) {
    TextStyle.Heading1 -> MaterialTheme.typography.headlineLarge
    TextStyle.Heading2 -> MaterialTheme.typography.headlineMedium
    TextStyle.Heading3 -> MaterialTheme.typography.headlineSmall
    TextStyle.Heading4,
    TextStyle.Heading5,
    TextStyle.Heading6 -> MaterialTheme.typography.titleMedium
    else -> MaterialTheme.typography.bodyMedium
}

@Composable
fun HeadingElement(element: ContentElement.Heading, settings: ReaderSettings) {
    Text(
        text = element.content,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.Spacing16)
            .padding(
                top = Dimens.Spacing24,
                bottom = Dimens.Spacing16
            ),
        style = when (element.level) {
            1 -> MaterialTheme.typography.headlineLarge
            2 -> MaterialTheme.typography.headlineMedium
            3 -> MaterialTheme.typography.headlineSmall
            else -> MaterialTheme.typography.titleMedium
        },
        fontFamily = settings.fontStyle.fontFamily,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Start
    )
}

@Composable
fun QuoteElement(element: ContentElement.Quote, settings: ReaderSettings) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.Spacing16)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(Dimens.Spacing16)
    ) {
        Text(
            text = element.content,
            style = MaterialTheme.typography.bodyMedium,
            fontStyle = FontStyle.Italic,
            fontFamily = settings.fontStyle.fontFamily
        )
        element.attribution?.let {
            Text(
                text = "— $it",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun ListElement(element: ContentElement.List, settings: ReaderSettings) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.Spacing16, vertical = Dimens.Spacing08)
    ) {
        element.items.forEachIndexed { index, item ->
            Row(
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    text = if (element.ordered) "${index + element.startIndex}. " else "• ",
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = settings.fontStyle.fontFamily
                )
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = settings.fontStyle.fontFamily
                )
            }
        }
    }
}

@Composable
fun CodeBlockElement(element: ContentElement.CodeBlock, settings: ReaderSettings) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.Spacing16)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(Dimens.Spacing16)
    ) {
        element.language?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        Text(
            text = element.content,
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
            fontSize = settings.fontSize.fontSize
        )
    }
}
