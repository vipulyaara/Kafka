package org.kafka.domain

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState

val CombinedLoadStates.isLoading
    get() = refresh == LoadState.Loading || append == LoadState.Loading
            || mediator?.refresh == LoadState.Loading || mediator?.append == LoadState.Loading

val CombinedLoadStates.isError
    get() = refresh is LoadState.Error || append is LoadState.Error
            || mediator?.refresh is LoadState.Error || mediator?.append is LoadState.Error

val CombinedLoadStates.errorMessage: String
    get() = (refresh as? LoadState.Error)?.error?.localizedMessage
        ?: (append as? LoadState.Error)?.error?.localizedMessage
        ?: (mediator?.append as? LoadState.Error)?.error?.localizedMessage
        ?: (mediator?.refresh as? LoadState.Error)?.error?.localizedMessage
        ?: "Something went gwrong"
