package com.kafka.user.extensions

import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.databinding.BindingAdapter

/**
 * @author Vipul Kumar; dated 14/02/19.
 */
@BindingAdapter("htmlText")
fun TextView.htmlText(htmlText: String?) {
    htmlText?.let { text = Html.fromHtml(htmlText) }
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

