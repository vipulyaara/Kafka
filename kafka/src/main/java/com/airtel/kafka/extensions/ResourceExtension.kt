package com.airtel.kafka.extensions

import com.airtel.data.model.data.Resource

/**
 * @author Vipul Kumar; dated 13/02/19.
 */
inline fun <Request, Response> Resource<Request>.map(
    func: (Request?) -> Response?
): Resource<Response> {
    return Resource.create(func.invoke(this.data), this.status, this.error)
}
