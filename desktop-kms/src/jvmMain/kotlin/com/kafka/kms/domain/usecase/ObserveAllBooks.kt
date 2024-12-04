package com.kafka.kms.domain.usecase

import com.kafka.data.entities.ItemDetail
import com.kafka.data.feature.Supabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveAllBooks(private val supabase: Supabase) {
    fun getBooks(): Flow<List<ItemDetail>> = flow {
        try {
            val books = supabase.itemDetail
                .select()
                .decodeList<ItemDetail>()
            emit(books)
        } catch (e: Exception) {
            emit(emptyList())
            e.printStackTrace()
        }
    }
} 