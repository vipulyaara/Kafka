package com.kafka.domain.interactors

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.ResultInteractor
import com.kafka.data.dao.FileDao
import com.kafka.data.dao.ItemDetailDao
import com.kafka.data.dao.RecentTextDao
import com.kafka.data.encodeUrl
import com.kafka.data.entities.RecentTextItem
import com.kafka.data.entities.nameWithoutExtension
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetReaderState @Inject constructor(
    private val itemDetailDao: ItemDetailDao,
    private val fileDao: FileDao,
    private val recentTextDao: RecentTextDao,
    private val dispatchers: CoroutineDispatchers,
) : ResultInteractor<GetReaderState.Params, ReaderState>() {

    override suspend fun doWork(params: Params): ReaderState {
        return withContext(dispatchers.io) {
            val itemDetail = itemDetailDao.get(params.itemId)
            val fileId = params.fileId
            val file = fileDao.getOrNull(fileId)
            val recentText = fileId.let { recentTextDao.getOrNull(it) }
            val currentPage = recentText?.currentPage ?: 1

            if (recentText == null) {
                recentTextDao.insert(createEmptyText(fileId, currentPage))
            }

            val baseUrl = "https://archive.org/details"
            val fileName = file?.nameWithoutExtension().orEmpty()
            val idSegment =
                if (file != null) "/${params.itemId}/${fileName.encodeUrl()}" else "/${params.itemId}"
            val pageSegment =
                if (currentPage == 1) "" else "/page/n${currentPage - 1}" // archive reader index starts at 0

            val url = "$baseUrl$idSegment$pageSegment/mode/1up?view=theater"

            ReaderState(
                url = url,
                itemId = params.itemId,
                fileId = fileId,
                itemTitle = itemDetail.title,
                currentPage = currentPage
            )
        }
    }

    private fun createEmptyText(fileId: String, currentPage: Int) = RecentTextItem(
        fileId = fileId,
        currentPage = currentPage,
        localUri = "",
        type = RecentTextItem.Type.PDF
    )

    data class Params(val itemId: String, val fileId: String)
}

data class ReaderState(
    val url: String,
    val itemId: String,
    val fileId: String,
    val itemTitle: String?,
    val currentPage: Int,
)

fun String.getCurrentPageFromReaderUrl(): Int {
    val segments = split("/")
    val index = segments.indexOf("page")
    return segments[index + 1].removePrefix("n").toIntOrNull() ?: 1
}
