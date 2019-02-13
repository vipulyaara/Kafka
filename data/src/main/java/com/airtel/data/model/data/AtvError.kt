package com.airtel.data.model.data

/**
 * Created by Aditya Mehta on 03/10/18.
 */
class AtvError {
    var respCode: Int? = null
        private set
    var errorCode: String? = null
        private set
    var subErrorCode: String? = null
        internal set
    var errorUserTitle: String? = null
        private set
    var errorUserMessage: String? = null
        private set
}
