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
        if (columnContents.size < 2) throw IllegalArgumentException("At least two columns are required")

        // Calculate the maximum length of the content in the first column
        val firstColumnMaxLength = columnContents[0].maxOfOrNull { it.length }?.toFloat() ?: 0f

        // Total weight to be distributed (1.0)
        val totalWeight = 1f

        // Determine the weight for the first column to fully display its content
        val firstColumnWeight = firstColumnMaxLength.coerceAtLeast(1f) / MAX_CHARACTERS_PER_LINE // Normalize weight

        // Ensure the first column does not exceed the total weight
        val clampedFirstColumnWeight = firstColumnWeight.coerceIn(0f, totalWeight)

        // Remaining weight goes to the second column
        val secondColumnWeight = totalWeight - clampedFirstColumnWeight

        // Return weights for the first and second columns
        return listOf(clampedFirstColumnWeight, secondColumnWeight)
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

const val MAX_CHARACTERS_PER_LINE = 40
