@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.reader.epub.settings

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kafka.common.image.Icons
import com.kafka.common.simpleClickable
import com.kafka.reader.epub.settings.ReaderSettings.FontStyle
import com.kafka.reader.epub.settings.ReaderSettings.LineHeight
import ui.common.theme.theme.Dimens

@Composable
fun SettingsSheet(
    settings: ReaderSettings,
    language: String,
    changeSettings: (ReaderSettings) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = colorScheme.surface,
        contentColor = colorScheme.onSurface
    ) {
        Column(
            modifier = Modifier.padding(Dimens.Spacing24),
            verticalArrangement = Arrangement.spacedBy(Dimens.Spacing36)
        ) {
            TextControls(
                fontScale = settings.fontScale,
                lineHeight = settings.lineHeightType,
                textAlignment = settings.textAlignment,
                onFontScaleChange = { changeSettings(settings.copy(fontScale = it)) },
                onLineHeightChange = { changeSettings(settings.copy(lineHeightType = it)) },
                onTextAlignmentChange = { changeSettings(settings.copy(textAlignment = it)) }
            )

            FontStyle(
                fontStyle = settings.fontStyle,
                language = language,
                onClick = { changeSettings(settings.copy(fontStyle = it)) }
            )

//            MarginControl(
//                marginScale = settings.marginScale,
//                onMarginChange = { changeSettings(settings.copy(marginScale = it)) }
//            )

            Background(
                background = settings.background,
                isDarkMode = settings.isDarkMode,
                onClick = { changeSettings(settings.copy(background = it)) }
            )

            DisplayToggles(
                isDarkMode = settings.isDarkMode,
                isReadingMode = settings.isReadingMode,
                onDarkModeChange = { changeSettings(settings.copy(isDarkMode = it)) },
                onReadingModeChange = { changeSettings(settings.copy(isReadingMode = it)) }
            )
        }
    }
}

@Composable
private fun TextControls(
    fontScale: Float,
    lineHeight: LineHeight,
    textAlignment: ReaderSettings.TextAlignment,
    onFontScaleChange: (Float) -> Unit,
    onLineHeightChange: (LineHeight) -> Unit,
    onTextAlignmentChange: (ReaderSettings.TextAlignment) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Label(text = "Text")

        Spacer(modifier = Modifier.height(Dimens.Spacing12))

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Font Size Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing12)
            ) {
                IconButton(
                    onClick = {
                        val currentIndex = ReaderSettings.fontScaleOptions.indexOf(fontScale)
                        if (currentIndex > 0) {
                            onFontScaleChange(ReaderSettings.fontScaleOptions[currentIndex - 1])
                        }
                    },
                    enabled = fontScale > ReaderSettings.fontScaleOptions.first()
                ) {
                    Icon(Icons.Minus, "Decrease font size")
                }

                Text(
                    text = "${(fontScale * 100).toInt()}%",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = {
                        val currentIndex = ReaderSettings.fontScaleOptions.indexOf(fontScale)
                        if (currentIndex < ReaderSettings.fontScaleOptions.lastIndex) {
                            onFontScaleChange(ReaderSettings.fontScaleOptions[currentIndex + 1])
                        }
                    },
                    enabled = fontScale < ReaderSettings.fontScaleOptions.last()
                ) {
                    Icon(Icons.Plus, "Increase font size")
                }
            }

            Spacer(Modifier.width(Dimens.Spacing16))

            // Line Height Controls
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing12)) {
                LineHeight.entries.forEach { option ->
                    val selected = option == lineHeight
                    val color =
                        if (selected) colorScheme.primary else colorScheme.surfaceContainerHighest

                    SurfaceIcon(
                        onClick = { onLineHeightChange(option) },
                        selected = selected
                    ) {
                        val icon = when (option) {
                            LineHeight.COMPACT -> Icons.LineHeightCompact
                            LineHeight.NORMAL -> Icons.LineHeightNormal
                            LineHeight.RELAXED -> Icons.LineHeightRelaxed
                        }

                        Icon(imageVector = icon, contentDescription = option.label, tint = color)
                    }
                }
            }

            Spacer(modifier = Modifier.width(Dimens.Spacing16))

            // Text Alignment Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing12)
            ) {
                ReaderSettings.TextAlignment.entries.forEach { option ->
                    val selected = option == textAlignment
                    val color =
                        if (selected) colorScheme.primary else colorScheme.surfaceContainerHighest

                    SurfaceIcon(
                        onClick = { onTextAlignmentChange(option) },
                        selected = selected
                    ) {
                        val icon = when (option) {
                            ReaderSettings.TextAlignment.LEFT -> Icons.AlignLeft
                            ReaderSettings.TextAlignment.RIGHT -> Icons.AlignRight
                            ReaderSettings.TextAlignment.JUSTIFY -> Icons.AlignJustified
                        }

                        Icon(imageVector = icon, contentDescription = option.label, tint = color)
                    }
                }
            }
        }
    }
}

