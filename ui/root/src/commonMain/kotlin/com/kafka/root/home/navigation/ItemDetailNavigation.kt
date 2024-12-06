package com.kafka.root.home.navigation

import androidx.compose.material.navigation.bottomSheet
import androidx.core.bundle.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.kafka.common.animation.ProvideLocalAnimatedContentScope
import com.kafka.item.detail.ItemDetail
import com.kafka.item.detail.ItemDetailViewModel
import com.kafka.item.detail.description.DescriptionDialog
import com.kafka.item.report.ReportContentScreen
import com.kafka.item.report.ReportContentViewModel
import com.kafka.item.reviews.ReviewScreen
import com.kafka.item.reviews.ReviewViewModel
import com.kafka.library.bookshelf.add.AddToBookshelf
import com.kafka.library.bookshelf.add.AddToBookshelfViewModel
import com.kafka.navigation.deeplink.Config
import com.kafka.navigation.graph.Screen
import com.kafka.navigation.graph.Screen.ItemDetail.Origin
import com.kafka.summary.SummaryScreen
import com.kafka.summary.SummaryViewModel
import me.tatarka.inject.annotations.Inject
import kotlin.reflect.typeOf

typealias addItemDetailGroup = NavGraphBuilder.() -> Unit

@Inject
fun NavGraphBuilder.addItemDetailGroup(
    addItemDetail: addItemDetail,
    addItemDescription: addItemDescription,
    addToBookshelf: addToBookshelf,
    addReportContent: addReportContent,
    addEpubReader: addEpubReader,
    addSummary: addSummary,
    addReviews: addReviews
) {
    addItemDetail()
    addItemDescription()
    addToBookshelf()
    addReportContent()
    addEpubReader()
    addSummary()
    addReviews()
}

typealias addItemDetail = NavGraphBuilder.() -> Unit

@Inject
fun NavGraphBuilder.addItemDetail(
    viewModelFactory: (SavedStateHandle) -> ItemDetailViewModel,
) {
    val originNavType = object : NavType<Origin>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): Origin? {
            return bundle.getString(key)?.let { Origin.valueOf(it) }
        }

        override fun parseValue(value: String): Origin {
            return Origin.valueOf(value)
        }

        override fun put(bundle: Bundle, key: String, value: Origin) {
            bundle.putString(key, value.name)
        }
    }

    composable<Screen.ItemDetail>(
        typeMap = mapOf(typeOf<Origin>() to originNavType),
        deepLinks = listOf(
            navDeepLink<Screen.ItemDetail>(
                basePath = "${Config.BASE_URL}item",
                typeMap = mapOf(typeOf<Origin>() to originNavType)
            ),
            navDeepLink<Screen.ItemDetail>(
                basePath = "${Config.BASE_URL_ALT}item",
                typeMap = mapOf(typeOf<Origin>() to originNavType)
            )
        )
    ) {
        ProvideLocalAnimatedContentScope(this@composable) {
            val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
            ItemDetail(viewModel)
        }
    }
}

typealias addItemDescription = NavGraphBuilder.() -> Unit

@Inject
fun NavGraphBuilder.addItemDescription(
    viewModelFactory: (SavedStateHandle) -> ItemDetailViewModel,
) {
    bottomSheet(Screen.ItemDescription.route) {
        val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
        DescriptionDialog(viewModel)
    }
}

typealias addReportContent = NavGraphBuilder.() -> Unit

@Inject
fun NavGraphBuilder.addReportContent(viewModelFactory: () -> ReportContentViewModel) {
    bottomSheet(route = Screen.ReportContent.route) {
        val viewModel = viewModel { viewModelFactory() }
        ReportContentScreen(viewModel)
    }
}

typealias addSummary = NavGraphBuilder.() -> Unit

@Inject
fun NavGraphBuilder.addSummary(viewModelFactory: (SavedStateHandle) -> SummaryViewModel) {
    composable<Screen.Summary> {
        val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
        SummaryScreen(viewModel)
    }
}

typealias addReviews = NavGraphBuilder.() -> Unit

@Inject
fun NavGraphBuilder.addReviews(viewModelFactory: (SavedStateHandle) -> ReviewViewModel) {
    composable<Screen.Reviews> {
        val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
        ReviewScreen(viewModel)
    }
}

typealias addToBookshelf = NavGraphBuilder.() -> Unit

@Inject
fun NavGraphBuilder.addToBookshelf(
    viewModelFactory: (SavedStateHandle) -> AddToBookshelfViewModel,
) {
    bottomSheet(Screen.AddToBookshelf.route) {
        val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
        AddToBookshelf(viewModel)
    }
}