package ui.common.theme.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

internal object EnglishTypography {

    val BodyLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = TypeScale.BodyLargeSize,
        lineHeight = TypeScale.BodyLargeLineHeight,
        letterSpacing = TypeScale.BodyLargeTracking,
    )
    val BodyMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = TypeScale.BodyMediumLineHeight,
    )
    val BodySmall = TextStyle(
        fontFamily = Inter,
        fontWeight = TypeScale.BodySmallWeight,
        fontSize = 13.sp,
    )
    val DisplayLarge = TextStyle(
        fontFamily = Circular,
        fontWeight = FontWeight.Light,
        fontSize = 16.sp,
        fontStyle = FontStyle.Italic,
        lineHeight = TypeScale.DisplayLargeLineHeight,
        letterSpacing = TypeScale.DisplayLargeTracking,
    )
    val ContentProse = TextStyle(
        fontFamily = Circular,
        fontWeight = FontWeight.Light,
        fontSize = 18.sp,
    )
    val DisplaySmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = TypeScale.DisplaySmallSize,
        lineHeight = TypeScale.DisplaySmallLineHeight,
        letterSpacing = TypeScale.DisplaySmallTracking,
    )
    val HeadlineLarge = TextStyle(
        fontFamily = TypeScale.HeadlineLargeFont,
        fontWeight = TypeScale.HeadlineLargeWeight,
        fontSize = TypeScale.HeadlineLargeSize,
        lineHeight = TypeScale.HeadlineLargeLineHeight,
        letterSpacing = TypeScale.HeadlineLargeTracking,
    )
    val HeadlineMedium = TextStyle(
        fontFamily = TypeScale.HeadlineMediumFont,
        fontWeight = TypeScale.HeadlineMediumWeight,
        fontSize = TypeScale.HeadlineMediumSize,
        lineHeight = TypeScale.HeadlineMediumLineHeight,
        letterSpacing = TypeScale.HeadlineMediumTracking,
    )
    val HeadlineSmall = TextStyle(
        fontFamily = TypeScale.HeadlineSmallFont,
        fontWeight = TypeScale.HeadlineSmallWeight,
        fontSize = TypeScale.HeadlineSmallSize,
        lineHeight = TypeScale.HeadlineSmallLineHeight,
        letterSpacing = TypeScale.HeadlineSmallTracking,
    )
    val LabelLarge = TextStyle(
        fontFamily = TypeScale.LabelLargeFont,
        fontWeight = TypeScale.LabelLargeWeight,
        fontSize = TypeScale.LabelLargeSize,
        lineHeight = TypeScale.LabelLargeLineHeight,
        letterSpacing = TypeScale.LabelLargeTracking,
    )
    val LabelMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = TypeScale.LabelMediumSize,
        lineHeight = TypeScale.LabelMediumLineHeight,
    )
    val LabelSmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = TypeScale.LabelSmallSize,
        lineHeight = TypeScale.LabelSmallLineHeight,
        letterSpacing = TypeScale.LabelSmallTracking,
    )
    val TitleLarge = TextStyle(
        fontFamily = Circular,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = TypeScale.TitleLargeLineHeight,
        letterSpacing = TypeScale.TitleLargeTracking,
    )
    val TitleMedium = TextStyle(
        fontFamily = TypeScale.TitleMediumFont,
        fontWeight = TypeScale.TitleMediumWeight,
        fontSize = 16.sp,
        lineHeight = TypeScale.TitleMediumLineHeight,
        letterSpacing = 0.3.sp,
    )
    val TitleSmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        lineHeight = TypeScale.TitleSmallLineHeight,
        letterSpacing = 0.2.sp,
    )
}

val TypographyEnglish = Typography(
    displayLarge = EnglishTypography.DisplayLarge,
    displayMedium = EnglishTypography.ContentProse,
    displaySmall = EnglishTypography.DisplaySmall,
    headlineLarge = EnglishTypography.HeadlineLarge,
    headlineMedium = EnglishTypography.HeadlineMedium,
    headlineSmall = EnglishTypography.HeadlineSmall,
    titleLarge = EnglishTypography.TitleLarge,
    titleMedium = EnglishTypography.TitleMedium,
    titleSmall = EnglishTypography.TitleSmall,
    bodyLarge = EnglishTypography.BodyLarge,
    bodyMedium = EnglishTypography.BodyMedium,
    bodySmall = EnglishTypography.BodySmall,
    labelLarge = EnglishTypography.LabelLarge,
    labelMedium = EnglishTypography.LabelMedium,
    labelSmall = EnglishTypography.LabelSmall,
)
