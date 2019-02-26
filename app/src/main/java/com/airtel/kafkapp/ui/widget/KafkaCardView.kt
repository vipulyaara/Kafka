package com.airtel.kafkapp.ui.widget

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import com.airtel.kafkapp.R
import com.airtel.kafkapp.extensions.getColor
import com.google.android.material.card.MaterialCardView

/**
 * @author Vipul Kumar; dated 25/02/19.
 */
class KafkaCardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            outlineAmbientShadowColor = getColor(R.color.shadow_dark)
            outlineSpotShadowColor = getColor(R.color.shadow_dark)
        }
    }
}
