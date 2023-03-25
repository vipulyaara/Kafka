package ui.common.theme.theme

import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import ui.common.theme.R

@ExperimentalTextApi
private fun createSingleGoogleFontFamily(
    name: String,
    provider: GoogleFont.Provider = GmsFontProvider,
    variants: List<Pair<FontWeight, FontStyle>>,
): FontFamily = FontFamily(
    variants.map { (weight, style) ->
        Font(
            googleFont = GoogleFont(name),
            fontProvider = provider,
            weight = weight,
            style = style,
        )
    },
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
internal fun createFontFamily(id: String) =
    createSingleGoogleFontFamily(
        name = id,
        variants = listOf(
            FontWeight.Light to FontStyle.Normal,
            FontWeight.Normal to FontStyle.Normal,
            FontWeight.Normal to FontStyle.Italic,
            FontWeight.Medium to FontStyle.Normal,
            FontWeight.SemiBold to FontStyle.Normal,
            FontWeight.Bold to FontStyle.Normal,
            FontWeight.Black to FontStyle.Normal,
        )
    )

internal val InterFontFamily: FontFamily by lazy {
    createSingleGoogleFontFamily(
        name = "Inter",
        variants = listOf(
            FontWeight.Light to FontStyle.Normal,
            FontWeight.Normal to FontStyle.Normal,
            FontWeight.Normal to FontStyle.Italic,
            FontWeight.Medium to FontStyle.Normal,
            FontWeight.SemiBold to FontStyle.Normal,
            FontWeight.Bold to FontStyle.Normal,
            FontWeight.Black to FontStyle.Normal,
        ),
    )
}

val Roboto = createFontFamily("Roboto")
val OpenSans = createFontFamily("Open Sans")
val ProseFont = createFontFamily("Lora")
val Poppins = createFontFamily("Poppins")
val Inter = createFontFamily("Inter")
val DefaultFont = InterFontFamily
