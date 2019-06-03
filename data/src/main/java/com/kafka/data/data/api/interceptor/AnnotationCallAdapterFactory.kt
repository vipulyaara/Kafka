//package com.airtel.data.data.api.interceptor
//
//import com.airtel.data.data.api.RequestPolicy
//import retrofit2.CallAdapter
//import retrofit2.Retrofit
//import java.lang.reflect.Type
//
//internal class AnnotationCallAdapterFactory(
//    private val callAdapterFactories: List<CallAdapter.Factory>,
//    private val registration: MutableMap<Int, RequestPolicy>
//) : CallAdapter.Factory() {
//
//    override fun get(
//        returnType: Type,
//        annotations: Array<Annotation>,
//        retrofit: Retrofit
//    ): CallAdapter<*, *>? {
//        val annotation = getAnnotation(annotations)
//        for (i in callAdapterFactories.indices) {
//            val adapter = callAdapterFactories[i].get(returnType, annotations, retrofit)
//            if (adapter != null) {
//                if (annotation != null) {
//                    // get whatever info you need from your annotation
//                    return AnnotationCallAdapter(
//                        adapter as CallAdapter<out Any, Any>,
//                        registration,
//                        annotation
//                    )
//                }
//                return adapter
//            }
//        }
//        return null
//    }
//
//    private fun getAnnotation(annotations: Array<Annotation>): RequestPolicy? {
//        for (annotation in annotations) {
//            if (annotation is RequestPolicy) {
//                return annotation
//            }
//        }
//        return null
//    }
//}
