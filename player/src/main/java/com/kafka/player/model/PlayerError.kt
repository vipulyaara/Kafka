package com.kafka.player.model

import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.source.BehindLiveWindowException
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker
import com.google.android.exoplayer2.upstream.HttpDataSource
import java.lang.Exception
import java.io.PrintWriter
import java.io.StringWriter
import java.net.UnknownHostException


class PlayerError {
    private val TAG = PlayerError::class.java.simpleName
    var isRecoverable: Boolean = false
    var currentState: PlayerState
    var errorMsg: String? = null
        get() {
            return if (field == null) {
                "No message found"
            } else {
                field
            }
        }
    var errorCode: Int = ERROR_CODE_UNHANDLED
    var errorReason: ErrorReason?
    var stackTrace: String? = null

    companion object {
        const val playerErrorTitle = "Playback Error "
        const val playerErrorReason = "An error occurred. Please try again later."
        const val ERROR_CODE_UNHANDLED = 1001
        const val ERROR_CODE_BEHIND_LIVE_WINDOW = 1002
        const val ERROR_CODE_PLAYLIST_RESET = 1003
        const val ERROR_CODE_INVALID_CONTENT_TYPE = 1004
        const val ERROR_CODE_PLAYER_RENDERER = 1005
        const val ERROR_CODE_PLAYLIST_STUCK = 1006
        const val ERROR_CODE_UNKNOWN_HOST = 1007
    }

    constructor(error: ExoPlaybackException, currentState: PlayerState) {
        this.currentState = currentState
        this.errorReason = ErrorReason.PLAYER_INTERNAL_ERROR
        buildError(error)
//        if (error.cause is HttpDataSource.InvalidResponseCodeException) {
//            errorCode = (error.cause as HttpDataSource.InvalidResponseCodeException).responseCode
//            errorMsg = (error.cause as HttpDataSource.InvalidResponseCodeException).message ?: "unknown"
//        } else {
//            errorMsg = error.cause?.message ?: error.message ?: error.cause.toString() ?: ""
//            errorCode = 1001
//        }
//        isRecoverable = error.cause is BehindLiveWindowException
    }

    private fun buildError(exception: ExoPlaybackException) {
        isRecoverable = false
        when (exception.type) {
            ExoPlaybackException.TYPE_SOURCE -> {
                errorMsg = exception.sourceException.cause?.message ?: exception.message ?: exception.localizedMessage ?: exception.sourceException.toString() ?: "No message found"
                var cause: Throwable? = exception.sourceException
                while (cause != null) {
                    if (cause is HttpDataSource.InvalidResponseCodeException) {
                        this.errorCode = cause.responseCode
                        break
                    } else if (cause is HlsPlaylistTracker.PlaylistResetException) {
                        isRecoverable = true
                        errorCode =
                            ERROR_CODE_PLAYLIST_RESET
                        break
                    } else if (cause is HlsPlaylistTracker.PlaylistStuckException) {
                        isRecoverable = true
                        errorCode =
                            ERROR_CODE_PLAYLIST_STUCK
                        break
                    } else if (cause is BehindLiveWindowException) {
                        isRecoverable = true
                        errorCode =
                            ERROR_CODE_BEHIND_LIVE_WINDOW
                        break
                    } else if (cause is HttpDataSource.InvalidContentTypeException) {
                        errorCode =
                            ERROR_CODE_INVALID_CONTENT_TYPE
                        break
                    } else if (cause is UnknownHostException){
                        errorCode =
                            ERROR_CODE_UNKNOWN_HOST
                    }
                    cause = cause.cause
                }
            }
            ExoPlaybackException.TYPE_RENDERER -> {
                errorMsg = exception.rendererException.cause?.message ?: exception.message ?: exception.localizedMessage ?: "No message found"
                errorCode = ERROR_CODE_PLAYER_RENDERER
                stackTrace = getStackTrace(exception)
            }
            ExoPlaybackException.TYPE_UNEXPECTED -> {
                errorMsg = exception.unexpectedException.cause?.message ?: exception.message ?: exception.localizedMessage ?: "No message found"

                errorCode = ERROR_CODE_UNHANDLED
                stackTrace = getStackTrace(exception)
            }
        }
    }

