package com.kafka.content.ui.homepage

import com.kafka.content.R
import com.kafka.content.data.Homepage
import com.kafka.content.domain.homepage.HomepageTag
import com.kafka.content.ui.query.SearchQuery
import com.kafka.content.ui.query.SearchQueryType
import com.kafka.data.db.*
import com.kafka.ui_common.base.BaseViewState

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class HomepageViewState(
    val homepage: Homepage? = null,
    val error: Throwable? = null
) : BaseViewState

val bannerImages = listOf(
    R.drawable.img_banner_30,
    R.drawable.img_banner_31,
    R.drawable.img_banner_32,
    R.drawable.img_banner_26,
    R.drawable.img_banner_33
)

val tags = listOf(
    HomepageTag("Urdu Poetry", urduPoetry.toQuery(), false),
    HomepageTag("English Prose", englishProse.toQuery(), true),
    HomepageTag("Devnagri", devnagri.toQuery(), false),
    HomepageTag("English Poetry", englishPoetry.toQuery(), false),
    HomepageTag("Urdu Prose", urduProse.toQuery(), false)
)

private fun String.toQuery() = SearchQuery(type = SearchQueryType.Suggested(this))
