package com.kafka.reader.epub.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kafka.reader.epub.settings.ReaderSettings
import kafka.reader.core.models.ColumnAlignment
import kafka.reader.core.models.ContentElement
import kafka.reader.core.models.TableStyle

@Composable
fun TableComponent(element: ContentElement.Table, settings: ReaderSettings) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Caption
        element.caption?.let { caption ->
            Text(
                text = caption,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Table content
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
                // Headers
                if (element.isHeaderRow) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(8.dp)
                    ) {
                        element.headers.forEachIndexed { index, header ->
                            TableCell(
                                text = header,
                                weight = 1f,
                                alignment = element.columnAlignments.getOrNull(index)
                                    ?: ColumnAlignment.LEFT,
                                isHeader = true,
                                settings = settings
                            )
                        }
                    }
                }

                // Rows
                element.rows.forEachIndexed { rowIndex, row ->
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
                            TableCell(
                                text = cell,
                                weight = 1f,
                                alignment = element.columnAlignments.getOrNull(colIndex)
                                    ?: ColumnAlignment.LEFT,
                                isHeader = element.isHeaderColumn && colIndex == 0,
                                settings = settings
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.TableCell(
    text: String,
    weight: Float,
    alignment: ColumnAlignment,
    isHeader: Boolean,
    settings: ReaderSettings
) {
    Text(
        text = text,
        style = if (isHeader) MaterialTheme.typography.titleSmall else MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = when (alignment) {
            ColumnAlignment.LEFT -> TextAlign.Start
            ColumnAlignment.CENTER -> TextAlign.Center
            ColumnAlignment.RIGHT -> TextAlign.End
        },
        modifier = Modifier
            .weight(weight)
            .padding(horizontal = 4.dp)
    )
} 