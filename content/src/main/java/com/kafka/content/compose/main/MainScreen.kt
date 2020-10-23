package com.kafka.content.compose.main

import androidx.activity.OnBackPressedDispatcher
import androidx.annotation.DrawableRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.ExperimentalLazyDsl
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.savedinstancestate.rememberSavedInstanceState
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.kafka.content.R
import com.kafka.content.compose.Actions
import com.kafka.content.compose.Destination
import com.kafka.content.compose.detail.ItemDetail
import com.kafka.content.compose.feed.Feed
import com.kafka.ui.Navigator
import com.kafka.ui.theme.KafkaColors

@ExperimentalMaterialApi
@ExperimentalLazyDsl
@Composable
fun MainScreen(backDispatcher: OnBackPressedDispatcher) {
    val navigator: Navigator<Destination> = rememberSavedInstanceState(saver = Navigator.saver(backDispatcher)) {
        Navigator(Destination.Home, backDispatcher)
    }
    val actions = remember(navigator) { Actions(navigator) }

    val selectedNavigation: MutableState<MainTabItem> = mutableStateOf(MainTabItem.Feed)

    val (currentSection, setCurrentSection) = savedInstanceState { MainTabItem.Feed }

    Scaffold(
        backgroundColor = KafkaColors.background,
        bottomBar = { BottomBar(currentSection, setCurrentSection) }
    ) {
        Crossfade(navigator.current) { destination ->
            when (destination) {
                Destination.Home -> when (selectedNavigation.value) {
                    MainTabItem.Feed -> Feed(actions)
                    MainTabItem.Search -> Empty()
                    MainTabItem.Library -> Empty()
                    MainTabItem.Profile -> Empty()
                }
                is Destination.ItemDetail -> ItemDetail(itemId = destination.itemId, actions = actions)
            }
        }
    }
}

@Composable
fun Empty() {

}

@Composable
fun TopBar() {
    TopAppBar(
        modifier = Modifier.padding(horizontal = 16.dp),
        title = { Text(text = "") },
        backgroundColor = KafkaColors.background,
        navigationIcon = { Icon(vectorResource(id = R.drawable.ic_menu)) },
        actions = { Icon(vectorResource(id = R.drawable.ic_sun)) },
        elevation = 0.dp
    )
}

@Composable
private fun BottomBar(currentSection: MainTabItem, setCurrentSection: (MainTabItem) -> Unit) {
    BottomNavigation(backgroundColor = KafkaColors.primary) {
        MainTabItem.values().forEach {
            BottomNavigationItem(
                icon = { Icon(vectorResource(id = it.icon), tint = KafkaColors.background) },
                selected = currentSection == it,
                onClick = { setCurrentSection(it) },
                selectedContentColor = Color(0xFF000000)
            )
        }
    }
}

private enum class MainTabItem(
    val title: String,
    @DrawableRes val icon: Int
) {
    Feed("Feed", R.drawable.ic_home),
    Search("Search", R.drawable.ic_twotone_search_24),
    Library("Library", R.drawable.ic_layers),
    Profile("Profile", R.drawable.ic_user)
}
