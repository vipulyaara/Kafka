package com.kafka.ui.content

import android.view.ViewGroup
import androidx.compose.Composable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.ui.foundation.HorizontalScroller
import androidx.ui.foundation.Text
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.*
import androidx.ui.material.*
import androidx.ui.unit.dp
import com.kafka.ui.MaterialThemeFromAndroidTheme
import com.kafka.ui.home.ContentItem
import com.kafka.ui.observe
import com.kafka.ui.setContentWithLifecycle

fun ViewGroup.composeContentDetailScreen(
    lifecycleOwner: LifecycleOwner,
    state: LiveData<ContentDetailViewState>,
    actioner: (ContentDetailAction) -> Unit
): Any = setContentWithLifecycle(lifecycleOwner) {
    val viewState = observe(state)
    if (viewState != null) {
        MaterialThemeFromAndroidTheme(context) {
            ContentDetailScreen(viewState, actioner)
        }
    }
}

@Composable
private fun ContentDetailScreen(
    viewState: ContentDetailViewState,
    actioner: (ContentDetailAction) -> Unit
) {
    if (viewState.isLoading && viewState.itemDetail?.itemId == null) {
        Container(modifier = LayoutAlign.Center + LayoutPadding(top = 64.dp)) {
            CircularProgressIndicator()
        }
    } else {
        ContentDetail(viewState = viewState, actioner = actioner)
    }
}

@Composable
private fun ContentDetail(
    viewState: ContentDetailViewState,
    actioner: (ContentDetailAction) -> Unit
) {
    Surface(color = MaterialTheme.colors.background) {
        VerticalScroller {
            Column {
                Spacer(LayoutPadding(top = 24.dp))
                ContentDetailItem(itemDetail = viewState.itemDetail, actioner = actioner)
                Spacer(LayoutPadding(top = 24.dp))

                ProvideEmphasis(emphasis = EmphasisAmbient.current.disabled) {
                    Text(
                        text = "More by ${viewState.itemDetail?.creator}",
                        style = MaterialTheme.typography.h6,
                        modifier = LayoutPadding(start = 16.dp)
                    )
                }

                Spacer(modifier = LayoutHeight(16.dp))
                HorizontalScroller {
                    Row {
                        viewState.itemsByCreator?.forEach { content ->
                            ContentItem(content) {
                                actioner(ContentDetailAction.ContentItemClick(it))
                            }
                            Spacer(modifier = LayoutWidth(8.dp))
                        }
                    }
                }
                Spacer(modifier = LayoutHeight(48.dp))
            }
        }
    }
}
