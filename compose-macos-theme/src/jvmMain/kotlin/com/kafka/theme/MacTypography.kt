package com.kafka.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object MacTypography {
    val h1 = TextStyle(
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        color = MacColors.TextPrimary
    )
    
    val h2 = TextStyle(
        fontSize = 22.sp,
        fontWeight = FontWeight.SemiBold,
        color = MacColors.TextPrimary
    )
    
    val body = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = MacColors.TextPrimary
    )
    
    val caption = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        color = MacColors.TextSecondary
    )
} 