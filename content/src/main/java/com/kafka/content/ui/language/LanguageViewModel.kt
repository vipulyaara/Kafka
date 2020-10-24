package com.kafka.content.ui.language

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.kafka.data.model.launchObserve
import com.kafka.content.domain.language.ObserveSelectedLanguages
import com.kafka.content.domain.language.UpdateLanguages
import com.kafka.content.ui.homepage.HomepageAction
import com.kafka.data.entities.Language
import com.kafka.ui_common.base.ReduxViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Named

class LanguageViewModel @ViewModelInject constructor(
    observeSelectedLanguages: ObserveSelectedLanguages,
    private val updateLanguages: UpdateLanguages,
    @Named("app") private val sharedPreferences: SharedPreferences
) : ReduxViewModel<LanguageViewState>(LanguageViewState()) {
    private val actioner = Channel<HomepageAction>(Channel.BUFFERED)

    init {
        viewModelScope.launchObserve(observeSelectedLanguages) { flow ->
            flow.distinctUntilChanged().collectAndSetState { copy(languages = it) }
        }

        observeSelectedLanguages(Unit)
    }

    fun areLanguagesSelected() = sharedPreferences.getBoolean("language_selected", false)

    fun onDoneClicked() = sharedPreferences.edit {  putBoolean("language_selected", true) }

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
