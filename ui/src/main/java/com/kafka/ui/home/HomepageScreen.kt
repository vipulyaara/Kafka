package com.kafka.ui.home

import android.view.ViewGroup
import androidx.collection.arraySetOf
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
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.kafka.ui.MaterialThemeFromAndroidTheme
import com.kafka.ui.observe
import com.kafka.ui.setContentWithLifecycle

fun ViewGroup.composeHomepageScreen(
    lifecycleOwner: LifecycleOwner,
    state: LiveData<HomepageViewState>,
    actioner: (HomepageAction) -> Unit
): Any = setContentWithLifecycle(lifecycleOwner) {
    val viewState = observe(state)
    if (viewState != null) {
        MaterialThemeFromAndroidTheme(context) {
            HomepageScreen(viewState, actioner)
        }
    }
}

@Composable
private fun HomepageScreen(
    viewState: HomepageViewState,
    actioner: (HomepageAction) -> Unit
) {
    if (viewState.isLoading && viewState.items.isNullOrEmpty()) {
        Container(modifier = LayoutAlign.Center + LayoutPadding(top = 64.dp)) {
            CircularProgressIndicator()
        }
    } else {
        HomepageList(viewState = viewState, actioner = actioner)
    }
}

@Composable
private fun HomepageList(
    viewState: HomepageViewState,
    actioner: (HomepageAction) -> Unit
) {
    VerticalScroller {
        Column {
            viewState.items?.forEach { item ->
                Spacer(modifier = LayoutHeight(24.dp))

                ProvideEmphasis(emphasis = EmphasisLevels().disabled) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography().body1,
                        modifier = LayoutPadding(left = 16.dp)
                    )
                }

                Spacer(modifier = LayoutHeight(12.dp))

                HorizontalScroller {
                    Row {
                        item.contents?.forEach { content ->
                            ContentItem(content) { actioner(ContentItemClick(it))}
                            Spacer(modifier = LayoutWidth(8.dp))
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun HomepageScreenPreview() = HomepageScreen(viewState = HomepageViewState(
    arraySetOf()
), actioner = {})