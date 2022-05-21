package ui.common.theme.theme.type

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import ui.common.theme.R

val Typography.body1
    get() = this.bodyMedium
val Typography.body2
    get() = this.bodySmall

@ExperimentalTextApi
internal fun createSingleGoogleFontFamily(
    name: String,
    provider: GoogleFont.Provider = GmsFontProvider,
    weights: List<FontWeight>,
): FontFamily = FontFamily(
    weights.map { weight ->
        Font(
            googleFont = GoogleFont(name),
            fontProvider = provider,
            weight = weight,
        )
    }
)

@ExperimentalTextApi
internal val GmsFontProvider: GoogleFont.Provider by lazy {
    GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs,
    )
}

@OptIn(ExperimentalTextApi::class)
internal fun createFontFamily(id: String, additionalWeights: List<FontWeight> = emptyList()) =
    createSingleGoogleFontFamily(
        name = id,
        weights = listOf(
            FontWeight.Light,
            FontWeight.Normal,
            FontWeight.Medium,
            FontWeight.SemiBold,
            FontWeight.Bold,
        ) + additionalWeights
    )

val Inter = createFontFamily("Inter")
val Lora = createFontFamily("Lora")

val MerriweatherSans = createFontFamily("Merriweather Sans")
val Laila = createFontFamily("Laila")
val NotoDevnagri = createFontFamily("Noto Sans Devanagari")
val NotoNastaliq = createFontFamily("Noto Nastaliq Urdu")
val Lato = createFontFamily("Lato")
val Rozha = createFontFamily("Rozha One")

val MerriweatherLight = FontFamily(
    Font(R.font.merriweather_extended_light_italic_eng, FontWeight.Light),
    Font(R.font.merriweather_extended_light_italic_eng, FontWeight.Normal),
    Font(R.font.merriweather_bold_eng, FontWeight.SemiBold),
    Font(R.font.merriweather_heavy_eng, FontWeight.Medium),
    Font(R.font.merriweather_heavy_eng, FontWeight.Bold)
)

val MehrNastaliq = FontFamily(
    Font(R.font.mehr_nastaliq_regular, FontWeight.Light),
    Font(R.font.mehr_nastaliq_regular, FontWeight.Normal),
    Font(R.font.mehr_nastaliq_regular, FontWeight.SemiBold),
    Font(R.font.mehr_nastaliq_regular, FontWeight.Medium),
    Font(R.font.mehr_nastaliq_regular, FontWeight.Bold)
)
