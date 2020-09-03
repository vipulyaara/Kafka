//package com.kafka.ui.profile
//
//import androidx.compose.material.Surface
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.layout.ContentScale
//import com.kafka.ui.R
//import com.kafka.ui.VectorImage
//import com.kafka.ui.colors
//import com.kafka.ui.typography
//
//sealed class MenuItem
//data class TextMenuItem(val icon: Int, val text: String) : MenuItem()
//object MenuSeparator : MenuItem()
//
//val menuItems = arrayListOf(
//    TextMenuItem(R.drawable.ic_heart_sign, "Favorites"),
//    MenuSeparator,
//    TextMenuItem(R.drawable.ic_download, "Downloads"),
//    TextMenuItem(R.drawable.ic_globe, "Languages"),
//    TextMenuItem(R.drawable.ic_feather, "Shayari"),
//    MenuSeparator,
//    TextMenuItem(R.drawable.ic_settings, "Settings"),
//    TextMenuItem(R.drawable.ic_info, "About us"),
//    TextMenuItem(R.drawable.ic_help_circle, "Feedback")
//)
//
//@Composable
//fun ProfileScreen() {
//    Surface(color = colors().background) {
//        Column {
//            MenuItems()
//        }
//    }
//}
//
//@Composable
//fun ProfileHeader() {
//    Row(modifier = Modifier.padding(24.dp)) {
//        Card(elevation = 12.dp, modifier = Modifier.size(96.dp), shape = CircleShape) {
//            CoilImage(data = R.drawable.img_profile, contentScale = ContentScale.Crop)
//        }
//        Column(modifier = Modifier.padding(horizontal = 24.dp).gravity(Alignment.CenterVertically)) {
//            Text(text = "Vipul Kumar", style = typography().subtitle1)
//            Text(text = "24 Favorites", style = typography().body1, modifier = Modifier.padding(vertical = 4.dp))
//        }
//    }
//}
//
//@Composable
//fun MenuItems() {
//    VerticalScroller {
//        Column {
//            ProfileHeader()
//            Spacer(modifier = Modifier.height(12.dp))
//            for (it in menuItems) {
//                when (it) {
//                    is MenuSeparator -> MenuSeparator()
//                    is TextMenuItem -> TextMenuItem(it)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun MenuSeparator() {
//    Column {
//        Card(modifier = Modifier.padding(12.dp), elevation = 0.dp) {
//            Box(backgroundColor = colors().primary, modifier = Modifier.height(2.dp).fillMaxWidth())
//        }
//    }
//}
//
//@Composable
//fun TextMenuItem(it: TextMenuItem) {
//    Clickable(onClick = {}, modifier = Modifier.ripple()) {
//        Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp).fillMaxWidth()) {
//            VectorImage(
//                id = it.icon,
//                modifier = Modifier.gravity(Alignment.CenterVertically),
//                tint = colors().primary.copy(alpha = 0.7f)
//            )
//            Text(
//                text = it.text,
//                style = typography().subtitle2,
//                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp).gravity(Alignment.CenterVertically)
//            )
//        }
//    }
//}
