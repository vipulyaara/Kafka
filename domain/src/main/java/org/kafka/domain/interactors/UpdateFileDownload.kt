package org.kafka.domain.interactors

import android.net.Uri
import com.kafka.data.dao.ItemDetailDao
import kotlinx.coroutines.withContext
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class UpdateFileDownload @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val itemDetailDao: ItemDetailDao,
) : Interactor<UpdateFileDownload.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val itemDetail = itemDetailDao.itemDetail(params.itemId).run {
                val files = this.files?.toMutableList()?.apply {
                    replaceAll {
                        if (it.readerUrl == params.readerUrl) {
                            it.copy(localUri = params.localUri.toString())
                        } else {
                            it
                        }
                    }
                }
                copy(files = files)
            }
            itemDetailDao.insert(itemDetail)
        }
    }

    data class Params(val itemId: String, val readerUrl: String, val localUri: Uri)
}
