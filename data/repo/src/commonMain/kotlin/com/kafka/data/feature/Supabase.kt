package com.kafka.data.feature

import com.kafka.data.entities.File
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.result.PostgrestResult
import io.github.jan.supabase.storage.storage
import javax.inject.Inject

class SupabaseDb @Inject constructor(private val supabase: SupabaseClient) {
    val books = supabase.from("books")
    val bookDetail = supabase.from("book_detail")
    val files = supabase.from("files")
    val covers = supabase.storage.from("items")
    val recentItems = supabase.from("recent_items")
}

fun PostgrestResult.decodeFiles(): List<File> = decodeList()