package com.kafka.data.feature

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject

class Supabase @Inject constructor(supabase: SupabaseClient) {
    val items = supabase.from("items")
    val bookDetail = supabase.from("item_detail")
    val files = supabase.from("files")
    val recentItems = supabase.from("reading_list")
    val feedback = supabase.from("feedback")
    val favoriteList = supabase.from("favorite_list")

    val auth = supabase.auth
}
