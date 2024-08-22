package ui.common.theme.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import kafka.ui.theme.generated.resources.Res
import kafka.ui.theme.generated.resources.inter_black
import kafka.ui.theme.generated.resources.inter_bold
import kafka.ui.theme.generated.resources.inter_light
import kafka.ui.theme.generated.resources.inter_medium
import kafka.ui.theme.generated.resources.inter_regular
import kafka.ui.theme.generated.resources.inter_semibold
import org.jetbrains.compose.resources.Font

val DefaultFont: FontFamily
    @Composable get() = FontFamily(
        Font(Res.font.inter_light, weight = FontWeight.Light),
        Font(Res.font.inter_regular, weight = FontWeight.Normal),
        Font(Res.font.inter_medium, weight = FontWeight.Medium),
        Font(Res.font.inter_semibold, weight = FontWeight.SemiBold),
        Font(Res.font.inter_bold, weight = FontWeight.Bold),
        Font(Res.font.inter_black, weight = FontWeight.Black),
    )
