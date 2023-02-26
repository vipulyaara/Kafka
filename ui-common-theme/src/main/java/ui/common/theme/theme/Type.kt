package ui.common.theme.theme

import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import ui.common.theme.R

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

val ProductSans = FontFamily(
    Font(R.font.product_sans_light, FontWeight.Light),
    Font(R.font.product_sans_regular, FontWeight.Normal),
    Font(R.font.product_sans_medium, FontWeight.SemiBold),
    Font(R.font.product_sans_medium, FontWeight.Medium),
    Font(R.font.product_sans_bold, FontWeight.Bold)
)

val Roboto = createFontFamily("Roboto")
val OpenSans = createFontFamily("Open Sans")
val ProseFont = createFontFamily("Lora")
val Poppins = createFontFamily("Poppins")
val DefaultFont = Roboto
