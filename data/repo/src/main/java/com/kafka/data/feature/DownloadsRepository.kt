package com.kafka.data.feature

import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.kafka.data.entities.DownloadItem
import com.kafka.data.feature.auth.AccountRepository
import com.kafka.data.feature.firestore.FirestoreGraph
import dagger.Reusable
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@Reusable
class DownloadsRepository @Inject constructor(
    private val firestoreGraph: FirestoreGraph,
    private val accountRepository: AccountRepository
) {
    fun observeFavorites(uid: String) = firestoreGraph.getDownloadsCollection(uid)
        .snapshots()
        .map { snapshots -> snapshots.map { it.toObject<DownloadItem>() } }

    suspend fun getDownloads() =
        accountRepository.currentFirebaseUser?.uid?.let { uid ->
            firestoreGraph.getDownloadsCollection(uid).get()
                .await()
                .map { snapshots -> snapshots.toObject<DownloadItem>() }
        }

    suspend fun addDownload(downloadItem: DownloadItem) {
        accountRepository.currentFirebaseUser?.uid?.let {
            firestoreGraph.getDownloadsCollection(it).document(downloadItem.id)
                .set(downloadItem)
        }?.await()
    }

    suspend fun addDownloads(downloadItems: List<DownloadItem>) {
        accountRepository.currentFirebaseUser?.uid?.let {
            val collection = firestoreGraph.getDownloadsCollection(it)
            downloadItems.forEach {
                collection.document(it.id).set(it).await()
            }
        }
    }

    suspend fun removeDownload(downloadItem: DownloadItem) {
        accountRepository.currentFirebaseUser?.uid?.let {
            firestoreGraph.getDownloadsCollection(it).document(downloadItem.id).delete()
        }?.await()
    }
}
