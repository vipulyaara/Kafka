package com.kafka.ui.content

import android.text.Html
import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.SimpleImage
import androidx.ui.foundation.Text
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.layout.*
import androidx.ui.material.*
import androidx.ui.res.imageResource
import androidx.ui.text.style.TextOverflow
import androidx.ui.unit.dp
import com.kafka.data.entities.ItemDetail
import com.kafka.ui.R
import com.kafka.ui.alignCenter
import com.kafka.ui.lineHeight
import com.kafka.ui.widget.ButtonRegular

@Composable
fun ContentDetailItem(itemDetail: ItemDetail?, actioner: (ContentDetailAction) -> Unit) {
    Column {
        Box(
            modifier = Modifier.preferredSize(196.dp, 258.dp) + Modifier.gravity(ColumnAlign.Center),
            shape = RoundedCornerShape(5.dp)
        ) { SimpleImage(image = imageResource(id = R.drawable.img_author_camus_latranger)) }

        Spacer(modifier = Modifier.padding(top = 20.dp))

        Text(
            text = itemDetail?.title ?: "",
            style = MaterialTheme.typography.h2.alignCenter(),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = LayoutGravity.Center
        )
        Spacer(modifier = Modifier.padding(2.dp))
        Text(
            text = "by " + itemDetail?.creator,
            style = MaterialTheme.typography.h6,
            modifier = LayoutGravity.Center
        )
        Spacer(modifier = Modifier.padding(12.dp))

        Clickable(onClick = { actioner(ContentDetailAction.RatingWidgetClick()) }) {
            Card(
                modifier = LayoutGravity.Center,
                shape = RoundedCornerShape(5.dp),
                elevation = 1.dp,
                color = MaterialTheme.colors.surface
            ) {
                RatingWidget()
            }
        }

        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = itemDetail?.description?.let { Html.fromHtml(it)?.toString() } ?: "",
            maxLines = 3,
            style = MaterialTheme.typography.body2.lineHeight(1.3).alignCenter(),
            modifier = Modifier.padding(20.dp)
        )

        Row(modifier = Modifier.padding(20.dp)) {
            ProvideEmphasis(emphasis = EmphasisAmbient.current.disabled) {
                ButtonRegular(
                    modifier = Modifier.weight(0.49f),
                    text = "DOWNLOAD",
                    backgroundColor = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(2.dp),
                    elevation = 16.dp
                )
            }

            Box(modifier = Modifier.weight(0.04f)) {}

            ButtonRegular(
                modifier = Modifier.weight(0.49f),
                text = "LISTEN",
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 24.dp,
                shape = RoundedCornerShape(2.dp),
                contentColor = MaterialTheme.colors.secondary,
                onClick = {}
            )
        }
    }
}
