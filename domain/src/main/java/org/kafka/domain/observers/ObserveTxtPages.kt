package org.kafka.domain.observers

import androidx.core.net.toUri
import com.kafka.data.dao.FileDao
import com.kafka.data.dao.RecentTextDao
import com.kafka.data.entities.RecentTextItem
import com.kafka.data.entities.isTxt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.kafka.base.domain.SubjectInteractor
import org.kafka.domain.interactors.ReadTextFromUri
import javax.inject.Inject

/**
 * Observe recent text file and extract pages using the local uri.
 * */
class ObserveTxtPages @Inject constructor(
    private val fileDao: FileDao,
    private val recentTextDao: RecentTextDao,
    private val readTextFromUri: ReadTextFromUri
) : SubjectInteractor<ObserveTxtPages.Params, List<RecentTextItem.Page>>() {

    override fun createObservable(params: Params): Flow<List<RecentTextItem.Page>> {
        return combine(
            recentTextDao.observe(params.fileId),
            fileDao.observe(params.fileId)
        ) { recentText, file ->
            if (file != null) {
                val pages = if (file.isTxt()) {
                    readTextFromUri(recentText.localUri.toUri()).getOrElse { emptyList() }
                } else {
                    emptyList()
                }

                pages.mapIndexed { index, s -> RecentTextItem.Page(index + 1, s) }
            } else {
                emptyList()
            }
        }
    }

    data class Params(val fileId: String)
}
