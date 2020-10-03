package com.kafka.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.font
import androidx.compose.ui.text.font.fontFamily
import androidx.compose.ui.unit.sp
import com.kafka.ui.R

private val Circular = fontFamily(
    font(R.font.circular, FontWeight.Light),
    font(R.font.circular_std_book, FontWeight.Normal),
    font(R.font.circular_std_medium, FontWeight.Medium),
    font(R.font.circular_std_black, FontWeight.SemiBold)
)

private val Roboto = fontFamily(
    font(R.font.roboto_light, FontWeight.Light),
    font(R.font.roboto_regular, FontWeight.Normal),
    font(R.font.roboto_medium, FontWeight.Medium),
    font(R.font.roboto_bold, FontWeight.SemiBold)
)

private val Alegreya = fontFamily(
    font(R.font.alegreya_bold, FontWeight.Bold)
)

val Typography = Typography(
    h1 = TextStyle(
        fontFamily = Roboto,
        fontSize = 96.sp,
        fontWeight = FontWeight.Light,
        lineHeight = 117.sp,
        letterSpacing = (-1.5).sp
    ),
    h2 = TextStyle(
        fontFamily = Roboto,
        fontSize = 60.sp,
        fontWeight = FontWeight.Light,
        lineHeight = 73.sp,
        letterSpacing = (-0.5).sp
    ),
    h3 = TextStyle(
        fontFamily = Roboto,
        fontSize = 48.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 59.sp
    ),
    h4 = TextStyle(
        fontFamily = Circular,
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 24.sp
    ),
    h5 = TextStyle(
        fontFamily = Circular,
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 24.sp
    ),
    h6 = TextStyle(
        fontFamily = Alegreya,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 24.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = Roboto,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = Alegreya,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 24.sp,
        letterSpacing = 0.1.sp
    ),
    body1 = TextStyle(
        fontFamily = Roboto,
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 18.sp,
        letterSpacing = 0.15.sp
    ),
    body2 = TextStyle(
        fontFamily = Roboto,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 16.sp,
        letterSpacing = 0.25.sp
    ),
    button = TextStyle(
        fontFamily = Roboto,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 16.sp,
        letterSpacing = 1.25.sp
    ),
    caption = TextStyle(
        fontFamily = Circular,
        fontSize = 12.sp,
        fontWeight = FontWeight.Light,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    overline = TextStyle(
        fontFamily = Roboto,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 16.sp,
        letterSpacing = 1.sp
    )
)
