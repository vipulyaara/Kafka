package kafka.reader.core.parser

import com.fleeksoft.ksoup.nodes.Element
import kafka.reader.core.models.ColumnAlignment
import kafka.reader.core.models.ContentElement
import kafka.reader.core.models.TableStyle

fun parseTable(element: Element): ContentElement.Table {
    val caption = element.select("caption").firstOrNull()?.text()
    val summary = element.attr("data-summary")

    // Parse headers as Text elements with full styling
    val headerElements = element.select("thead tr th").map { cell ->
        ContentParser.parseNode(cell).firstOrNull() as? ContentElement.Text 
            ?: ContentElement.Text(cell.text())
    }

    // Parse column alignments
    val columnAlignments = element.select("thead tr th").map { th ->
        when {
            th.hasClass("text-right") -> ColumnAlignment.RIGHT
            th.hasClass("text-center") -> ColumnAlignment.CENTER
            else -> ColumnAlignment.LEFT
        }
    }

    // Parse rows with full text styling support
    val rowElements = element.select("tbody tr").map { row ->
        row.select("td").map { cell ->
            ContentParser.parseNode(cell).firstOrNull() as? ContentElement.Text 
                ?: ContentElement.Text(cell.text())
        }
    }

    // Determine if table has header row/column
    val isHeaderRow = element.select("thead").isNotEmpty()
    val isHeaderColumn = element.select("tbody tr th").isNotEmpty()

    // Determine table style
    val style = when {
        element.hasClass("striped") -> TableStyle.Striped
        element.hasClass("bordered") -> TableStyle.Bordered
        element.hasClass("compact") -> TableStyle.Compact
        element.hasClass("custom") -> TableStyle.Custom
        else -> TableStyle.Default
    }

    return ContentElement.Table(
        caption = caption,
        summary = summary,
        columnAlignments = columnAlignments,
        isHeaderRow = isHeaderRow,
        isHeaderColumn = isHeaderColumn,
        style = style,
        headerElements = headerElements,
        rowElements = rowElements
    )
} 