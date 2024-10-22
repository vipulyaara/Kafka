package com.kafka.data.feature

import com.kafka.data.entities.File
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.result.PostgrestResult
import javax.inject.Inject

class Supabase @Inject constructor(supabase: SupabaseClient) {
    val books = supabase.from("items")
    val bookDetail = supabase.from("item_detail")
    val files = supabase.from("files")
    val recentItems = supabase.from("reading_list")

    val auth = supabase.auth
}

fun PostgrestResult.decodeFiles(): List<File> = decodeList()
