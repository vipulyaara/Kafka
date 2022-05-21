package ui.common.theme.theme.type

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

internal object HindiTypography {
    val BodyLarge = TextStyle(
        fontFamily = NotoDevnagri,
        fontWeight = TypeScale.BodyLargeWeight,
        fontSize = TypeScale.BodyLargeSize,
        lineHeight = TypeScale.BodyLargeLineHeight,
        letterSpacing = TypeScale.BodyLargeTracking,
    )
    val BodyMedium = TextStyle(
        fontFamily = Laila,
        fontWeight = FontWeight.Medium,
        fontSize = TypeScale.BodyMediumSize,
        lineHeight = TypeScale.BodyMediumLineHeight,
        letterSpacing = TypeScale.BodyMediumTracking,
    )
    val BodySmall = TextStyle(
        fontFamily = Laila,
        fontWeight = TypeScale.BodySmallWeight,
        fontSize = 14.sp,
        lineHeight = TypeScale.BodySmallLineHeight,
        letterSpacing = TypeScale.BodySmallTracking,
    )
    val Content = TextStyle(
        fontFamily = Laila,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 12.sp,
        letterSpacing = TypeScale.DisplayLargeTracking,
    )
    val ContentProse = TextStyle(
        fontFamily = Laila,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Normal,
        fontSize = 18.sp,
    )
    val DisplaySmall = TextStyle(
        fontFamily = Laila,
        fontWeight = TypeScale.DisplaySmallWeight,
        fontSize = TypeScale.DisplaySmallSize,
        lineHeight = TypeScale.DisplaySmallLineHeight,
        letterSpacing = TypeScale.DisplaySmallTracking,
    )
    val HeadlineLarge = TextStyle(
        fontFamily = NotoDevnagri,
        fontWeight = TypeScale.HeadlineLargeWeight,
        fontSize = TypeScale.HeadlineLargeSize,
        lineHeight = TypeScale.HeadlineLargeLineHeight,
        letterSpacing = TypeScale.HeadlineLargeTracking,
    )
    val HeadlineMedium = TextStyle(
        fontFamily = Rozha,
        fontWeight = TypeScale.HeadlineMediumWeight,
        fontSize = TypeScale.HeadlineMediumSize,
        lineHeight = TypeScale.HeadlineMediumLineHeight,
        letterSpacing = TypeScale.HeadlineMediumTracking,
    )
    val HeadlineSmall = TextStyle(
        fontFamily = NotoDevnagri,
        fontWeight = TypeScale.HeadlineSmallWeight,
        fontSize = TypeScale.HeadlineSmallSize,
        lineHeight = TypeScale.HeadlineSmallLineHeight,
        letterSpacing = TypeScale.HeadlineSmallTracking,
    )
    val LabelLarge = TextStyle(
        fontFamily = NotoDevnagri,
        fontWeight = TypeScale.LabelLargeWeight,
        fontSize = TypeScale.LabelLargeSize,
        lineHeight = TypeScale.LabelLargeLineHeight,
        letterSpacing = TypeScale.LabelLargeTracking,
    )
    val LabelMedium = TextStyle(
        fontFamily = NotoDevnagri,
        fontWeight = TypeScale.LabelMediumWeight,
        fontSize = TypeScale.LabelMediumSize,
        lineHeight = TypeScale.LabelMediumLineHeight,
        letterSpacing = TypeScale.LabelMediumTracking,
    )
    val LabelSmall = TextStyle(
        fontFamily = NotoDevnagri,
        fontWeight = TypeScale.LabelSmallWeight,
        fontSize = TypeScale.LabelSmallSize,
        lineHeight = TypeScale.LabelSmallLineHeight,
        letterSpacing = TypeScale.LabelSmallTracking,
    )
    val TitleLarge = TextStyle(
        fontFamily = NotoDevnagri,
        fontWeight = TypeScale.TitleLargeWeight,
        fontSize = TypeScale.TitleLargeSize,
        lineHeight = TypeScale.TitleLargeLineHeight,
        letterSpacing = TypeScale.TitleLargeTracking,
    )
    val TitleMedium = TextStyle(
        fontFamily = NotoDevnagri,
        fontWeight = TypeScale.TitleMediumWeight,
        fontSize = TypeScale.TitleMediumSize,
        lineHeight = TypeScale.TitleMediumLineHeight,
        letterSpacing = TypeScale.TitleMediumTracking,
    )
    val TitleSmall = TextStyle(
        fontFamily = Laila,
        fontWeight = FontWeight.SemiBold,
        fontSize = TypeScale.TitleSmallSize,
        lineHeight = TypeScale.TitleSmallLineHeight,
        letterSpacing = TypeScale.TitleSmallTracking,
    )
}

