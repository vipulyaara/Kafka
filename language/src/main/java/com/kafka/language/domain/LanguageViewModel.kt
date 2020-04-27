package com.kafka.language.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.base.launchObserve
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class LanguageViewModel @Inject constructor(
    private val updateLanguages: UpdateLanguages,
    private val observeSelectedLanguages: ObserveSelectedLanguages
) : ViewModel() {

    init {
        viewModelScope.launchObserve(observeSelectedLanguages) { flow ->
//            flow.distinctUntilChanged().execute {
//
//            }
        }

        observeSelectedLanguages(Unit)
    }
}
