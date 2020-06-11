package com.kafka.ui.detail

import android.view.ViewGroup
import androidx.compose.Composable
import androidx.compose.Recomposer
import androidx.lifecycle.LiveData
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.HorizontalScroller
import androidx.ui.foundation.Text
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.*
import androidx.ui.material.EmphasisAmbient
import androidx.ui.material.MaterialTheme
import androidx.ui.material.ProvideEmphasis
import androidx.ui.material.Surface
import androidx.ui.unit.dp
import com.kafka.data.extensions.letEmpty
import com.kafka.ui.colors
import com.kafka.ui.home.ContentItem
import com.kafka.ui.home.FullScreenLoader
import com.kafka.ui.observe
import com.kafka.ui_common.showToast
import dev.chrisbanes.accompanist.mdctheme.MaterialThemeFromMdcTheme

fun ViewGroup.composeContentDetailScreen(
    state: LiveData<ItemDetailViewState>,
    actioner: (ItemDetailAction) -> Unit
): Any = setContent(Recomposer.current()) {
    val viewState = observe(state)
    if (viewState != null) {
        MaterialThemeFromMdcTheme {
            ContentDetailScreen(viewState, actioner)
            viewState.error?.let { context.showToast(it) }
        }
    }
}

@Composable
private fun ContentDetailScreen(viewState: ItemDetailViewState, actioner: (ItemDetailAction) -> Unit) {
    if (viewState.isLoading && viewState.itemDetail?.itemId == null) {
        FullScreenLoader()
    } else {
        ContentDetail(viewState = viewState, actioner = actioner)
    }
}

@Composable
private fun ContentDetail(viewState: ItemDetailViewState, actioner: (ItemDetailAction) -> Unit) {
    Surface(color = colors().primaryVariant) {
        VerticalScroller {
            Column {
                Spacer(Modifier.padding(top = 24.dp))
                ItemDetailView(
                    itemDetail = viewState.itemDetail,
                    recentItem = viewState.recentItem,
                    actioner = actioner
                )
                Spacer(Modifier.padding(top = 48.dp))
                ItemsByCreator(viewState = viewState, actioner = actioner)
                Spacer(modifier = Modifier.preferredHeight(48.dp))
            }
        }
    }
}

@Composable
fun ItemsByCreator(viewState: ItemDetailViewState, actioner: (ItemDetailAction) -> Unit) {
    viewState.itemsByCreator?.letEmpty {
        ProvideEmphasis(emphasis = EmphasisAmbient.current.medium) {
            Text(
                text = "More by ${viewState.itemDetail?.creator}",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(start = 24.dp)
            )
        }
        Spacer(modifier = Modifier.preferredHeight(16.dp))
        HorizontalScroller {
            Row {
                viewState.itemsByCreator.forEach { content ->
                    ContentItem(content) {
                        actioner(ItemDetailAction.RelatedItemClick(it))
                    }
                    Spacer(modifier = Modifier.preferredWidth(8.dp))
                }
            }
        }
    }
}
