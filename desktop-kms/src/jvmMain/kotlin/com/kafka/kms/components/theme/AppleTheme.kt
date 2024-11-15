package com.kafka.kms.components.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val AppleLight = lightColorScheme(
    // Primary colors
    primary = Color(0xFF020617),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF0F172A),
    onPrimaryContainer = Color(0xFF020617),
    
    // Secondary colors
    secondary = Color(0xFFF8FAFC),
    onSecondary = Color(0xFF020617),
    secondaryContainer = Color(0xFFE2E8F0),
    onSecondaryContainer = Color(0xFF020617),
    
    // Tertiary colors
    tertiary = Color(0xFF64748B),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF94A3B8),
    onTertiaryContainer = Color(0xFF020617),
    
    // Background colors
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF020617),
    
    // Surface colors
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF020617),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF334155),
    surfaceTint = Color(0xFF020617),
    
    // Error colors
    error = Color(0xFFDC2626),
    onError = Color.White,
    errorContainer = Color(0xFFFEE2E2),
    onErrorContainer = Color(0xFFB91C1C),
    
    // Additional colors
    outline = Color(0xFF94A3B8),
    outlineVariant = Color(0xFFCBD5E1),
    scrim = Color(0x99020617),
    inverseSurface = Color(0xFF1E293B),
    inverseOnSurface = Color(0xFFF8FAFC),
    inversePrimary = Color(0xFFE2E8F0),
)

private val AppleDark = darkColorScheme(
    // Primary colors
    primary = Color(0xFFF8FAFC),
    onPrimary = Color(0xFF020617),
    primaryContainer = Color(0xFFE2E8F0),
    onPrimaryContainer = Color(0xFF020617),
    
    // Secondary colors
    secondary = Color(0xFF1E293B),
    onSecondary = Color(0xFFF8FAFC),
    secondaryContainer = Color(0xFF334155),
    onSecondaryContainer = Color(0xFFF8FAFC),
    
    // Tertiary colors
    tertiary = Color(0xFF94A3B8),
    onTertiary = Color(0xFF020617),
    tertiaryContainer = Color(0xFF64748B),
    onTertiaryContainer = Color(0xFFF8FAFC),
    
    // Background colors
    background = Color(0xFF0A0F1A),
    onBackground = Color(0xFFF8FAFC),
    
    // Surface colors
    surface = Color(0xFF000509),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF111827),
    onSurfaceVariant = Color(0xFFCBD5E1),
    surfaceTint = Color(0xFFF8FAFC),
    
    // Error colors
    error = Color(0xFFFEE2E2),
    onError = Color(0xFFB91C1C),
    errorContainer = Color(0xFFDC2626),
    onErrorContainer = Color(0xFFFFFFFF),
    
    // Additional colors
    outline = Color(0xFF64748B),
    outlineVariant = Color(0xFF334155),
    scrim = Color(0x99F8FAFC),
    inverseSurface = Color(0xFFF8FAFC),
    inverseOnSurface = Color(0xFF020617),
    inversePrimary = Color(0xFF1E293B),
)

private val AppleTypography = Typography(
    // Large titles (used for main headers)
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        lineHeight = 41.sp,
        letterSpacing = 0.37.sp
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        letterSpacing = 0.36.sp
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.35.sp
    ),

    // Headers
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 25.sp,
        letterSpacing = 0.38.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.43).sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.24).sp
    ),

    // Body text
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.43).sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.24).sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.08).sp
    ),

    // Labels
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.08).sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 13.sp,
        letterSpacing = 0.06.sp
    )
)

@Composable
fun AppleTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) AppleDark else AppleLight,
        typography = AppleTypography,
        content = content
    )
} 