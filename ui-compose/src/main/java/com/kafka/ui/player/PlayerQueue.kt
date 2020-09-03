//package com.kafka.ui.player
//
//import androidx.compose.Composable
//import androidx.ui.core.Alignment
//import androidx.ui.core.Modifier
//import androidx.ui.foundation.Clickable
//import androidx.ui.foundation.Text
//import androidx.ui.layout.*
//import androidx.ui.material.Surface
//import androidx.ui.unit.dp
//import com.kafka.data.entities.File
//import com.kafka.data.entities.filterMp3
//import com.kafka.data.entities.formattedDuration
//import com.kafka.ui.*
//import com.kafka.ui.R
//import com.kafka.content.ui.PlayerCommand
//
//@Composable
//fun PlayerQueue(files: List<File>, actioner: (com.kafka.content.ui.PlayerCommand) -> Unit) {
////    VerticalScroller {
//    Column {
//        files.filterMp3().sortedBy { it.title }.forEach {
//            File(it = it, actioner = actioner)
//        }
////        }
//    }
//}
//
//@Composable
//fun File(it: File?, actioner: (com.kafka.content.ui.PlayerCommand) -> Unit) {
//    val title = it?.title ?: ""
//    val subtitle = it?.formattedDuration() ?: ""
//    Clickable(onClick = { actioner(com.kafka.content.ui.PlayerCommand.Play(it?.playbackUrl ?: "")) }) {
//        Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp).fillMaxWidth()) {
//            Row {
//                Surface(modifier = Modifier.gravity(Alignment.CenterVertically), color = colors().background) {
//                    VectorImage(id = R.drawable.ic_headphones, tint = colors().secondary)
//                }
//                Spacer(modifier = Modifier.preferredWidth(16.dp))
//                Column(modifier = Modifier.gravity(Alignment.CenterVertically)) {
//                    Text(text = title, style = typography().subtitle2.lineHeight(1.3), color = colors().onPrimary)
//                    Text(
//                        text = subtitle,
//                        style = typography().caption.lineHeight(1.3),
//                        color = colors().secondary
//                    )
//                }
//            }
//        }
//    }
//}
