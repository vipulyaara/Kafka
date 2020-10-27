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
import androidx.compose.runtime.remember
import androidx.compose.runtime.savedinstancestate.rememberSavedInstanceState
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.kafka.content.R
import com.kafka.content.compose.Actions
import com.kafka.content.compose.Destination
import com.kafka.content.compose.detail.ItemDetail
import com.kafka.content.compose.feed.Feed
import com.kafka.content.compose.player.MiniPlayerScaffold
import com.kafka.content.compose.search.Search
import com.kafka.ui_common.navigation.Navigator
import com.kafka.ui_common.theme.KafkaColors
import com.kafka.ui_common.theme.KafkaTheme

@ExperimentalMaterialApi
@ExperimentalLazyDsl
@Composable
fun MainScreen(backDispatcher: OnBackPressedDispatcher) {
    val navigator: Navigator<Destination> = rememberSavedInstanceState(saver = Navigator.saver(backDispatcher)) {
        Navigator(Destination.Home, backDispatcher)
    }
    val actions = remember(navigator) { Actions(navigator) }

    val (currentSection, setCurrentSection) = savedInstanceState { MainTabItem.Feed }

    Scaffold(
        backgroundColor = KafkaColors.background,
        bottomBar = { BottomBar(currentSection, setCurrentSection) }
    ) {
//        PlayerScaffold {
        MiniPlayerScaffold(modifier = Modifier.padding(it)) {
            Crossfade(modifier = Modifier.padding(it), current = navigator.current) { destination ->
                when (destination) {
                    Destination.Home -> when (currentSection) {
                        MainTabItem.Feed -> Feed(actions = actions)
                        MainTabItem.Search -> Search(actions = actions)
                        MainTabItem.Library -> Empty()
                        MainTabItem.Profile -> Empty()
                    }
                    is Destination.ItemDetail -> ItemDetail(itemId = destination.itemId, actions = actions)
                }
            }
            }
//        }

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
    BottomNavigation(backgroundColor = KafkaColors.surface, elevation = 24.dp) {
        MainTabItem.values().forEach {
            BottomNavigationItem(
                icon = { Icon(vectorResource(id = it.icon)) },
                selected = currentSection == it,
                onClick = { setCurrentSection(it) },
                unselectedContentColor = KafkaTheme.colors.iconPrimary,
                selectedContentColor = KafkaTheme.colors.secondary
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
