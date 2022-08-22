package org.kafka.homepage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.kafka.common.Icons
import org.kafka.ui_common_compose.shadowMaterial
import ui.common.theme.theme.surfaceLight
import ui.common.theme.theme.textPrimary

@Composable
fun TumblrFeed() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceLight)
    ) {
        items(posts, key = { it.id }) {
            Post(it)
        }
    }
}

@Composable
fun Post(post: Post) {
    Card(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .shadowMaterial(12.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Column {
            Header()
            AsyncImage(
                model = post.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(12.dp))
            )
            Actions(actions = actions)
        }
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = R.drawable.img_profile,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = "Vipul Kumar",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.textPrimary
            )
//            Text(
//                text = "we_love_android",
//                style = MaterialTheme.typography.labelMedium,
//                color = MaterialTheme.colorScheme.textSecondary
//            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Image(imageVector = Icons.Follow, contentDescription = null)
    }
}

@Composable
fun Actions(actions: List<Action>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        actions.forEach {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable { }
                    .padding(12.dp)
                    .align(Alignment.CenterVertically),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    imageVector = it.icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
//                Spacer(modifier = Modifier.width(12.dp))
//                Text(text = it.text, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

data class Action(val icon: ImageVector, val text: String)

data class Post(
    val id: Int,
    val title: String = "This is the title",
    val text: String = "This is the text",
    val image: Int
)

val actions: List<Action>
    @Composable get() = listOf(
        Action(Icons.Heart, ""),
        Action(Icons.Comment, ""),
        Action(Icons.Retweet, ""),
        Action(Icons.Share, ""),
    )

val images = listOf(
    R.drawable.img_ill_4,
    R.drawable.img_ill_2,
    R.drawable.img_banner_11,
    R.drawable.img_ill_5,
    R.drawable.img_banner_30,
    R.drawable.img_banner_31,
    R.drawable.img_banner_32,
    R.drawable.img_ill_3,
    R.drawable.img_illustration_5,
    R.drawable.tumblr,
    R.drawable.tumblr_2,
    R.drawable.tumblr_3,
)

val posts = mutableListOf<Post>().apply {
    repeat(20) {
        add(Post(id = it, image = images.getOrElse(it) { images.random() }))
    }
}