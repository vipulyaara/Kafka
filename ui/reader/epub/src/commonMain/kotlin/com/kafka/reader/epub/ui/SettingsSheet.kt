@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.reader.epub.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kafka.common.simpleClickable
import com.kafka.reader.epub.settings.ReaderSettings
import com.kafka.reader.epub.settings.ReaderSettings.Background
import com.kafka.reader.epub.settings.ReaderSettings.FontSize
import com.kafka.reader.epub.settings.ReaderSettings.FontStyle
import com.kafka.ui.components.material.Slider
import ui.common.theme.theme.Dimens

@Composable
fun SettingsSheet(
    settings: ReaderSettings,
    changeSettings: (ReaderSettings) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.padding(Dimens.Spacing24),
            verticalArrangement = Arrangement.spacedBy(Dimens.Spacing36)
        ) {

            FontSize(
                fontSize = settings.fontSize,
                onClick = { changeSettings(settings.copy(fontSize = it)) }
            )

            FontStyle(
                fontStyle = settings.fontStyle,
                onClick = { changeSettings(settings.copy(fontStyle = it)) }
            )

            Background(
                background = settings.background,
                onClick = { changeSettings(settings.copy(background = it)) })

        }
    }
}

@Composable
private fun Background(background: Background, onClick: (Background) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Label(text = "Background")

        Spacer(modifier = Modifier.height(Dimens.Spacing12))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing12)
        ) {
            Background.Options(isSystemInDarkTheme()).forEach {
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
private fun FontStyle(fontStyle: FontStyle, onClick: (FontStyle) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Label(text = "Font Style")

        Spacer(modifier = Modifier.height(Dimens.Spacing08))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FontStyle.Options.forEach {
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

@Composable
private fun FontSize(fontSize: FontSize, onClick: (FontSize) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Label(text = "Font Size")

        Spacer(modifier = Modifier.height(Dimens.Spacing08))

        val fontSizes = FontSize.Options.map { it.fontSize }
        val initialSliderPosition = FontSize.Options.indexOf(fontSize).toFloat()
        var sliderPosition by remember { mutableStateOf(initialSliderPosition) }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Aa",
                    style = MaterialTheme.typography.bodySmall,
                    color = sheetContentColor
                )

                Spacer(modifier = Modifier.width(Dimens.Spacing12))

                Slider(
                    value = sliderPosition,
                    onValueChange = {
                        sliderPosition = it
                        onClick(FontSize.Options[it.toInt()])
                    },
                    valueRange = 0f..(fontSizes.size - 1).toFloat(),
                    steps = fontSizes.size - 2,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(Dimens.Spacing12))

                Text(
                    text = "Aa",
                    style = MaterialTheme.typography.headlineSmall,
                    color = sheetContentColor
                )
            }
        }
    }
}

private val sheetContentColor
    @Composable get() = colorScheme.onSurface

@Composable
private fun Label(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = sheetContentColor.copy(alpha = 0.5f)
    )
}
