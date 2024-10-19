package com.kafka.ads.admob

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.kafka.common.widgets.shadowMaterial
import com.kafka.ui.components.item.CoverDefaults
import com.kafka.ui.components.item.CoverImage
import com.kafka.ui.components.item.ItemTitleSmall
import ui.common.theme.theme.Dimens

@Composable
fun RowAd() {
    val adState = rememberNativeAdState(LocalContext.current, testAdId)

    if (adState != null) {
        NativeAdView(adState) { ad, _ ->
            RowAdItem(
                coverImage = ad.images.firstOrNull()?.uri,
                icon = ad.icon?.uri,
                title = ad.headline,
                subtitle = ad.body,
                callToAction = ad.callToAction,
                modifier = Modifier
                    .widthIn(max = Dimens.CoverSizeLarge.width)
                    .clickable { }
            )
        }
    } else {
        Surface(
            tonalElevation = Dimens.Elevation02,
            color = MaterialTheme.colorScheme.surface,
            shape = CoverDefaults.shape,
            modifier = Modifier
                .size(Dimens.CoverSizeLarge)
                .shadowMaterial(Dimens.Elevation02, CoverDefaults.shape)
        ) {
        }
    }
}

@Composable
fun RowAdItem(
    coverImage: Any?,
    modifier: Modifier = Modifier,
    icon: Any? = null,
    title: String? = null,
    subtitle: String? = null,
    callToAction: String? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        CoverImage(icon = icon, coverImage = coverImage, callToAction = callToAction)

        Column(
            modifier = Modifier.padding(
                vertical = Dimens.Spacing08,
                horizontal = Dimens.Spacing04
            )
        ) {
            ItemTitleSmall(title = title.orEmpty(), maxLines = 2)
        }
    }
}

@Composable
private fun CoverImage(icon: Any?, coverImage: Any?, callToAction: String? = null) {
    Surface(
        tonalElevation = Dimens.Elevation02,
        color = MaterialTheme.colorScheme.surface,
        shape = CoverDefaults.shape,
        modifier = Modifier
            .size(Dimens.CoverSizeLarge)
            .shadowMaterial(Dimens.Elevation02, CoverDefaults.shape)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.CoverSizeLarge.height),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            CoverHeader(icon = icon)

            CoverImage(
                data = coverImage,
                size = Dimens.CoverSizeLarge,
                placeholder = CoverDefaults.placeholder,
                contentScale = ContentScale.FillWidth,
                shape = RoundedCornerShape(Dimens.RadiusMedium)
            )

            if (callToAction != null) {
                Text(
                    text = callToAction,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .clip(RoundedCornerShape(Dimens.Radius04))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = Dimens.Spacing12, vertical = Dimens.Spacing04)
                )
            }
        }
    }
}

@Composable
private fun CoverHeader(icon: Any?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.Spacing08),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = icon,
            contentDescription = "icon",
            modifier = Modifier
                .size(Dimens.Spacing24)
                .clip(RoundedCornerShape(Dimens.Radius04))
        )

        Text(
            text = "Ad",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .clip(RoundedCornerShape(Dimens.Radius08))
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = Dimens.Spacing12, vertical = Dimens.Spacing04)
        )
    }
}
