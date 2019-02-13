package com.airtel.kafkapp.feature.common

/**
 * @author Vipul Kumar; dated 22/12/18.
 *
 * ViewStates are the data holders that are updated by viewModels and observed by Fragment.
 * They are usually small data classes that are wrapped in observables.
 *
 * ViewStates are observable (a live data) in [BaseViewModel]s.
 * Because they are small data classes, any change to them might look like 'state.copy(...)'.
 * Anytime a change is made to a state, the live data will be notified and fragment can render the data.
 */
open class BaseViewState
