//package com.kafka.ui.widget
//
//import android.graphics.Color
//import kotlin.random.Random
//
//data class SeekData(val color: Color)
//
//val fakeList = mutableListOf<SeekData>()
//    .also { list ->
//        repeat(96) {
//            list.add(SeekData(Color(0x88AEABAE)))
//        }
//    }
//
//@Composable
//fun FakeSeekBar(duration: Int) {
//    val containerHeight = 72
//    LazyRowItems(
//        modifier = Modifier.fillMaxWidth().height(containerHeight.dp),
//        items = fakeList
//    ) {
//        val height = Random.nextInt(12, containerHeight).dp
//        Stack(modifier = Modifier.height(containerHeight.dp).padding(2.dp)) {
//            Surface(
//                shape = RoundedCornerShape(4.dp),
//                modifier = Modifier.size(3.dp, height).gravity(Alignment.Center),
//                color = it.color,
//                elevation = 0.dp
//            ) {}
//        }
//    }
//}
