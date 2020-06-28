package com.kafka.search.ui

import androidx.hilt.lifecycle.ViewModelInject
import com.kafka.data.query.languages
import com.kafka.language.domain.UpdateLanguages
import com.kafka.ui.search.HomepageViewState
import com.kafka.ui_common.BaseViewModel

class MainViewModel @ViewModelInject constructor(
    private val updateLanguages: UpdateLanguages
) : BaseViewModel<HomepageViewState>(HomepageViewState()) {

    init {
    }

    fun updateLanguages() {
        updateLanguages(UpdateLanguages.Params(languages))
    }
}

