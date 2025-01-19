package ui.common.theme.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import kafka.ui.theme.generated.resources.Res
import kafka.ui.theme.generated.resources.geist_black
import kafka.ui.theme.generated.resources.geist_bold
import kafka.ui.theme.generated.resources.geist_light
import kafka.ui.theme.generated.resources.geist_medium
import kafka.ui.theme.generated.resources.geist_regular
import kafka.ui.theme.generated.resources.geist_semibold
import kafka.ui.theme.generated.resources.inter_black
import kafka.ui.theme.generated.resources.inter_bold
import kafka.ui.theme.generated.resources.inter_light
import kafka.ui.theme.generated.resources.inter_medium
import kafka.ui.theme.generated.resources.inter_regular
import kafka.ui.theme.generated.resources.inter_semibold
import kafka.ui.theme.generated.resources.laila_bold
import kafka.ui.theme.generated.resources.laila_medium
import kafka.ui.theme.generated.resources.laila_semibold
import kafka.ui.theme.generated.resources.nirmala_regular
import kafka.ui.theme.generated.resources.sohne_black
import kafka.ui.theme.generated.resources.sohne_bold
import kafka.ui.theme.generated.resources.sohne_light
import kafka.ui.theme.generated.resources.sohne_medium
import kafka.ui.theme.generated.resources.sohne_regular
import kafka.ui.theme.generated.resources.sohne_semibold
import org.jetbrains.compose.resources.Font

val Inter: FontFamily
    @Composable get() = FontFamily(
        Font(Res.font.inter_light, weight = FontWeight.Light),
        Font(Res.font.inter_regular, weight = FontWeight.Normal),
        Font(Res.font.inter_medium, weight = FontWeight.Medium),
        Font(Res.font.inter_semibold, weight = FontWeight.SemiBold),
        Font(Res.font.inter_bold, weight = FontWeight.Bold),
        Font(Res.font.inter_black, weight = FontWeight.Black),
    )

val Laila: FontFamily
    @Composable get() = FontFamily(
        Font(Res.font.nirmala_regular, weight = FontWeight.Light),
        Font(Res.font.nirmala_regular, weight = FontWeight.Normal),
        Font(Res.font.laila_medium, weight = FontWeight.Medium),
        Font(Res.font.laila_semibold, weight = FontWeight.SemiBold),
        Font(Res.font.laila_bold, weight = FontWeight.Bold),
        Font(Res.font.laila_bold, weight = FontWeight.Black),
    )

val Sohne: FontFamily
    @Composable get() = FontFamily(
        Font(Res.font.sohne_light, weight = FontWeight.Light),
        Font(Res.font.sohne_regular, weight = FontWeight.Normal),
        Font(Res.font.sohne_medium, weight = FontWeight.Medium),
        Font(Res.font.sohne_semibold, weight = FontWeight.SemiBold),
        Font(Res.font.sohne_bold, weight = FontWeight.Bold),
        Font(Res.font.sohne_black, weight = FontWeight.Black),
    )

val Geist: FontFamily
    @Composable get() = FontFamily(
        Font(Res.font.geist_light, weight = FontWeight.Light),
        Font(Res.font.geist_regular, weight = FontWeight.Normal),
        Font(Res.font.geist_medium, weight = FontWeight.Medium),
        Font(Res.font.geist_semibold, weight = FontWeight.SemiBold),
        Font(Res.font.geist_bold, weight = FontWeight.Bold),
        Font(Res.font.geist_black, weight = FontWeight.Black),
    )

val DefaultFont
    @Composable get() = Geist
