import com.fleeksoft.ksoup.nodes.Element

object TableAnalyzer {
    data class TableMetadata(
        val columnWeights: List<Float>,
        val isDialogueTable: Boolean,
        val hasHeader: Boolean,
        val columnTypes: List<ColumnType>
    )

    enum class ColumnType {
        TEXT, NUMERIC, NAME, DATE, EMPTY
    }

    fun analyzeTable(element: Element): TableMetadata {
        val rows = element.select("tr")
        val headerRow = element.selectFirst("thead tr") ?: rows.firstOrNull()
        val dataRows = if (element.selectFirst("thead") != null) {
            rows.filter { it.parent()?.tagName() != "thead" }
        } else {
            rows.drop(1)
        }

        val columnCount = headerRow?.children()?.size ?: 0
        val columnContents = List(columnCount) { mutableListOf<String>() }

        // Collect all content for each column
        dataRows.forEach { row ->
            row.children().forEachIndexed { index, cell ->
                if (index < columnCount) {
                    columnContents[index].add(cell.text())
                }
            }
        }

        return TableMetadata(
            columnWeights = calculateColumnWeights(columnContents),
            isDialogueTable = isDialogueTable(columnContents),
            hasHeader = element.selectFirst("thead") != null,
            columnTypes = determineColumnTypes(columnContents)
        )
    }

    private fun calculateColumnWeights(columnContents: List<List<String>>): List<Float> {
        if (columnContents.isEmpty()) return emptyList()

        // Calculate various metrics for each column
        val columnMetrics = columnContents.map { column ->
            val lengths = column.map { it.length }
            ColumnMetrics(
                maxLength = lengths.maxOrNull()?.toFloat() ?: 0f,
                avgLength = lengths.average().toFloat(),
                contentDensity = column.count { it.isNotBlank() }.toFloat() / column.size,
                hasLongContent = column.any { it.length > 50 }
            )
        }

        // Calculate base weights using a weighted combination of metrics
        val baseWeights = columnMetrics.map { metrics ->
            val weight = (
                (metrics.maxLength * 0.4f) +     // Max length has high importance
                (metrics.avgLength * 0.4f) +     // Average length equally important
                (if (metrics.hasLongContent) 2f else 0f) + // Bonus for long content
                (metrics.contentDensity * 0.2f)  // Density has some influence
            )
            weight.coerceAtLeast(1f)  // Ensure minimum weight
        }

        // Normalize weights to sum to 1
        val total = baseWeights.sum()
        return if (total > 0) {
            baseWeights.map { it / total }
        } else {
            List(columnContents.size) { 1f / columnContents.size }
        }
    }

    private fun isDialogueTable(columnContents: List<List<String>>): Boolean {
        if (columnContents.size != 2) return false
        
        val firstColumnAvgLength = columnContents[0].map { it.length }.average()
        val secondColumnAvgLength = columnContents[1].map { it.length }.average()
        
        return firstColumnAvgLength < 20 && secondColumnAvgLength > firstColumnAvgLength * 3
    }

    private fun determineColumnTypes(columnContents: List<List<String>>): List<ColumnType> {
        return columnContents.map { column ->
            when {
                column.isEmpty() -> ColumnType.EMPTY
                column.all { it.matches(Regex("^[\\d.,]+$")) } -> ColumnType.NUMERIC
                column.all { it.matches(Regex("^[A-Za-z.\\s]{2,20}$")) } -> ColumnType.NAME
                column.all { it.matches(Regex("^\\d{1,2}[-/]\\d{1,2}[-/]\\d{2,4}$")) } -> ColumnType.DATE
                else -> ColumnType.TEXT
            }
        }
    }
}

private data class ColumnMetrics(
    val maxLength: Float,
    val avgLength: Float,
    val contentDensity: Float,
    val hasLongContent: Boolean
) 