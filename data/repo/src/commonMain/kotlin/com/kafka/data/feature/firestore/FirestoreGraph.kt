package com.kafka.data.feature.firestore

import com.kafka.base.ApplicationScope
import com.kafka.data.entities.BookshelfDefaults
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.DocumentReference
import me.tatarka.inject.annotations.Inject
import dev.gitlive.firebase.firestore.FirebaseFirestore as FirebaseFirestoreKt

@ApplicationScope
@Inject
class FirestoreGraph(
    private val firestoreKt: FirebaseFirestoreKt
) {
    val homepageCollection: CollectionReference
        get() = firestoreKt
            .collection("homepage-collection-debug") //todo

    val appUpdateConfig: DocumentReference
        get() = firestoreKt
            .collection("app_config")
            .document("app_update")

    val appMessageConfig: DocumentReference
        get() = firestoreKt
            .collection("app_config")
            .document("app_message")

    val summaryCollection: CollectionReference
        get() = firestoreKt.collection("summary")

    val reportsCollection: CollectionReference
        get() = firestoreKt.collection("reports")

    fun listCollection(uid: String) = firestoreKt
        .collection("users")
        .document(uid)
        .collection("lists")

    fun listItemsCollection(uid: String, listId: String) = listCollection(uid)
        .document(listId)
        .collection("items")

    fun readingListCollection(uid: String) =
        listItemsCollection(uid, BookshelfDefaults.reading.bookshelfId)

    fun favoriteListCollection(uid: String) =
        listItemsCollection(uid, BookshelfDefaults.favorite.bookshelfId)
}