    fun getStackTrace(e:Exception):String{
        var sStackTrace = ""
        try {
            val sw = StringWriter()
            val pw = PrintWriter(sw, true)
            e.printStackTrace(pw)
            sStackTrace = sw.toString()
        }catch (e:Exception){

        }
        return sStackTrace
    }

    override fun toString(): String {
        return "PlayerError (errorCode: $errorCode, errorMsg: $errorMsg, errorReason: $errorReason, isRecoverable: $isRecoverable)"
    }


//    private fun isInvalidResponse500(e: ExoPlaybackException): Boolean {
//        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
//            return false
//        }
//        setErrorMsgForSourceType(e)
//        var cause: Throwable? = e.sourceException
//        while (cause != null) {
//            if (cause is HttpDataSource.InvalidResponseCodeException) {
//                if (cause.responseCode == 500) {
//                    this.errorCode = 500
//                    return true
//                }
//            }
//            cause = cause.cause
//        }
//        return false
//    }
//
//
//    private fun isInvalidResponse403(e: ExoPlaybackException): Boolean {
//        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
//            return false
//        }
//        setErrorMsgForSourceType(e)
//        var cause: Throwable? = e.sourceException
//        while (cause != null) {
//            if (cause is HttpDataSource.InvalidResponseCodeException) {
//                if (cause.responseCode == 403) {
//                    this.errorCode = 403
//                    return true
//                }
//            }
//            cause = cause.cause
//        }
//        return false
//    }
//
//    private fun isInvalidResponse404(e: ExoPlaybackException): Boolean {
//        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
//            return false
//        }
//        setErrorMsgForSourceType(e)
//        var cause: Throwable? = e.sourceException
//        while (cause != null) {
//            if (cause is HttpDataSource.InvalidResponseCodeException) {
//                if (cause.responseCode == 404) {
//                    this.errorCode = 404
//                    isRecoverable = false
//                    return true
//                }
//            }
//            cause = cause.cause
//        }
//        return false
//    }
//
//    private fun isInvalidResponse400(e: ExoPlaybackException): Boolean {
//        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
//            return false
//        }
//        setErrorMsgForSourceType(e)
//        var cause: Throwable? = e.sourceException
//        while (cause != null) {
//            if (cause is HttpDataSource.InvalidResponseCodeException) {
//                if (cause.responseCode == 400) {
//                    this.errorCode = 400
//                    isRecoverable = false
//                    return true
//                } else {
//                    errorCode = 1001
//                    isRecoverable = false
//                }
//            }
//            cause = cause.cause
//        }
//        return false
//    }
//
//    /* changed the error message trt source message first if no message found then try general message */
//    private fun setErrorMsgForSourceType(e: ExoPlaybackException) {
//        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
//            return
//        }
//        if (e.sourceException.cause != null) {
//            errorMsg = e.sourceException.cause?.message ?: ""
//        }
//        if (TextUtils.isEmpty(errorMsg)) {
//            setGeneralErrorMessage(e)
//        }
//    }
//
//    private fun setGeneralErrorMessage(e: ExoPlaybackException) {
//        if (TextUtils.isEmpty(errorMsg)) {
//            if (!TextUtils.isEmpty(e.message)) {
//                this.errorMsg = e.message ?: ""
//            } else if (!TextUtils.isEmpty(e.localizedMessage)) {
//                this.errorMsg = e.localizedMessage
//            } else {
//                this.errorMsg = PlayerConstants.NO_MESSAGE_FOUND_ERROR_PLAYBACK
//            }
//        }
//    }
//
//    private fun isBehindLiveWindow(e: ExoPlaybackException): Boolean {
//        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
//            return false
//        }
//        setErrorMsgForSourceType(e)
//        this.errorReason = ErrorReason.PLAYER_INTERNAL_ERROR
//        var cause: Throwable? = e.sourceException
//        if (cause is HttpDataSource.InvalidResponseCodeException) {
//            errorCode = cause.responseCode
//            errorMsg = cause.message ?: "unknown"
//        } else {
//            errorMsg = cause?.message ?: cause?.message ?: cause.toString() ?: ""
//            errorCode = 1001
//        }
//        while (cause != null) {
//            if (cause is BehindLiveWindowException) {
//                isRecoverable = true
//                return true
//            }
//            cause = cause.cause
//        }
//        return false
//    }
//
//    private fun isPlayListResetError(e: ExoPlaybackException): Boolean {
//        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
//            return false
//        }
//        setErrorMsgForSourceType(e)
//        this.errorReason = ErrorReason.PLAYER_INTERNAL_ERROR
//        var cause: Throwable? = e.sourceException
//        if (cause is HttpDataSource.InvalidResponseCodeException) {
//            errorCode = cause.responseCode
//            errorMsg = cause.message ?: "unknown"
//        } else {
//            errorMsg = cause?.message ?: cause?.message ?: cause.toString() ?: ""
//            errorCode = 1001
//        }
//        while (cause != null) {
//            if (cause is BehindLiveWindowException) {
//                isRecoverable = true
//                return true
//            }
//            cause = cause.cause
//        }
//        return false
//    }
//
//
//    private fun sendGeneralPlayBackException(e: ExoPlaybackException) {
//        try {
//            setInvalidResponse(e)
//        } catch (e1: Exception) {
//            Logger.i(javaClass.simpleName + " send General ExoPlaybackException in  ExoPlayer " + e.message)
//        }
//    }
//
//
//    private fun setInvalidResponse(e: ExoPlaybackException): Int {
//        when {
//            e.type == ExoPlaybackException.TYPE_SOURCE -> {
//                var cause: Throwable? = e.sourceException
//                setErrorMsgForSourceType(e)
//                while (cause != null) {
//                    if (cause is HttpDataSource.InvalidContentTypeException) {
//                    } else if (cause is HttpDataSource.InvalidResponseCodeException) {
//                        errorCode = cause.responseCode
//                        isRecoverable = false
//                        return cause.responseCode
//                    }
//                    cause = cause.cause
//                }
//            }
//            e.type == ExoPlaybackException.TYPE_RENDERER -> {
//                errorCode = PlayerConstants.EXO_PLAYER_RENDERER_ERROR_CODE
//                isRecoverable = false
//                if (e.rendererException != null && e.rendererException.cause != null) {
//                    errorMsg = e.rendererException.cause?.message ?: ""
//                }
//                if (TextUtils.isEmpty(errorMsg)) {
//                    setGeneralErrorMessage(e)
//                }
//                return PlayerConstants.EXO_PLAYER_RENDERER_ERROR_CODE
//            }
//            e.type == ExoPlaybackException.TYPE_UNEXPECTED -> {
//                errorCode = PlayerConstants.EXO_PLAYER_UNHANDLED_ERROR_CODE
//                isRecoverable = false
//                if (e.unexpectedException != null && e.unexpectedException.cause != null) {
//                    errorMsg = e.unexpectedException.cause?.message ?: ""
//                }
//                if (TextUtils.isEmpty(errorMsg)) {
//                    setGeneralErrorMessage(e)
//                }
//                return PlayerConstants.EXO_PLAYER_UNHANDLED_ERROR_CODE
//            }
//        }
//        errorCode = PlayerConstants.EXO_PLAYER_UNHANDLED_ERROR_CODE
//        setGeneralErrorMessage(e)
//        isRecoverable = false
//        return PlayerConstants.EXO_PLAYER_UNHANDLED_ERROR_CODE
//    }
//
//    private fun getAppropriateErrorExoPlayerTerm(errorReason: ExoPlaybackException) {
//        when {
//            isBehindLiveWindow(errorReason) -> Logger.i("isBehindLiveWindow")
//            isPlayListResetError(errorReason) -> Logger.i("isBehindLiveWindow")
//            isInvalidResponse400(errorReason) -> Logger.i("isInvalidResponse400")
//            isInvalidResponse403(errorReason) -> Logger.i("isInvalidResponse403")
//            isInvalidResponse404(errorReason) -> Logger.i("isInvalidResponse404")
//            isInvalidResponse500(errorReason) -> Logger.i("isInvalidResponse500")
//            else -> sendGeneralPlayBackException(errorReason)
//        }
//
//    }
}
