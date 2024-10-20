package com.kafka.domain.interactors.recent

import com.kafka.base.Service
import com.kafka.base.appRecentItems
import com.kafka.base.domain.Interactor
import com.kafka.data.feature.SupabaseDb
import com.kafka.data.feature.auth.AccountRepository
import com.kafka.data.feature.firestore.FirestoreGraph
import javax.inject.Inject

class RemoveRecentItem @Inject constructor(
    private val firestoreGraph: FirestoreGraph,
    private val accountRepository: AccountRepository,
    private val supabaseDb: SupabaseDb
) : Interactor<String, Unit>() {
    override suspend fun doWork(params: String) {
        if (appRecentItems == Service.Archive) {
            val document = firestoreGraph.recentItemsCollection.document(params)
            document.delete()
        } else {
            accountRepository.currentFirebaseUser?.let { user ->
                supabaseDb.recentItems.delete {
                    filter {
                        eq("file_id", params)
                        eq("uid", user.uid)
                    }
                }
            }
        }
    }
}
