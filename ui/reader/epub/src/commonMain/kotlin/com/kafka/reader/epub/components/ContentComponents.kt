package com.kafka.reader.epub.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kafka.common.toColor
import com.kafka.reader.epub.settings.ReaderSettings
import com.kafka.reader.epub.settings.font
import com.kafka.reader.epub.settings.theme
import kafka.reader.core.models.ContentElement
import kafka.reader.core.models.InlineElement
import kafka.reader.core.models.enums.TextStyle
import kafka.reader.core.models.getEffectiveStyle
import ui.common.theme.theme.Dimens

@Composable
fun TextElement(
    element: ContentElement.Text,
    settings: ReaderSettings,
    navigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val style = element.getEffectiveStyle()
    val isHeading = style in TextStyle.Heading1..TextStyle.Heading6
    val linkColor = Color(0xFF0066CC)

    val firstLineIndent = element.indentSize?.let { indent ->
        (indent * settings.fontSize.value).sp
    } ?: 0.sp

    val textStyle =
        getHeadingStyle(style).copy(textIndent = TextIndent(firstLine = firstLineIndent))

    val annotatedString = remember(element) {
        buildTextAnnotatedString(
            element = element,
            linkColor = linkColor,
            navigateToUrl = navigate
        )
    }

    Text(
        text = if (element.inlineElements.isEmpty()) AnnotatedString(element.content) else annotatedString,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = settings.horizontalMargin)
            .padding(
                vertical = if (isHeading) Dimens.Spacing24 else Dimens.Spacing08,
            ),
        style = textStyle,
        fontFamily = settings.font.fontFamily,
        fontWeight = when {
            TextStyle.Bold in element.styles -> FontWeight.Bold
            style in TextStyle.Heading1..TextStyle.Heading6 -> FontWeight.Bold
            else -> settings.font.fontWeight
        },
        fontStyle = when {
            TextStyle.Italic in element.styles -> FontStyle.Italic
            else -> FontStyle.Normal
        },
        fontSize = when {
            isHeading -> textStyle.fontSize
            else -> (settings.fontSize.value * element.sizeFactor).sp
        },
        lineHeight = when {
            isHeading -> textStyle.lineHeight
            element.lineHeight != null -> element.lineHeight?.sp ?: settings.lineHeight
            else -> settings.lineHeight
        },
        textAlign = if (isHeading) {
            TextAlign.Center
        } else {
            settings.textAlignment.asAlignment()
        },
        color = settings.theme.contentColor,
    )
}

private fun buildTextAnnotatedString(
    element: ContentElement.Text,
    linkColor: Color,
    navigateToUrl: (String) -> Unit
): AnnotatedString {
    return buildAnnotatedString {
        if (element.inlineElements.isEmpty()) {
            append(element.content)
            return@buildAnnotatedString
        }

        val content = element.content
        val length = content.length

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
            // Make start exclusive (add 1) and end inclusive (add 1)
            val adjustedStart = (inline.start + 1).coerceIn(currentIndex, length)
            val adjustedEnd = (inline.end + 1).coerceIn(adjustedStart, length)

            // Add any text before the inline element
            if (adjustedStart > currentIndex) {
                append(content.substring(currentIndex, adjustedStart))
            }

            when (inline) {
                is InlineElement.Link -> {
                    val linkText = content.substring(adjustedStart, adjustedEnd)
                    val link = LinkAnnotation.Url(
                        inline.href,
                        TextLinkStyles(SpanStyle(color = linkColor))
                    ) {
                        navigateToUrl((it as LinkAnnotation.Url).url)
                    }
                    withLink(link) {
                        append(linkText)
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
                            },
                            background = when {
                                TextStyle.Highlight in inline.styles -> Color(0xFFFFEB3B) // Default yellow highlight
                                else -> Color.Unspecified
                            }
                        )
                    ) {
                        append(content.substring(adjustedStart, adjustedEnd))
                    }
                }

                is InlineElement.BackgroundColor -> {
                    withStyle(SpanStyle(background = inline.color.toColor())) {
                        append(content.substring(adjustedStart, adjustedEnd))
                    }
                }

                is InlineElement.Color -> {
                    withStyle(SpanStyle(color = inline.color.toColor())) {
                        append(content.substring(adjustedStart, adjustedEnd))
                    }
                }

                is InlineElement.Data -> {
                    // Data elements typically contain custom attributes
                    // Just append the content without special styling
                    append(content.substring(adjustedStart, adjustedEnd))
                }

                is InlineElement.Direction -> {
                    // Handle RTL/LTR text direction
                    withStyle(
                        SpanStyle(
                            // Note: Compose currently doesn't support direct text direction styling
                            // We'll append the content as-is, and text direction should be handled
                            // at the container level
                        )
                    ) {
                        append(content.substring(adjustedStart, adjustedEnd))
                    }
                }

                is InlineElement.Tooltip -> {
                    // For tooltips, we'll just show the main content
                    // Tooltip functionality would need to be implemented at a higher level
                    append(content.substring(adjustedStart, adjustedEnd))
                }
            }

            currentIndex = adjustedEnd
        }

        // Append any remaining text
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
            .padding(horizontal = settings.horizontalMargin)
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
        fontFamily = settings.font.fontFamily,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = settings.theme.contentColor,
    )
}

@Composable
fun QuoteElement(element: ContentElement.Quote, settings: ReaderSettings) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(horizontal = settings.horizontalMargin)
            .padding(vertical = Dimens.Spacing16)
    ) {
        Box(
            modifier = Modifier
                .width(6.dp)
                .fillMaxHeight()
                .background(
                    color = settings.theme.prominentColor,
                    shape = RoundedCornerShape(Dimens.Radius04)
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = settings.horizontalMargin)
                .padding(Dimens.Spacing16)
        ) {
            Text(
                text = element.content,
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                fontFamily = settings.font.fontFamily,
                color = settings.theme.contentColor,
            )

            element.attribution?.let {
                Text(
                    text = "— $it",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.End,
                    color = settings.theme.contentColor.copy(alpha = 0.7f),
                )
            }
        }
    }
}

@Composable
fun ListElement(element: ContentElement.Listing, settings: ReaderSettings) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = settings.horizontalMargin)
            .padding(vertical = Dimens.Spacing08)
    ) {
        element.items.forEachIndexed { index, item ->
            Row(
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    text = if (element.ordered) "${index + element.startIndex}. " else "• ",
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = settings.font.fontFamily,
                    color = settings.theme.contentColor,
                )
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = settings.font.fontFamily,
                    color = settings.theme.contentColor,
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
            .padding(horizontal = settings.horizontalMargin)
            .padding(vertical = Dimens.Spacing16)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(Dimens.Spacing16)
    ) {
        element.language?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.labelSmall,
                color = settings.theme.contentColor.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        Text(
            text = element.content,
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
            fontSize = settings.fontSize,
            color = settings.theme.contentColor,
        )
    }
}
