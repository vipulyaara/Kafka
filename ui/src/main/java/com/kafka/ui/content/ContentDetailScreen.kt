package com.kafka.ui.content

import android.view.ViewGroup
import androidx.compose.Composable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.ui.core.Text
import androidx.ui.foundation.HorizontalScroller
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.*
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.material.EmphasisLevels
import androidx.ui.material.MaterialTheme
import androidx.ui.material.ProvideEmphasis
import androidx.ui.material.surface.Surface
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
    if (viewState.isLoading && viewState.contentDetail?.contentId == null) {
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
    Surface(color = MaterialTheme.colors().background) {
        VerticalScroller {
            Column {
                Spacer(LayoutPadding(top = 24.dp))
                ContentDetailItem(contentDetail = viewState.contentDetail, actioner = actioner)
                Spacer(LayoutPadding(top = 24.dp))

                ProvideEmphasis(emphasis = EmphasisLevels().disabled) {
                    Text(
                        text = "More by ${viewState.contentDetail?.creator}",
                        style = MaterialTheme.typography().h6,
                        modifier = LayoutPadding(left = 16.dp)
                    )
                }

                Spacer(modifier = LayoutHeight(16.dp))
                HorizontalScroller {
                    Row {
                        viewState.itemsByCreator?.contents?.forEach { content ->
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