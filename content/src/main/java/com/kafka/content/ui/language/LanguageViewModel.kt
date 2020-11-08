package com.kafka.content.ui.language

import androidx.datastore.DataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.kafka.content.domain.language.ObserveSelectedLanguages
import com.kafka.content.domain.language.UpdateLanguages
import com.kafka.content.ui.homepage.HomepageAction
import com.kafka.data.entities.Language
import com.kafka.data.model.launchObserve
import com.kafka.ui_common.base.ReduxViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Named

class LanguageViewModel @ViewModelInject constructor(
    observeSelectedLanguages: ObserveSelectedLanguages,
    private val updateLanguages: UpdateLanguages,
    @Named("app") private val dataStore: DataStore<androidx.datastore.preferences.Preferences>
) : ReduxViewModel<LanguageViewState>(LanguageViewState()) {
    private val actioner = Channel<HomepageAction>(Channel.BUFFERED)
    private val languageSelectedKey = preferencesKey<Boolean>("language_selected")

    init {
        viewModelScope.launchObserve(observeSelectedLanguages) { flow ->
            flow.distinctUntilChanged().collectAndSetState { copy(languages = it) }
        }

        observeSelectedLanguages(Unit)
    }

    fun areLanguagesSelected() = dataStore.data.map { it[languageSelectedKey] ?: false }

    suspend fun onDoneClicked() = dataStore.edit { it[languageSelectedKey] = true }

    fun submitAction(action: HomepageAction) {
        viewModelScope.launch {
            if (!actioner.isClosedForSend) {
                actioner.send(action)
            }
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
