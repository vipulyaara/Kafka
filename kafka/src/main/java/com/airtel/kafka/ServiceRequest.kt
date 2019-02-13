package com.airtel.kafka

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations.switchMap
import java.util.Objects
import kotlin.random.Random

/**
 * @author Vipul Kumar; dated 02/01/19.
 *
 * Service requests are used to identify a request sent to the SDK.
 * SDK should build an instance of a ServiceRequest for every request made; to keep track of it.
 */
const val serviceRequestIdPrefix = "SR"

data class ServiceRequest<Result>(
    val id: String = "$serviceRequestIdPrefix-${Random.nextInt(100, 9999)}",
    val request: () -> LiveData<Result>
) {

    val result = MediatorLiveData<Result>()
    private val trigger = MutableLiveData<Boolean>()
    private val invocation = switchMap(trigger) {
        if (it) {
            request.invoke()
        } else {
            MutableLiveData()
        }
    }!!

    init {
        result.addSource(invocation) {
            if (!Objects.equals(result.value, it)) {
                result.value = it
            }
        }
    }

    fun execute() {
        trigger.value = true
    }
}

inline fun <T> ServiceRequest<T>.observe(
    owner: LifecycleOwner,
    crossinline observer: (T) -> Unit
) {
    this.result.observe(owner, Observer { it?.let { observer(it) } })
}