@Composable
private fun MarginControl(marginScale: Float, onMarginChange: (Float) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Label(text = "Margins")

        Spacer(modifier = Modifier.height(Dimens.Spacing12))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    val currentIndex = ReaderSettings.marginScaleOptions.indexOf(marginScale)
                    if (currentIndex > 0) {
                        onMarginChange(ReaderSettings.marginScaleOptions[currentIndex - 1])
                    }
                },
                enabled = marginScale > ReaderSettings.marginScaleOptions.first()
            ) {
                Icon(Icons.Minus, "Decrease margin")
            }

            Text(
                text = "${(marginScale * 100).toInt()}%",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = Dimens.Spacing16)
            )

            IconButton(
                onClick = {
                    val currentIndex = ReaderSettings.marginScaleOptions.indexOf(marginScale)
                    if (currentIndex < ReaderSettings.marginScaleOptions.lastIndex) {
                        onMarginChange(ReaderSettings.marginScaleOptions[currentIndex + 1])
                    }
                },
                enabled = marginScale < ReaderSettings.marginScaleOptions.last()
            ) {
                Icon(Icons.Plus, "Increase margin")
            }
        }
    }
}

// Keep existing Background and FontStyle components, but update Background to accept isDarkMode parameter
@Composable
private fun Background(
    background: ReaderSettings.Background,
    isDarkMode: Boolean,
    onClick: (ReaderSettings.Background) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Label(text = "Background")

        Spacer(modifier = Modifier.height(Dimens.Spacing12))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing12)
        ) {
            ReaderSettings.Background.getOptions(isDarkMode).forEach {
                val selected = it.color == background.color
                val borderColor = if (selected) colorScheme.primary else colorScheme.surfaceVariant
                val borderWidth = if (selected) 2.dp else 1.dp

                Surface(
                    modifier = Modifier
                        .widthIn(max = Dimens.Spacing52)
                        .aspectRatio(1f),
                    color = it.color,
                    border = BorderStroke(borderWidth, borderColor),
                    shape = RoundedCornerShape(Dimens.Radius04),
                    onClick = { onClick(it) }
                ) {}
            }
        }
    }
}

@Composable
private fun DisplayToggles(
    isDarkMode: Boolean,
    isReadingMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onReadingModeChange: (Boolean) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Label(text = "Display")

        Spacer(modifier = Modifier.height(Dimens.Spacing12))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .simpleClickable { onDarkModeChange(!isDarkMode) }
                .padding(vertical = Dimens.Spacing08),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing12)
        ) {
            Icon(
                imageVector = if (isDarkMode) Icons.DarkMode else Icons.LightMode,
                contentDescription = "Theme mode"
            )

            Text(text = "Dark mode", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.weight(1f))
            Switch(checked = isDarkMode, onCheckedChange = onDarkModeChange)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .simpleClickable { onReadingModeChange(!isReadingMode) }
                .padding(vertical = Dimens.Spacing08),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing12)
        ) {
            Icon(
                imageVector = if (isReadingMode) Icons.MenuBook else Icons.Book,
                contentDescription = "Reading mode"
            )

            Text(text = "Reading mode", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.weight(1f))
            Switch(checked = isReadingMode, onCheckedChange = onReadingModeChange)
        }
    }
}

@Composable
private fun FontStyle(fontStyle: FontStyle, language: String, onClick: (FontStyle) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Label(text = "Font Style")

        Spacer(modifier = Modifier.height(Dimens.Spacing08))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FontStyle.getOptionsForLanguage(language).forEach {
                val alpha by animateFloatAsState(if (it == fontStyle) 1f else 0.2f)

                Column(
                    modifier = Modifier.simpleClickable { onClick(it) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Aa",
                        style = MaterialTheme.typography.headlineMedium,
                        fontFamily = it.fontFamily,
                        color = sheetContentColor.copy(alpha = alpha)
                    )

                    Spacer(modifier = Modifier.height(Dimens.Spacing02))

                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.labelMedium,
                        fontFamily = it.fontFamily,
                        color = sheetContentColor.copy(alpha = alpha)
                    )
                }
            }
        }
    }
}

private val sheetContentColor
    @Composable get() = colorScheme.onSurface

@Composable
private fun Label(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Black,
        color = colorScheme.primary.copy(alpha = 0.5f)
    )
}

@Composable
private fun SurfaceIcon(
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val borderColor = if (selected) colorScheme.primary else colorScheme.surfaceContainerHighest

    Surface(
        modifier = modifier,
        border = BorderStroke(1.5.dp, borderColor),
        shape = CircleShape,
        onClick = onClick,
    ) {
        Box(Modifier.padding(Dimens.Spacing12)) {
            content()
        }
    }
}
