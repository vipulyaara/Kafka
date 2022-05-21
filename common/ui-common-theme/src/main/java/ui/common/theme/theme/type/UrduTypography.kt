package ui.common.theme.theme.type

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

internal object UrduTypography {
    val BodyLarge = TextStyle(
        fontFamily = Laila,
        fontWeight = TypeScale.BodyLargeWeight,
        fontSize = TypeScale.BodyLargeSize,
        lineHeight = TypeScale.BodyLargeLineHeight,
        letterSpacing = TypeScale.BodyLargeTracking,
    )
    val BodyMedium = TextStyle(
        fontFamily = TypeScale.BodyMediumFont,
        fontWeight = TypeScale.BodyMediumWeight,
        fontSize = TypeScale.BodyMediumSize,
        lineHeight = TypeScale.BodyMediumLineHeight,
        letterSpacing = TypeScale.BodyMediumTracking,
    )
    val BodySmall = TextStyle(
        fontFamily = TypeScale.BodySmallFont,
        fontWeight = TypeScale.BodySmallWeight,
        fontSize = TypeScale.BodySmallSize,
        lineHeight = TypeScale.BodySmallLineHeight,
        letterSpacing = TypeScale.BodySmallTracking,
    )
    val ContentRegular = TextStyle(
        fontFamily = MehrNastaliq,
        fontWeight = TypeScale.DisplayLargeWeight,
        fontSize = 24.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.1).sp,
    )
    val ContentProse = TextStyle(
        fontFamily = MehrNastaliq,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
    )
    val DisplaySmall = TextStyle(
        fontFamily = TypeScale.DisplaySmallFont,
        fontWeight = TypeScale.DisplaySmallWeight,
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
        fontFamily = Rozha,
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
        fontFamily = TypeScale.LabelMediumFont,
        fontWeight = TypeScale.LabelMediumWeight,
        fontSize = TypeScale.LabelMediumSize,
        lineHeight = TypeScale.LabelMediumLineHeight,
        letterSpacing = TypeScale.LabelMediumTracking,
    )
    val LabelSmall = TextStyle(
        fontFamily = TypeScale.LabelSmallFont,
        fontWeight = TypeScale.LabelSmallWeight,
        fontSize = TypeScale.LabelSmallSize,
        lineHeight = TypeScale.LabelSmallLineHeight,
        letterSpacing = TypeScale.LabelSmallTracking,
    )
    val TitleLarge = TextStyle(
        fontFamily = TypeScale.TitleLargeFont,
        fontWeight = TypeScale.TitleLargeWeight,
        fontSize = TypeScale.TitleLargeSize,
        lineHeight = TypeScale.TitleLargeLineHeight,
        letterSpacing = TypeScale.TitleLargeTracking,
    )
    val TitleMedium = TextStyle(
        fontFamily = TypeScale.TitleMediumFont,
        fontWeight = TypeScale.TitleMediumWeight,
        fontSize = TypeScale.TitleMediumSize,
        lineHeight = TypeScale.TitleMediumLineHeight,
        letterSpacing = TypeScale.TitleMediumTracking,
    )
    val TitleSmall = TextStyle(
        fontFamily = TypeScale.TitleSmallFont,
        fontWeight = TypeScale.TitleSmallWeight,
        fontSize = TypeScale.TitleSmallSize,
        lineHeight = TypeScale.TitleSmallLineHeight,
        letterSpacing = TypeScale.TitleSmallTracking,
    )
}

val TypographyUrdu = Typography(
    displayLarge = UrduTypography.ContentRegular,
    displayMedium = UrduTypography.ContentProse,
    displaySmall = UrduTypography.DisplaySmall,
    headlineLarge = UrduTypography.HeadlineLarge,
    headlineMedium = UrduTypography.HeadlineMedium,
    headlineSmall = UrduTypography.HeadlineSmall,
    titleLarge = UrduTypography.TitleLarge,
    titleMedium = UrduTypography.TitleMedium,
    titleSmall = UrduTypography.TitleSmall,
    bodyLarge = UrduTypography.BodyLarge,
    bodyMedium = UrduTypography.BodyMedium,
    bodySmall = UrduTypography.BodySmall,
    labelLarge = UrduTypography.LabelLarge,
    labelMedium = UrduTypography.LabelMedium,
    labelSmall = UrduTypography.LabelSmall,
)

//val TypographyUrdu = Typography(
//    defaultFontFamily = NotoNastaliq,
//    h1 = TextStyle(
//        fontFamily = MehrNastaliq,
//        fontSize = 22.sp,
//        fontWeight = FontWeight.Normal,
//        lineHeight = 22.sp
//    ),
//    h2 = TextStyle(
//        fontFamily = Rozha,
//        fontSize = 24.sp,
//        fontWeight = FontWeight.Bold,
//        lineHeight = 28.sp
//    ),
//    h3 = TextStyle(
//        fontSize = 48.sp,
//        fontWeight = FontWeight.SemiBold,
//        lineHeight = 32.sp
//    ),
//    h4 = TextStyle(
//        fontSize = 32.sp,
//        fontWeight = FontWeight.SemiBold,
//        lineHeight = 32.sp
//    ),
//    h5 = TextStyle(
//        fontSize = 24.sp,
//        fontWeight = FontWeight.SemiBold,
//        lineHeight = 24.sp
//    ),
//    h6 = TextStyle(
//        fontFamily = MehrNastaliq,
//        fontSize = 20.sp,
//        fontWeight = FontWeight.Normal,
//        lineHeight = 24.sp
//    ),
//    subtitle1 = TextStyle(
//        fontSize = 16.sp,
//        fontWeight = FontWeight.Bold,
//        lineHeight = 18.sp
//    ),
//    subtitle2 = TextStyle(
//        fontSize = 15.sp,
//        fontWeight = FontWeight.Bold,
//        lineHeight = 15.sp
//    ),
//    body1 = TextStyle(
//        fontSize = 18.sp,
//        fontWeight = FontWeight.Medium,
//        lineHeight = 18.sp
//    ),
//    body2 = TextStyle(
//        fontSize = 16.sp,
//        fontWeight = FontWeight.SemiBold,
//        lineHeight = 18.sp
//    ),
//    button = TextStyle(
//        fontSize = 14.sp,
//        fontWeight = FontWeight.SemiBold,
//        lineHeight = 14.sp
//    ),
//    caption = TextStyle(
//        fontSize = 13.sp,
//        fontWeight = FontWeight.Medium,
//        lineHeight = 14.sp
//    ),
//    overline = TextStyle(
//        fontSize = 13.sp,
//        fontWeight = FontWeight.SemiBold,
//        lineHeight = 16.sp
//    )
//)
