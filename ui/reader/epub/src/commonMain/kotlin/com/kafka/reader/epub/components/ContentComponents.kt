@file:OptIn(ExperimentalTime::class)

package com.kafka.reader.epub.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kafka.base.debug
import com.kafka.common.toColor
import com.kafka.reader.epub.settings.ReaderSettings
import com.kafka.reader.epub.settings.font
import com.kafka.reader.epub.settings.theme
import kafka.reader.core.models.ContentElement
import kafka.reader.core.models.InlineElement
import kafka.reader.core.models.enums.TextAlignment
import kafka.reader.core.models.enums.TextStyle
import kafka.reader.core.models.getEffectiveStyle
import kotlinx.datetime.Clock
import ui.common.theme.theme.Dimens
import kotlin.time.Clock.*
import kotlin.time.ExperimentalTime

@Composable
fun TextElement(
    element: ContentElement.Text,
    settings: ReaderSettings,
    modifier: Modifier = Modifier,
    toggleContextMenu: (String?) -> Unit = { },
    navigate: (String) -> Unit,
) {
    val style = element.getEffectiveStyle()
    val isHeading = style in TextStyle.Heading1..TextStyle.Heading6
    val linkColor = Color(0xFF0066CC)

    val firstLineIndent = element.indentSize?.let { indent ->
        (indent * settings.fontSize.value).sp
    } ?: 0.sp

    val textStyle = getHeadingStyle(style)
        .copy(textIndent = TextIndent(firstLine = firstLineIndent))

    val annotatedString = remember(element) {
        buildTextAnnotatedString(
            element = element,
            linkColor = linkColor,
            navigateToUrl = navigate
        )
    }

    val textContent = if (element.inlineElements.isEmpty()) {
        AnnotatedString(element.content)
    } else {
        annotatedString
    }

    var tapCount by remember { mutableStateOf(0) }
    var lastTapTime by remember { mutableStateOf(0L) }
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    
    // Store the selected sentence range
    var selectedSentenceRange by remember { mutableStateOf<IntRange?>(null) }
    
    // Highlight color for the selected sentence
    val highlightColor = Color(0x330066CC) // Light blue with 20% opacity

    fun findSentenceBoundaries(text: String, position: Int): IntRange {
        if (position < 0 || position >= text.length) return IntRange(0, 0)
        
        var start = position
        var end = position
        
        // Find start of sentence (looking for .!? followed by space)
        while (start > 0) {
            if (start < text.length - 1 && 
                text[start - 1] in listOf('.', '!', '?') && 
                text[start].isWhitespace()) {
                break
            }
            start--
        }
        
        // Skip leading whitespace
        while (start < text.length && text[start].isWhitespace()) {
            start++
        }
        
        // Find end of sentence
        while (end < text.length) {
            if (text[end] in listOf('.', '!', '?')) {
                end++
                break
            }
            end++
        }
        
        return IntRange(start, end)
    }

    LaunchedEffect(selectedSentenceRange) {
        val selectedText = selectedSentenceRange?.let { range ->
            if (range.first < range.last) {
                textContent.substring(range.first, range.last)
            } else null
        }
        
        toggleContextMenu(selectedText)
    }

    LaunchedEffect(Unit) {
        debug { "TextElement initialized for content: ${element.content.take(50)}..." }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = settings.horizontalMargin)
            .padding(vertical = if (isHeading) Dimens.Spacing24 else Dimens.Spacing08)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset ->
                        val currentTime = System.now().toEpochMilliseconds()
                        debug { "Tap detected at $offset" }
                        
                        if (currentTime - lastTapTime < 300) { // Double tap threshold
                            tapCount++
                        } else {
                            tapCount = 1
                        }
                        lastTapTime = currentTime

                        // Get the character position from the tap offset
                        textLayoutResult?.let { result ->
                            try {
                                val position = result.getOffsetForPosition(offset)
                                debug { "Tap detected at position: $position" }
                                
                                // Check if we're tapping on an existing selection
                                val tappedOnSelection = selectedSentenceRange?.contains(position) == true
                                
                                if (tappedOnSelection) {
                                    // Clear the selection if tapping on an existing selection
                                    selectedSentenceRange = null
                                    debug { "Cleared selection" }
                                } else {
                                    // Get sentence boundaries and set selection
                                    val sentenceRange = findSentenceBoundaries(textContent.toString(), position)
                                    debug { "Sentence range: $sentenceRange" }
                                    
                                    // Set selection to the sentence
                                    if (sentenceRange.first < sentenceRange.last) {
                                        selectedSentenceRange = sentenceRange
                                        debug { "Selected sentence: ${sentenceRange.first}-${sentenceRange.last}" }
                                    }
                                }
                            } catch (e: Exception) {
                                debug { "Error getting text position: ${e.message}" }
                            }
                        } ?: debug { "TextLayoutResult is null, cannot determine tap position" }
                    }
                )
            }
    ) {
        // Use a simple Text composable
        Text(
            text = textContent,
            style = textStyle.copy(
                color = settings.theme.contentColor,
                hyphens = Hyphens.Auto,
                fontFeatureSettings = element.styles
                    .takeIf { TextStyle.SmallCaps in it }
                    ?.also { debug { "Annotated string: Small caps found for element: $element" } }
                    ?.let { "smcp" },
                fontFamily = if (TextStyle.SmallCaps !in element.styles) settings.font.fontFamily else null,
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
                textAlign = if (isHeading || element.alignment == TextAlignment.CENTER) {
                    TextAlign.Center
                } else {
                    settings.textAlignment.asAlignment()
                },
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(settings.theme.backgroundColor),
            onTextLayout = { result -> 
                textLayoutResult = result
                debug { "Text layout updated, line count: ${result.lineCount}" }
            }
        )
    }
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
                    if (inline.styles.contains(TextStyle.SmallCaps)) {
                        debug { "Annotated string: Small caps found for element: $element" }
                    }
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
                            fontFeatureSettings = when {
                                TextStyle.SmallCaps in inline.styles -> "smcp".also {
                                    debug { "Small caps applied" }
                                }

                                else -> null
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

                is InlineElement.Highlight -> {
                    withStyle(
                        SpanStyle(
                            background = inline.color.toColor().copy(alpha = 0.3f),
                            textDecoration = if (inline.note != null) TextDecoration.Underline else null
                        )
                    ) {
                        append(content.substring(adjustedStart, adjustedEnd))
                    }
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
