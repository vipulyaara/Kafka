@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.reader.epub.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kafka.common.image.Icons
import com.kafka.common.simpleClickable
import com.kafka.reader.epub.settings.ReaderFont
import com.kafka.reader.epub.settings.ReaderSettings
import com.kafka.reader.epub.settings.ReaderSettings.LineHeight
import com.kafka.reader.epub.settings.ReaderTheme
import com.kafka.reader.epub.settings.font
import com.kafka.reader.epub.settings.theme
import kafka.ui.reader.epub.generated.resources.Res
import kafka.ui.reader.epub.generated.resources.horizontal_navigation
import kafka.ui.reader.epub.generated.resources.horizontal_navigation_text
import org.jetbrains.compose.resources.stringResource
import ui.common.theme.theme.Dimens

@Composable
fun SettingsSheet(
    settings: ReaderSettings,
    language: String,
    changeSettings: (ReaderSettings) -> Unit
) {
    Column(
        modifier = Modifier
            .background(colorScheme.surface)
            .padding(vertical = Dimens.Spacing48),
        verticalArrangement = Arrangement.spacedBy(Dimens.Spacing36)
    ) {
        TextControls(
            lineHeight = settings.lineHeightType,
            textAlignment = settings.textAlignment,
            onLineHeightChange = { changeSettings(settings.copy(lineHeightType = it)) },
            onTextAlignmentChange = { changeSettings(settings.copy(textAlignment = it)) }
        )

        ThemeSelector(
            currentTheme = settings.theme,
            onThemeChange = { changeSettings(settings.copy(themeKey = it.key)) }
        )

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = Dimens.Gutter),
            horizontalArrangement = Arrangement.spacedBy(Dimens.Gutter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FontStyle(
                selectedFont = settings.font,
                language = language,
                onClick = { changeSettings(settings.copy(fontStyleKey = it.key)) }
            )
            FontMarginSize(
                fontScale = settings.fontScale,
                onFontScaleChange = { changeSettings(settings.copy(fontScale = it)) },
                marginScale = settings.marginScale,
                onMarginChange = { changeSettings(settings.copy(marginScale = it)) }
            )
        }
    }
}

@Composable
private fun TextControls(
    lineHeight: LineHeight,
    textAlignment: ReaderSettings.TextAlignment,
    onLineHeightChange: (LineHeight) -> Unit,
    onTextAlignmentChange: (ReaderSettings.TextAlignment) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = Dimens.Gutter),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing12)) {
            LineHeight.entries.forEach { option ->
                val selected = option == lineHeight
                val color by animateColorAsState(if (selected) colorScheme.primary else colorScheme.surfaceContainerHighest)

                SurfaceIcon(
                    selected = selected,
                    onClick = { onLineHeightChange(option) },
                ) {
                    LineHeightIcon(
                        variant = option,
                        modifier = Modifier.size(Dimens.Spacing24),
                        color = color
                    )
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing12)) {
            ReaderSettings.TextAlignment.entries.forEach { option ->
                val selected = option == textAlignment
                val color by animateColorAsState(if (selected) colorScheme.primary else colorScheme.surfaceContainerHighest)

                SurfaceIcon(
                    onClick = { onTextAlignmentChange(option) },
                    selected = selected
                ) {
                    val icon = when (option) {
                        ReaderSettings.TextAlignment.LEFT -> Icons.AlignLeft
                        ReaderSettings.TextAlignment.RIGHT -> Icons.AlignRight
                        ReaderSettings.TextAlignment.JUSTIFY -> Icons.AlignJustified
                    }

                    Icon(
                        imageVector = icon,
                        contentDescription = option.label,
                        tint = color
                    )
                }
            }
        }
    }
}

