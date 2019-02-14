package com.airtel.kafkapp.extensions

import android.text.Html
import android.widget.TextView
import androidx.databinding.BindingAdapter

/**
 * @author Vipul Kumar; dated 14/02/19.
 */
@BindingAdapter("htmlText")
fun TextView.htmlText(htmlText: String?) {
    htmlText?.let { text = Html.fromHtml(htmlText) }
}
