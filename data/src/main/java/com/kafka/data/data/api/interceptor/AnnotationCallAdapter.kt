package com.kafka.data.data.api.interceptor

import com.kafka.data.data.api.RequestPolicy
import okhttp3.Request
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

internal class
AnnotationCallAdapter<ReturnType>(
    private val adapter: CallAdapter<ReturnType, Any>,
    private val registration: MutableMap<Int, RequestPolicy>,
    private val info: RequestPolicy
) : CallAdapter<ReturnType, Any> {

    override fun responseType(): Type {
        return adapter.responseType()
    }

    override fun adapt(call: Call<ReturnType>): Any {
        val request = call.request()
        registration[identify(request)] = info
        return adapter.adapt(call)
    }

    private fun identify(request: Request): Int {
        // this is very experimental but it does the job currently
        return (request.url().toString() + request.method()).hashCode()
    }
}