@Composable
private fun FontMarginSize(
    fontScale: Float,
    onFontScaleChange: (Float) -> Unit,
    marginScale: Float,
    onMarginChange: (Float) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens.Gutter),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Font Size Controls
        Surface(
            shape = RoundedCornerShape(Dimens.Radius08),
            color = colorScheme.surfaceVariant
        ) {
            Row(
                modifier = Modifier
                    .animateContentSize()
                    .padding(horizontal = Dimens.Gutter, vertical = Dimens.Spacing08),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing12)
            ) {
                Text(
                    text = "aA",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onSurface,
                    modifier = Modifier.simpleClickable {
                        val currentIndex = ReaderSettings.fontScaleOptions.indexOf(fontScale)
                        if (currentIndex > 0) {
                            onFontScaleChange(ReaderSettings.fontScaleOptions[currentIndex - 1])
                        }
                    }
                )

                Text(
                    text = "${(fontScale * 100).toInt()}%",
                    style = MaterialTheme.typography.titleMedium,
                    color = colorScheme.onSurfaceVariant,
                )

                Text(
                    text = "aA",
                    style = MaterialTheme.typography.titleMedium,
                    color = colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.simpleClickable {
                        val currentIndex = ReaderSettings.fontScaleOptions.indexOf(fontScale)
                        if (currentIndex < ReaderSettings.fontScaleOptions.lastIndex) {
                            onFontScaleChange(ReaderSettings.fontScaleOptions[currentIndex + 1])
                        }
                    }
                )
            }
        }

        // Margin Controls
        Surface(
            shape = RoundedCornerShape(Dimens.Radius08),
            color = colorScheme.surfaceVariant,
        ) {
            Row(
                modifier = Modifier
                    .animateContentSize()
                    .padding(horizontal = Dimens.Gutter, vertical = Dimens.Spacing08),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing12)
            ) {
                Text(
                    text = "[ ]",
                    style = MaterialTheme.typography.labelSmall,
                    color = colorScheme.onSurface,
                    modifier = Modifier.simpleClickable {
                        val currentIndex = ReaderSettings.marginScaleOptions.indexOf(marginScale)
                        if (currentIndex > 0) {
                            onMarginChange(ReaderSettings.marginScaleOptions[currentIndex - 1])
                        }
                    }
                )

                Text(
                    text = "${(marginScale * 100).toInt()}%",
                    style = MaterialTheme.typography.titleMedium,
                    color = colorScheme.onSurfaceVariant
                )

                Text(
                    text = "[ ]",
                    style = MaterialTheme.typography.titleMedium,
                    color = colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.simpleClickable {
                        val currentIndex = ReaderSettings.marginScaleOptions.indexOf(marginScale)
                        if (currentIndex < ReaderSettings.marginScaleOptions.lastIndex) {
                            onMarginChange(ReaderSettings.marginScaleOptions[currentIndex + 1])
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun FontStyle(selectedFont: ReaderFont, language: String, onClick: (ReaderFont) -> Unit) {
    var showFontSelection by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(Dimens.Radius08),
        color = colorScheme.surfaceVariant,
        onClick = { showFontSelection = true }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = Dimens.Spacing12, vertical = Dimens.Spacing08),
            horizontalArrangement = Arrangement.spacedBy(Dimens.Gutter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedFont.name,
                style = MaterialTheme.typography.labelMedium,
                fontFamily = selectedFont.fontFamily,
            )

            Icon(imageVector = Icons.ChevronDown, contentDescription = null, tint = colorScheme.surfaceTint)
        }
    }

    if (showFontSelection) {
        Dialog(onDismissRequest = { showFontSelection = false }) {
            Surface(color = colorScheme.surfaceVariant, shape = RoundedCornerShape(Dimens.Radius20)) {
                Column(
                    modifier = Modifier.padding(Dimens.Spacing24),
                    verticalArrangement = Arrangement.spacedBy(Dimens.Spacing24)
                ) {
                    ReaderFont.options(language).forEach { font ->
                        val alpha by animateFloatAsState(if (font == selectedFont) 1f else 0.7f)

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Dimens.Gutter, vertical = Dimens.Spacing08)
                                .clip(RoundedCornerShape(Dimens.Radius04))
                                .background(colorScheme.surfaceContainer.copy(alpha = 0.2f))
                                .simpleClickable {
                                    onClick(font)
                                    showFontSelection = false
                                },
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = font.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontFamily = font.fontFamily,
                                color = sheetContentColor.copy(alpha = alpha)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ThemeSelector(currentTheme: ReaderTheme, onThemeChange: (ReaderTheme) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = Dimens.Gutter),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ReaderTheme.options.forEach { theme ->
            val selected = theme.key == currentTheme.key
            val borderColor by animateColorAsState(if (selected) colorScheme.primary else colorScheme.surfaceVariant)
            val borderWidth by animateDpAsState(if (selected) 2.dp else 1.dp)

            Surface(
                modifier = Modifier
                    .widthIn(max = Dimens.Spacing48)
                    .aspectRatio(1f),
                color = if (!theme.isSystemTheme) theme.backgroundColor else Color.Transparent,
                border = BorderStroke(borderWidth, borderColor),
                shape = CircleShape,
                onClick = { onThemeChange(theme) }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .then(
                            if (theme.isSystemTheme) {
                                Modifier.drawBehind {
                                    // Draw diagonal split background
                                    drawPath(
                                        path = Path().apply {
                                            moveTo(0f, 0f)
                                            lineTo(size.width, 0f)
                                            lineTo(size.width, size.height)
                                            lineTo(0f, size.height)
                                            close()
                                        },
                                        color = Color.White
                                    )
                                    drawPath(
                                        path = Path().apply {
                                            moveTo(0f, size.height)
                                            lineTo(size.width, 0f)
                                            lineTo(size.width, size.height)
                                            close()
                                        },
                                        color = Color.Black
                                    )
                                }
                            } else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (theme.isSystemTheme) {
                        Row(
                            modifier = Modifier.offset(y = (-2).dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "A",
                                color = Color.Black,
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                text = "a",
                                color = Color.White,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    } else {
                        Text(
                            text = "Aa",
                            color = theme.contentColor,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NavigationControl(
    horizontalNavigation: Boolean,
    onNavigationChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = Dimens.Gutter)
            .simpleClickable { onNavigationChange(!horizontalNavigation) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = stringResource(Res.string.horizontal_navigation),
                style = MaterialTheme.typography.titleSmall,
                color = colorScheme.onSurface
            )

            Text(
                text = stringResource(Res.string.horizontal_navigation_text),
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        Switch(
            checked = horizontalNavigation,
            onCheckedChange = onNavigationChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = colorScheme.primary,
                checkedTrackColor = colorScheme.primaryContainer,
                uncheckedThumbColor = colorScheme.outline,
                uncheckedTrackColor = colorScheme.surfaceVariant
            )
        )
    }
}

private val sheetContentColor
    @Composable get() = colorScheme.onSurface

@Composable
private fun SurfaceIcon(
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val borderColor by animateColorAsState(if (selected) colorScheme.primary else colorScheme.outline)

    Surface(
        modifier = modifier,
        border = BorderStroke(1.5.dp, borderColor),
        shape = CircleShape,
        onClick = onClick,
    ) {
        Box(modifier = Modifier.padding(Dimens.Spacing12)) {
            content()
        }
    }
}

class SettingsState {
    var show by mutableStateOf(false)

    fun show() {
        show = true
    }

    fun hide() {
        show = false
    }

    fun toggle() {
        if (show) hide() else show()
    }
}

@Composable
fun rememberSettingsState() = remember { SettingsState() }
