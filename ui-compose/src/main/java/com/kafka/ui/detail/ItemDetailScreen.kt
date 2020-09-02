//package com.kafka.ui.detail
//
//import android.view.ViewGroup
//import androidx.compose.material.MaterialTheme
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.Recomposer
//import androidx.lifecycle.LiveData
//import com.kafka.ui.alpha
//import com.kafka.ui.colors
//import com.kafka.ui.home.ContentItem
//import com.kafka.ui.home.FullScreenLoader
//import com.kafka.ui.observe
//fun ViewGroup.composeContentDetailScreen(
//    state: LiveData<com.kafka.content.ui.detail.ItemDetailViewState>,
//    actioner: (com.kafka.content.ui.detail.ItemDetailAction) -> Unit
//): Any = setContent(Recomposer.current()) {
//    val viewState = observe(state)
//    if (viewState != null) {
//        MaterialThemeFromMdcTheme {
//            ContentDetailScreen(viewState, actioner)
//            viewState.error?.let { context.showToast(it) }
//        }
//    }
//}
//
//@Composable
//private fun ContentDetailScreen(viewState: com.kafka.content.ui.detail.ItemDetailViewState, actioner: (com.kafka.content.ui.detail.ItemDetailAction) -> Unit) {
//    if (viewState.isLoading && viewState.itemDetail?.itemId == null) {
//        FullScreenLoader()
//    } else {
//        ContentDetail(viewState = viewState, actioner = actioner)
//    }
//}
//
//@Composable
//private fun ContentDetail(viewState: com.kafka.content.ui.detail.ItemDetailViewState, actioner: (com.kafka.content.ui.detail.ItemDetailAction) -> Unit) {
//    VerticalScroller {
//        Column {
//            Spacer(Modifier.padding(top = 24.dp))
//            ItemDetailView(
//                itemDetailViewState = viewState,
//                actioner = actioner
//            )
//            Spacer(Modifier.height(48.dp))
//            ItemsByCreator(viewState = viewState, actioner = actioner)
//            Spacer(Modifier.height(48.dp))
//        }
//    }
//}
//
//@Composable
//fun ItemsByCreator(viewState: com.kafka.content.ui.detail.ItemDetailViewState, actioner: (com.kafka.content.ui.detail.ItemDetailAction) -> Unit) {
//    viewState.itemsByCreator?.letEmpty {
//        ProvideEmphasis(emphasis = EmphasisAmbient.current.medium) {
//            Text(
//                text = "More by ${viewState.itemDetail?.creator}",
//                style = MaterialTheme.typography.h6,
//                color = colors().onPrimary.alpha(alpha = 0.6f),
//                modifier = Modifier.padding(start = 24.dp)
//            )
//        }
//        Spacer(modifier = Modifier.preferredHeight(16.dp))
//        HorizontalScroller {
//            Row {
//                viewState.itemsByCreator.forEach { content ->
//                    ContentItem(content) {
//                        actioner(com.kafka.content.ui.detail.ItemDetailAction.RelatedItemClick(it))
//                    }
//                    Spacer(modifier = Modifier.preferredWidth(8.dp))
//                }
//            }
//        }
//
//        Spacer(Modifier.height(48.dp))
//    }
//}
