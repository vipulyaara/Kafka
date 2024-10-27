package com.kafka.library

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kafka.library.favorites.FavoriteViewModel
import com.kafka.library.favorites.Favorites
import com.kafka.ui.components.ProvideScaffoldPadding
import com.kafka.ui.components.scaffoldPadding

@Composable
fun LibraryScreen(favoriteViewModel: FavoriteViewModel) {
    Scaffold { padding ->
        ProvideScaffoldPadding(padding = padding) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = scaffoldPadding().calculateTopPadding())
            ) {
                Favorites(favoriteViewModel)
            }
        }
    }
}
