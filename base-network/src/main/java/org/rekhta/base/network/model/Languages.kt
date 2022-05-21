package org.rekhta.base.network.model

import kotlinx.serialization.Serializable

@Serializable
enum class Language(val code: Int, val locale: String) {
    English(1, "en"),
    Hindi(2, "hi"),
    Urdu(3, "ur");

    companion object {
        val default = English

        fun from(languageCode: Int) = when (languageCode) {
            1 -> English
            2 -> Hindi
            3 -> Urdu
            else -> throw RuntimeException("Invalid Language")
        }

        fun languageFallbackOrder(currentLanguage: Language) = mutableListOf<Language>().apply {
            add(currentLanguage)
            addAll(values().filterNot { it == currentLanguage })
        }
    }
}
