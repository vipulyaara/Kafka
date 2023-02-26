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
        letterSpacing = TypeScale.BodyLargeTracking,
    )
    val BodyMedium = TextStyle(
        fontFamily = DefaultFont,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
    )
    val BodySmall = TextStyle(
        fontFamily = DefaultFont,
        fontWeight = TypeScale.BodySmallWeight,
        fontSize = 13.sp,
    )
    val DisplayLarge = TextStyle(
        fontFamily = DefaultFont,
        fontWeight = FontWeight.Light,
        fontSize = 16.sp,
        fontStyle = FontStyle.Italic,
        letterSpacing = TypeScale.DisplayLargeTracking,
    )
    val ContentProse = TextStyle(
        fontFamily = ProseFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    )
    val DisplaySmall = TextStyle(
        fontFamily = DefaultFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = TypeScale.DisplaySmallSize,
        letterSpacing = TypeScale.DisplaySmallTracking,
    )
    val HeadlineLarge = TextStyle(
        fontFamily = DefaultFont,
        fontWeight = FontWeight.Bold,
        fontSize = TypeScale.HeadlineLargeSize,
        letterSpacing = TypeScale.HeadlineLargeTracking,
    )
    val HeadlineMedium = TextStyle(
        fontFamily = DefaultFont,
        fontWeight = TypeScale.HeadlineMediumWeight,
        fontSize = TypeScale.HeadlineMediumSize,
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
    )
    val LabelMedium = TextStyle(
        fontFamily = DefaultFont,
        fontWeight = FontWeight.Medium,
        fontSize = TypeScale.LabelMediumSize,
    )
    val LabelSmall = TextStyle(
        fontFamily = DefaultFont,
        fontWeight = FontWeight.Bold,
        fontSize = TypeScale.LabelSmallSize,
    )
    val TitleLarge = TextStyle(
        fontFamily = DefaultFont,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
    )
    val TitleMedium = TextStyle(
        fontFamily = DefaultFont,
        fontWeight = TypeScale.TitleMediumWeight,
        fontSize = 16.sp,
    )
    val TitleSmall = TextStyle(
        fontFamily = DefaultFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
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