val TypographyHindi = Typography(
    displayLarge = HindiTypography.Content,
    displayMedium = HindiTypography.ContentProse,
    displaySmall = HindiTypography.DisplaySmall,
    headlineLarge = HindiTypography.HeadlineLarge,
    headlineMedium = HindiTypography.HeadlineMedium,
    headlineSmall = HindiTypography.HeadlineSmall,
    titleLarge = HindiTypography.TitleLarge,
    titleMedium = HindiTypography.TitleMedium,
    titleSmall = HindiTypography.TitleSmall,
    bodyLarge = HindiTypography.BodyLarge,
    bodyMedium = HindiTypography.BodyMedium,
    bodySmall = HindiTypography.BodySmall,
    labelLarge = HindiTypography.LabelLarge,
    labelMedium = HindiTypography.LabelMedium,
    labelSmall = HindiTypography.LabelSmall,
)
//defaultFontFamily = NotoDevnagri,
//h1 = TextStyle(
//fontFamily = Laila,
//fontSize = 18.sp,
//fontWeight = FontWeight.Normal
//),
//h2 = TextStyle(
//fontFamily = Rozha,
//fontSize = 24.sp,
//fontWeight = FontWeight.Bold,
//lineHeight = 24.sp
//),
//h3 = TextStyle(
//fontSize = 48.sp,
//fontWeight = FontWeight.SemiBold,
//lineHeight = 32.sp
//),
//h4 = TextStyle(
//fontSize = 32.sp,
//fontWeight = FontWeight.SemiBold,
//lineHeight = 32.sp
//),
//h5 = TextStyle(
//fontSize = 24.sp,
//fontWeight = FontWeight.SemiBold,
//lineHeight = 24.sp
//),
//h6 = TextStyle(
//fontFamily = Laila,
//fontSize = 18.sp,
//fontWeight = FontWeight.Normal,
//lineHeight = 20.sp
//),
//subtitle1 = TextStyle(
//fontSize = 14.sp,
//fontWeight = FontWeight.Bold,
//lineHeight = 16.sp
//),
//subtitle2 = TextStyle(
//fontFamily = Laila,
//fontSize = 13.sp,
//fontWeight = FontWeight.SemiBold
//),
//body1 = TextStyle(
//fontSize = 14.sp,
//fontWeight = FontWeight.Normal,
//lineHeight = 22.sp,
//letterSpacing = 0.25.sp
//),
//body2 = TextStyle(
//fontSize = 13.sp,
//fontWeight = FontWeight.Medium,
//lineHeight = 20.sp,
//letterSpacing = 0.25.sp
//),
//button = TextStyle(
//fontSize = 14.sp,
//fontWeight = FontWeight.SemiBold,
//lineHeight = 16.sp
//),
//caption = TextStyle(
//fontSize = 11.sp,
//fontWeight = FontWeight.Medium,
//lineHeight = 14.sp
//),
//overline = TextStyle(
//fontSize = 10.sp,
//fontWeight = FontWeight.SemiBold,
//lineHeight = 16.sp
//)
//)
