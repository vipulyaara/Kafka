package com.kafka.data.data.api

import com.kafka.data.data.mapper.Mapper
import com.kafka.data.extensions.bodyOrThrow
import com.kafka.data.extensions.toException
import com.kafka.data.model.data.ErrorResult
import com.kafka.data.model.data.Result
import com.kafka.data.model.data.Success
import retrofit2.Response

class RetrofitRunner {
    suspend fun <T, E> executeForResponse(
        mapper: Mapper<T, E>,
        request: suspend () -> Response<T>
    ): Result<E> {
        return try {
            val response = request()
            if (response.isSuccessful) {
                val okHttpNetworkResponse = response.raw().networkResponse()
                val notModified =
                    okHttpNetworkResponse == null || okHttpNetworkResponse.code() == 304
                Success(
                    data = mapper.map(response.bodyOrThrow()),
                    responseModified = !notModified
                )
            } else {
                ErrorResult(response.toException())
            }
        } catch (e: Exception) {
            ErrorResult(e)
        }
    }

    suspend fun <T> executeForResponse(request: suspend () -> Response<T>): Result<T> {
        val unitMapper = object : Mapper<T, T> {
            override fun map(from: T) = from
        }
        return executeForResponse(unitMapper, request)
    }
}
