package com.kafka.reader.epub.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kafka.reader.epub.settings.ReaderSettings
import kafka.reader.core.models.ContentElement
import kafka.reader.core.models.InlineElement
import kafka.reader.core.models.TextStyle
import ui.common.theme.theme.Dimens

@Composable
fun TextElement(element: ContentElement.Text, settings: ReaderSettings) {
    val isHeading = element.style in TextStyle.Heading1..TextStyle.Heading6
    val uriHandler = LocalUriHandler.current
    val linkColor = if (isSystemInDarkTheme()) {
        Color(0xFF66B2FF)
    } else {
        Color(0xFF0066CC)
    }

    val annotatedString = remember(element) {
        buildTextAnnotatedString(element, linkColor, uriHandler)
    }

    Text(
        text = if (element.inlineElements.isEmpty()) AnnotatedString(element.content) else annotatedString,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.Spacing16)
            .padding(
                vertical = if (isHeading) Dimens.Spacing24 else Dimens.Spacing08
            ),
        style = getHeadingStyle(element.style),
        fontFamily = settings.fontStyle.fontFamily,
        fontWeight = when {
            TextStyle.Bold in element.styles -> FontWeight.Bold
            element.style in TextStyle.Heading1..TextStyle.Heading6 -> FontWeight.Bold
            else -> settings.fontStyle.fontWeight
        },
        fontStyle = when {
            TextStyle.Italic in element.styles -> FontStyle.Italic
            else -> FontStyle.Normal
        },
        fontSize = when {
            isHeading -> getHeadingStyle(element.style).fontSize
            else -> (settings.fontSize.fontSize.value * element.sizeFactor).sp
        },
        lineHeight = when {
            isHeading -> getHeadingStyle(element.style).lineHeight
            element.lineHeight != null -> element.lineHeight?.sp ?: settings.fontSize.lineHeight
            else -> settings.fontSize.lineHeight
        },
        textAlign = if (isHeading) {
            TextAlign.Center
        } else {
            TextAlign.Justify
//            when (element.alignment) {
//                TextAlignment.LEFT -> TextAlign.Start
//                TextAlignment.CENTER -> TextAlign.Center
//                TextAlignment.RIGHT -> TextAlign.End
//                TextAlignment.JUSTIFY -> TextAlign.Justify
//            }
        },
        color = contentColorFor(settings.background.color)
//        color = element.color?.let { Rgb(it) Color(Color.parseColor(it)) }
//            ?: MaterialTheme.colorScheme.onBackground,
//        letterSpacing = element.letterSpacing?.sp ?: TextUnit.Unspecified,
//        background = element.backgroundColor?.let {
//            Color(android.graphics.Color.parseColor(it))
//        }
    )
}

private fun buildTextAnnotatedString(
    element: ContentElement.Text,
    linkColor: Color,
    uriHandler: UriHandler
): AnnotatedString {
    return buildAnnotatedString {
        if (element.inlineElements.isEmpty()) {
            append(element.content)
            return@buildAnnotatedString
        }

        val content = element.content
        val length = content.length
        
        // Sort and validate inline elements
        val sortedInlines = element.inlineElements
            .sortedBy { it.start }
            .filter { inline ->
                // Filter out invalid inline elements
                inline.start >= 0 && 
                inline.end <= length && 
                inline.start < inline.end
            }
        
        var currentIndex = 0
        
        sortedInlines.forEach { inline ->
            val safeStart = inline.start.coerceIn(currentIndex, length)
            val safeEnd = inline.end.coerceIn(safeStart, length)
            
            // Add text before the inline element if there is any
            if (safeStart > currentIndex) {
                append(content.substring(currentIndex, safeStart))
            }

            // Apply the appropriate style based on inline element type
            when (inline) {
                is InlineElement.Link -> {
                    val link = LinkAnnotation.Url(
                        inline.href,
                        TextLinkStyles(SpanStyle(color = linkColor))
                    ) {
                        val url = (it as LinkAnnotation.Url).url
                        uriHandler.openUri(url)
                    }
                    withLink(link) {
                        append(content.substring(safeStart, safeEnd))
                    }
                }

                is InlineElement.Style -> {
                    withStyle(
                        SpanStyle(
                            fontWeight = if (TextStyle.Bold in inline.styles) FontWeight.Bold else null,
                            fontStyle = if (TextStyle.Italic in inline.styles) FontStyle.Italic else null,
                            textDecoration = when {
                                TextStyle.Underline in inline.styles -> TextDecoration.Underline
                                TextStyle.Strikethrough in inline.styles -> TextDecoration.LineThrough
                                else -> null
                            },
                            fontFamily = when {
                                TextStyle.Monospace in inline.styles -> FontFamily.Monospace
                                else -> null
                            },
                            baselineShift = when {
                                TextStyle.Subscript in inline.styles -> BaselineShift.Subscript
                                TextStyle.Superscript in inline.styles -> BaselineShift.Superscript
                                else -> null
                            },
                            fontSize = when {
                                TextStyle.SmallCaps in inline.styles -> 12.sp
                                else -> 14.sp
                            }
                        )
                    ) {
                        append(content.substring(safeStart, safeEnd))
                    }
                }

                is InlineElement.BackgroundColor -> {}
                is InlineElement.Color -> {}
            }
            
            currentIndex = safeEnd
        }

        // Add remaining text after the last inline element
        if (currentIndex < length) {
            append(content.substring(currentIndex))
        }
    }
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
            fontFamily = settings.fontStyle.fontFamily,
            color = MaterialTheme.colorScheme.onSurfaceVariant
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
fun ListElement(element: ContentElement.Listing, settings: ReaderSettings) {
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
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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

