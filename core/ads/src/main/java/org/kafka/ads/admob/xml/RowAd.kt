package com.kafka.ads.admob.xml

import android.view.View
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.google.android.gms.ads.nativead.NativeAd
import com.kafka.core.ads.databinding.RowAdContainerBinding

@Composable
fun RowXmlAd(loadedAd: NativeAd?) {
    AndroidViewBinding(
        modifier = Modifier.padding(start = 4.dp, top = 8.dp, end = 4.dp, bottom = 4.dp),
        factory = (RowAdContainerBinding::inflate)
    ) {
        loadedAd?.let { nativead ->
            // Display the loaded ad
            nativead.icon?.let { icon ->
                this.adIcon.setImageDrawable(icon.drawable)
            }
            nativead.headline?.let { headline ->
                this.adHeadline.text = headline
            }
            nativead.body?.let { body ->
                this.adBody.text = body
            }

            if (nativead.images.isNotEmpty()) {
                val image = nativead.images[0]
                this.adImage.setImageDrawable(image.drawable)
                this.adImage.visibility = View.VISIBLE
            }

            nativead.callToAction?.let { actionbutton ->
                this.adActionbutton.text = actionbutton
                this.nativeadview.callToActionView = this.adActionbutton
            }
            this.nativeadview.setNativeAd(nativead)
        }
    }
}
