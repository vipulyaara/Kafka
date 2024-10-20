package com.kafka.domain.observers

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.dao.ItemDetailDao
import com.kafka.remote.config.RemoteConfig
import com.kafka.remote.config.isOnlineReaderEnabled
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import tm.alashow.datmusic.downloader.interactors.ObserveDownloadByItemId
import javax.inject.Inject

class ShouldUseOnlineReader @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val observeDownloadByItemId: ObserveDownloadByItemId,
    private val itemDetailDao: ItemDetailDao,
    private val remoteConfig: RemoteConfig,
) : SubjectInteractor<ShouldUseOnlineReader.Param, Boolean>() {

    override fun createObservable(params: Param): Flow<Boolean> {
        return combine(
            itemDetailDao.observeItemDetail(params.itemId),
            observeDownloadByItemId.createObservable(ObserveDownloadByItemId.Params(params.itemId))
        ) { itemDetail, download ->
            if (itemDetail == null) {
                return@combine false
            }

            val isOnlineReaderEnabled = download == null && remoteConfig.isOnlineReaderEnabled()

            isOnlineReaderEnabled || itemDetail.isAccessRestricted
        }.flowOn(dispatchers.io)
    }

    data class Param(val itemId: String)
}
