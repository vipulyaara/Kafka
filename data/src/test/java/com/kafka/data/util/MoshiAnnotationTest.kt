//package com.airtel.data.util
//
//import com.airtel.data.model.SingleToArray
//import com.squareup.moshi.Moshi
//import com.squareup.moshi.Types
//import java.util.Arrays
//
///**
// * @author Vipul Kumar; dated 15/02/19.
// */
//
//@Test @Throws(Exception::class)
//abstract fun singleToArray() {
//    val moshi = Moshi.Builder().add(SingleToArray.Adapter.FACTORY).build()
//    val adapter = moshi.adapter<List<String>>(
//        Types.newParameterizedType(
//            List<Any>::class.java,
//            String::class.java
//        ), SingleToArray::class.java
//    )
//    assertThat(adapter.fromJson("[\"Tom\",\"Huck\"]")).isEqualTo(Arrays.asList("Tom", "Huck"))
//    assertThat(adapter.toJson(Arrays.asList("Tom", "Huck"))).isEqualTo("[\"Tom\",\"Huck\"]")
//    assertThat(adapter.fromJson("\"Jim\"")).isEqualTo(listOf("Jim"))
//    assertThat(adapter.toJson(listOf("Jim"))).isEqualTo("\"Jim\"")
//    assertThat(adapter.fromJson("[]")).isEqualTo(emptyList<Any>())
//    assertThat(adapter.toJson(emptyList())).isEqualTo("[]")
//}
