package com.kafka.ui.main

import android.view.ViewGroup
import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.lifecycle.LifecycleOwner
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.graphics.Color
import androidx.ui.layout.Stack
import androidx.ui.layout.fillMaxSize
import androidx.ui.layout.preferredHeight
import androidx.ui.material.BottomNavigation
import androidx.ui.material.BottomNavigationItem
import androidx.ui.unit.dp
import com.kafka.ui.R
import com.kafka.ui.VectorImage
import com.kafka.ui.actions.SearchAction
import com.kafka.ui.home.HomepageAction
import com.kafka.ui.home.HomepageScreen
import com.kafka.ui.home.HomepageViewState
import com.kafka.ui.search.SearchScreen
import com.kafka.ui.search.SearchViewState
import com.kafka.ui.setContentWithLifecycle
import dev.chrisbanes.accompanist.mdctheme.MaterialThemeFromMdcTheme

sealed class BottomNavigationItem(val icon: Int) {
    object Home : BottomNavigationItem(R.drawable.ic_download)
    object Search : BottomNavigationItem(R.drawable.ic_twotone_search_24)
    object Library : BottomNavigationItem(R.drawable.ic_headphones)
    object Profile : BottomNavigationItem(R.drawable.ic_user)
}

val navigationItems = arrayOf(
    BottomNavigationItem.Home,
    BottomNavigationItem.Search,
    BottomNavigationItem.Library,
    BottomNavigationItem.Profile
)

fun ViewGroup.composeMainScreen(
    lifecycleOwner: LifecycleOwner,
    searchViewState: SearchViewState,
    homepageViewState: HomepageViewState,
    searchActioner: (SearchAction) -> Unit,
    homepageActioner: (HomepageAction) -> Unit
): Any = setContentWithLifecycle(lifecycleOwner) {
    val selectedNavigation: MutableState<BottomNavigationItem> = androidx.compose.state { BottomNavigationItem.Home }
    MaterialThemeFromMdcTheme {
        Stack(modifier = Modifier.fillMaxSize()) {
            when (selectedNavigation.value) {
                BottomNavigationItem.Home -> HomepageScreen(viewState = homepageViewState, actioner = homepageActioner)
                BottomNavigationItem.Search -> SearchScreen(viewState = searchViewState, actioner = searchActioner)
                BottomNavigationItem.Library -> { }
                BottomNavigationItem.Profile -> {}
            }

            Tabs(modifier = Modifier.gravity(Alignment.BottomCenter), selectedNavigation = selectedNavigation)
        }
    }
}

@Composable
fun Tabs(modifier: Modifier, selectedNavigation: MutableState<BottomNavigationItem>) {
    BottomNavigation(modifier = modifier.preferredHeight(48.dp), backgroundColor = Color(0xFFDFE9FF)) {
        navigationItems.forEach {
            BottomNavigationItem(
                icon = { VectorImage(id = it.icon) },
                selected = selectedNavigation.value == it,
                onSelected = { selectedNavigation.value = it }
            )
        }
    }
}
