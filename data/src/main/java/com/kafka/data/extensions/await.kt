//package com.kafka.data.extensions
//
//import com.google.android.gms.tasks.Task
//import kotlinx.coroutines.CancellationException
//import kotlinx.coroutines.suspendCancellableCoroutine
//import kotlin.coroutines.resumeWithException
//
//suspend fun <T> Task<T>.await(onError: (Exception) -> Unit = { throw it }): T? {
//    if (isComplete) {
//        val e = exception
//        return if (e == null) {
//            if (isCanceled) {
//                throw CancellationException(
//                    "Task $this was cancelled normally."
//                )
//            } else {
//                result
//            }
//        } else {
//            onError(e)
//            result
//        }
//    }
//
//    return suspendCancellableCoroutine { cont ->
//        addOnCompleteListener {
//            val e = exception
//            if (e == null) {
//                if (isCanceled) cont.cancel() else cont.resume(result) {}
//            } else {
//                cont.resumeWithException(e)
//            }
//        }
//    }
//}
