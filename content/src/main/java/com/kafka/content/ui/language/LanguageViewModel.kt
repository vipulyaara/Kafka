package com.kafka.content.ui.language

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.data.base.launchObserve
import com.kafka.content.domain.language.ObserveSelectedLanguages
import com.kafka.content.domain.language.UpdateLanguages
import com.kafka.content.ui.homepage.HomepageAction
import com.kafka.data.entities.Language
import com.kafka.ui_common.base.ReduxViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class LanguageViewModel @ViewModelInject constructor(
    observeSelectedLanguages: ObserveSelectedLanguages,
    private val updateLanguages: UpdateLanguages
) : ReduxViewModel<LanguageViewState>(LanguageViewState()) {
    private val actioner = Channel<HomepageAction>(Channel.BUFFERED)

    init {
        viewModelScope.launchObserve(observeSelectedLanguages) { flow ->
            flow.distinctUntilChanged().collectAndSetState { copy(languages = it) }
        }

        observeSelectedLanguages(Unit)
    }

    fun submitAction(action: HomepageAction) {
        viewModelScope.launch {
            if (!actioner.isClosedForSend) { actioner.send(action) }
        }
    }

    fun updateLanguages() {
        updateLanguages(UpdateLanguages.Params(languages))
    }
}

val languages = arrayListOf(
    Language("en", "English", "English", true),
    Language("hi", "Hindi", "हिन्दी", true),
    Language("ur", "Urdu", "اردو", true),
    Language("pn", "Punjabi", "ਪੰਜਾਬੀ", false),
    Language("de", "German", "German", false),
    Language("fr", "French", "Français", false)
)
