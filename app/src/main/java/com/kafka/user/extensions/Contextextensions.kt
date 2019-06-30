package com.kafka.user.extensions

import android.content.ContextWrapper

/**
 * @author Vipul Kumar; dated 04/04/19.
 */
fun <Service> ContextWrapper.getService(name: String) = getSystemService(name) as Service
