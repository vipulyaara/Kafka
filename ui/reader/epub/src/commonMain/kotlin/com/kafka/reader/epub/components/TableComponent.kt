package com.kafka.reader.epub.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kafka.reader.epub.settings.ReaderSettings
import kafka.reader.core.models.ColumnAlignment
import kafka.reader.core.models.ContentElement
import kafka.reader.core.models.TableStyle
import kafka.reader.core.models.TextStyle
import kafka.reader.core.models.toTextAlignment

@Composable
fun TableComponent(element: ContentElement.Table, settings: ReaderSettings) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {

        element.caption?.let { caption ->
            Text(
                text = caption,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }


        Box(
            modifier = Modifier
                .then(
                    when (element.style) {
                        TableStyle.Bordered -> Modifier.border(
                            1.dp,
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                        )

                        else -> Modifier
                    }
                )
        ) {
            Column {
                // Headers using headerElements
                if (element.isHeaderRow) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(8.dp)
                    ) {
                        element.headerElements.forEachIndexed { index, headerElement ->
                            val textElement = headerElement.copy(
                                alignment = (element.columnAlignments.getOrNull(index)
                                    ?: ColumnAlignment.LEFT).toTextAlignment(),
                                styles = setOf(TextStyle.Heading5)
                            )

                            TextElement(
                                element = textElement,
                                settings = settings,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Rows using rowElements
                element.rowElements.forEachIndexed { rowIndex, row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                when {
                                    element.style == TableStyle.Striped && rowIndex % 2 == 1 ->
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)

                                    else -> MaterialTheme.colorScheme.surface
                                }
                            )
                            .padding(8.dp)
                    ) {
                        row.forEachIndexed { colIndex, cell ->
                            val textElement = cell.copy(
                                alignment = (element.columnAlignments.getOrNull(colIndex)
                                    ?: ColumnAlignment.LEFT).toTextAlignment(),
                                styles = setOf(
                                    if (element.isHeaderColumn && colIndex == 0) {
                                        TextStyle.Heading5
                                    } else {
                                        TextStyle.Normal
                                    }
                                )
                            )

                            TextElement(
                                element = textElement,
                                settings = settings,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}
