//package com.kafka.ui.main
//
//import android.view.ViewGroup
//import androidx.compose.Composable
//import androidx.compose.MutableState
//import androidx.compose.Recomposer
//import androidx.lifecycle.LiveData
//import androidx.ui.core.Alignment
//import androidx.ui.core.Modifier
//import androidx.ui.core.setContent
//import androidx.ui.foundation.drawBackground
//import androidx.ui.graphics.Color
//import androidx.ui.layout.Stack
//import androidx.ui.layout.fillMaxSize
//import androidx.ui.layout.preferredHeight
//import androidx.ui.material.BottomNavigation
//import androidx.ui.material.BottomNavigationItem
//import androidx.ui.unit.dp
//import com.kafka.ui.R
//import com.kafka.ui.VectorImage
//import com.kafka.content.ui.search.HomepageAction
//import com.kafka.ui.colors
//import com.kafka.ui.home.SearchScreen
//import com.kafka.ui.observe
//import com.kafka.ui.profile.ProfileScreen
//import com.kafka.content.ui.search.SearchViewState
//import dev.chrisbanes.accompanist.mdctheme.MaterialThemeFromMdcTheme
//
//fun ViewGroup.composeMainScreen(
//    mainViewState: LiveData<com.kafka.content.ui.main.MainViewState>,
//    searchViewState: LiveData<com.kafka.content.ui.search.SearchViewState>,
//    searchActioner: (com.kafka.content.ui.search.HomepageAction) -> Unit
//): Any = setContent(Recomposer.current()) {
//    val selectedNavigation: MutableState<BottomNavigationItem> = androidx.compose.state { BottomNavigationItem.Search }
//    MaterialThemeFromMdcTheme {
//        observe(searchViewState)?.let {
//            Stack(modifier = Modifier.fillMaxSize()) {
//                when (selectedNavigation.value) {
//                    BottomNavigationItem.Home -> {
//                    }
//                    BottomNavigationItem.Search -> {
//                        SearchScreen(viewState = it, actioner = searchActioner)
//                    }
//                    BottomNavigationItem.Library -> {
//                    }
//                    BottomNavigationItem.Profile -> {
//                        ProfileScreen()
//                    }
//                }
//
//                Tabs(modifier = Modifier.gravity(Alignment.BottomCenter), selectedNavigation = selectedNavigation)
//            }
//        }
//    }
//}
//
//@Composable
//fun Tabs(modifier: Modifier, selectedNavigation: MutableState<BottomNavigationItem>) {
//    BottomNavigation(
//        modifier = modifier.preferredHeight(48.dp),
//        backgroundColor = colors().surface,
//        elevation = 20.dp
//    ) {
//        navigationItems.forEach {
//            val backgroundTint = if (it == selectedNavigation.value) Color(0x337d939c) else colors().surface
//            BottomNavigationItem(
//                modifier = Modifier.drawBackground(backgroundTint),
//                icon = { VectorImage(id = it.icon) },
//                selected = selectedNavigation.value == it,
//                onSelected = { selectedNavigation.value = it }
//            )
//        }
//    }
//}
//
//sealed class BottomNavigationItem(val icon: Int) {
//    object Home : BottomNavigationItem(R.drawable.ic_home)
//    object Search : BottomNavigationItem(R.drawable.ic_twotone_search_24)
//    object Library : BottomNavigationItem(R.drawable.ic_layers)
//    object Profile : BottomNavigationItem(R.drawable.ic_user)
//}
//
//val navigationItems = arrayOf(
//    BottomNavigationItem.Home,
//    BottomNavigationItem.Search,
//    BottomNavigationItem.Library,
//    BottomNavigationItem.Profile
//)
