package com.kafka.data.model.data

data class Resource<out T>(val status: Status, val data: T?, val error: AtvError?) {

    //TODO check this method logic
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }

        val resource = other as Resource<*>?

        return if (status != resource?.status) {
            false
        } else (if (error?.errorUserMessage != null)
            error.errorUserMessage == resource.error?.errorUserMessage
        else resource.error?.errorUserMessage == null)
                && if (data != null) data == resource.data else resource.data == null
    }

    override fun hashCode(): Int {
        var result = status.hashCode()
        result = 31 * result + (data?.hashCode() ?: 0)
        result = 31 * result + (error?.errorUserMessage?.hashCode() ?: 0)
        return result
    }

    companion object {

        fun <T> create(data: T?, status: Status, error: AtvError?): Resource<T> {
            return Resource(status, data, error)
        }

        fun <T> success(data: T?): Resource<T> {
            return Resource(
                Status.SUCCESS,
                data,
                null
            )
        }

        fun <T> error(error: AtvError?, data: T?): Resource<T> {
            return Resource(
                Status.ERROR,
                data,
                error
            )
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(
                Status.LOADING,
                data,
                null
            )
        }
    }
}
