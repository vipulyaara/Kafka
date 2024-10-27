@file:OptIn(SupabaseExperimental::class)

package com.kafka.data.feature

import com.kafka.base.errorLog
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.PostgrestQueryBuilder
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.PostgrestFilterBuilder
import io.github.jan.supabase.realtime.selectAsFlow
import io.github.jan.supabase.realtime.selectSingleValueAsFlow
import kotlinx.coroutines.flow.catch
import me.tatarka.inject.annotations.Inject
import kotlin.reflect.KProperty1

@Inject
class Supabase(client: SupabaseClient) {
    val items = client.from("items")
    val bookDetail = client.from("item_detail")
    val files = client.from("files")
    val recentItems = client.from("reading_list")
    val feedback = client.from("feedback")
    val favoriteList = client.from("favorite_list")
    val summary = client.from("summary")

    val auth = client.auth
}

inline fun <reified Data : Any, Value> PostgrestQueryBuilder.safeSelectAsFlow(
    primaryKeys: List<KProperty1<Data, Value>>,
    channelName: String? = null,
    filter: FilterOperation? = null,
) = selectAsFlow(primaryKeys, channelName, filter)
    .catch {
        errorLog(it) { "Error while observing $channelName" }
    }


inline fun <reified Data : Any, Value> PostgrestQueryBuilder.safeSelectSingleValueAsFlow(
    primaryKey: KProperty1<Data, Value>,
    channelName: String? = null,
    crossinline filter: PostgrestFilterBuilder.() -> Unit
) = selectSingleValueAsFlow(primaryKey, channelName, filter)
    .catch {
        errorLog(it) { "Error while observing $channelName" }
    }
