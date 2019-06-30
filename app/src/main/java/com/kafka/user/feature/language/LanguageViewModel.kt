package com.kafka.user.feature.language

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.kafka.data.data.annotations.UseInjection
import com.kafka.data.data.config.kodeinInstance
import com.kafka.data.data.interactor.launchInteractor
import com.kafka.data.feature.config.UpdateContentLanguage
import com.kafka.data.model.LanguageModel
import com.kafka.user.feature.common.BaseViewModel
import com.kafka.user.ui.RxLoadingCounter
import org.kodein.di.generic.instance

/**
 * @author Vipul Kumar; dated 10/12/18.
 *
 * Implementation of [BaseViewModel] to provide data for content detail.
 */
@UseInjection
internal class LanguageViewModel : BaseViewModel<LanguageViewState>(
    LanguageViewState()
) {
    private val updateLanguage: UpdateContentLanguage by kodeinInstance.instance()
    private val loadingState = RxLoadingCounter()

    init {
        loadingState.observable
            .execute { copy(isLoading = it() ?: false) }

        updateLanguage.observe()
            .toObservable()
            .doOnError(logger::e)
            .execute {
                copy(languages = it())
            }
    }

    private fun updateLanguages(languages: List<LanguageModel>) {
        updateLanguage.setParams(UpdateContentLanguage.Params(languages.joinToString(",") { it.languageId }))
        loadingState.addLoader()
        scope.launchInteractor(updateLanguage, UpdateContentLanguage.ExecuteParams())
            .invokeOnCompletion { loadingState.removeLoader() }
    }

    companion object : MvRxViewModelFactory<LanguageViewModel, LanguageViewState> {
        // This *must* be @JvmStatic for performance reasons.
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: LanguageViewState
        ): LanguageViewModel {
            return LanguageViewModel()
        }
    }
}
