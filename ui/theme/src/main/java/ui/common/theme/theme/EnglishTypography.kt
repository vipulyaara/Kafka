package ui.common.theme.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

internal object EnglishTypography {

    val BodyLarge = TextStyle(
        fontFamily = DefaultFont,
        fontWeight = FontWeight.Medium,
        fontSize = TypeScale.BodyLargeSize,
        lineHeight = TypeScale.BodyLargeLineHeight,
        letterSpacing = TypeScale.BodyLargeTracking,
    )
    val BodyMedium = TextStyle(
        fontFamily = DefaultFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = TypeScale.BodyMediumLineHeight,
    )
    val BodySmall = TextStyle(
        fontFamily = DefaultFont,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 16.sp,
    )
    val DisplayLarge = TextStyle(
        fontFamily = DefaultFont,
        fontWeight = FontWeight.Light,
        fontSize = 16.sp,
        fontStyle = FontStyle.Italic,
        lineHeight = TypeScale.DisplayLargeLineHeight,
        letterSpacing = TypeScale.DisplayLargeTracking,
    )
    val ContentProse = TextStyle(
        fontFamily = ProseFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.0.sp,
    )
    val DisplaySmall = TextStyle(
        fontFamily = DefaultFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = TypeScale.DisplaySmallSize,
        lineHeight = TypeScale.DisplaySmallLineHeight,
        letterSpacing = TypeScale.DisplaySmallTracking,
    )
    val HeadlineLarge = TextStyle(
        fontFamily = DefaultFont,
        fontWeight = FontWeight.Bold,
        fontSize = TypeScale.HeadlineLargeSize,
        lineHeight = TypeScale.HeadlineLargeLineHeight,
        letterSpacing = TypeScale.HeadlineLargeTracking,
    )
    val HeadlineMedium = TextStyle(
        fontFamily = DefaultFont,
        fontWeight = TypeScale.HeadlineMediumWeight,
        fontSize = TypeScale.HeadlineMediumSize,
        lineHeight = TypeScale.HeadlineMediumLineHeight,
    )
    val HeadlineSmall = TextStyle(
        fontFamily = Poppins,
        fontWeight = TypeScale.HeadlineSmallWeight,
        fontSize = 20.sp,
        letterSpacing = TypeScale.HeadlineSmallTracking,
    )
    val LabelLarge = TextStyle(
        fontFamily = DefaultFont,
        fontWeight = TypeScale.LabelLargeWeight,
        fontSize = TypeScale.LabelLargeSize,
        lineHeight = TypeScale.LabelLargeLineHeight,
    )
    val LabelMedium = TextStyle(
        fontFamily = DefaultFont,
        fontWeight = FontWeight.Medium,
        fontSize = TypeScale.LabelMediumSize,
        lineHeight = TypeScale.LabelMediumLineHeight,
    )
    val LabelSmall = TextStyle(
        fontFamily = DefaultFont,
        fontWeight = FontWeight.Bold,
        fontSize = TypeScale.LabelSmallSize,
        lineHeight = TypeScale.LabelSmallLineHeight,
    )
    val TitleLarge = TextStyle(
        fontFamily = DefaultFont,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = TypeScale.TitleLargeLineHeight,
    )
    val TitleMedium = TextStyle(
        fontFamily = DefaultFont,
        fontWeight = TypeScale.TitleMediumWeight,
        fontSize = 16.sp,
        lineHeight = TypeScale.TitleMediumLineHeight,
    )
    val TitleSmall = TextStyle(
        fontFamily = DefaultFont,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = TypeScale.TitleSmallLineHeight,
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
