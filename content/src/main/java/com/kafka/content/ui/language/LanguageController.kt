package com.kafka.content.ui.language

import com.airbnb.epoxy.TypedEpoxyController
import com.kafka.content.header
import com.kafka.content.languageSelection
import javax.inject.Inject

class LanguageController @Inject constructor() : TypedEpoxyController<LanguageViewState>() {

    override fun buildModels(data: LanguageViewState?) {
        data?.apply {
            header {
                id("header")
                text("Select Languages")
            }
            languages?.forEach {
                languageSelection {
                    id(it.id)
                    language(it)
                }
            }
        }
    }
}
