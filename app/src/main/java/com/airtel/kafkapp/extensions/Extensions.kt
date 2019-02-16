package com.airtel.kafkapp.extensions

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import com.airtel.data.data.config.kodeinInstance
import com.airtel.data.data.config.logging.Logger
import com.airtel.kafkapp.R
import org.kodein.di.generic.instance
import java.util.Random

/**
 * @author Vipul Kumar; dated 22/01/19.
 */

/**
 * "let"s when the collection is non-null and non-empty
 * */
fun <T : Collection<*>> T?.letEmpty(f: (it: T) -> Unit) {
    if (this != null && this.isNotEmpty()) f(this)
}

internal val logger: Logger by kodeinInstance.instance()

fun getRandomCoverResource(): Int {
    val covers = arrayOf(
        R.drawable.img_cover_1,
        R.drawable.img_cover_2,
        R.drawable.img_cover_3,
        R.drawable.img_cover_4,
        R.drawable.img_cover_5,
        R.drawable.img_cover_6,
        R.drawable.img_cover_7,
        R.drawable.img_cover_8,
        R.drawable.img_cover_9,
        R.drawable.img_cover_10
    )

    return covers[Random().nextInt(covers.size - 1)]
}

fun TextView.spanColor(textToBold: String, fullText: String, targetColor: Int) =
    SpannableStringBuilder(fullText).apply {
        setSpan(
            ForegroundColorSpan(targetColor),
            fullText.indexOf(textToBold),
            (fullText.indexOf(textToBold) + textToBold.length),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

fun illegalArgumentException(msg: String): Nothing = throw RuntimeException(msg)


