//package com.kafka.content.compose.player
//
//import androidx.compose.animation.animate
//import androidx.compose.foundation.*
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.lazy.ExperimentalLazyDsl
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.rounded.Create
//import androidx.compose.material.icons.rounded.Email
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.WithConstraints
//import androidx.compose.ui.drawLayer
//import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.DensityAmbient
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.viewinterop.viewModel
//import com.kafka.content.ui.player.PlayerViewModel
//import com.kafka.data.entities.Song
//import com.kafka.ui_common.navigation.backHandler
//import com.kafka.ui_common.theme.AmbientInsets
//import com.kafka.ui_common.theme.KafkaTheme
//import com.kafka.ui_common.theme.statusBarsPadding
//import com.kafka.ui_common.theme.toPaddingValues
//
//private enum class SheetState { Open, Closed }
//private val FabSize = 124.dp
//
//@ExperimentalLazyDsl
//@OptIn(ExperimentalMaterialApi::class)
//@Composable
//fun PlayerScaffold(content: @Composable () -> Unit) {
//    WithConstraints {
//        val sheetState = rememberSwipeableState(SheetState.Closed)
//        val fabSize = with(DensityAmbient.current) { FabSize.toPx() }
//        val dragRange = constraints.maxHeight - fabSize
//
//        backHandler(
//            enabled = sheetState.value == SheetState.Open,
//            onBack = { sheetState.animateTo(SheetState.Closed) }
//        )
//
//        Box(
//            // The Lessons sheet is initially closed and appears as a FAB. Make it openable by
//            // swiping or clicking the FAB.
//            Modifier.swipeable(
//                state = sheetState,
//                anchors = mapOf(
//                    0f to SheetState.Closed,
//                    -dragRange to SheetState.Open
//                ),
//                thresholds = { _, _ -> FractionalThreshold(0.5f) },
//                orientation = Orientation.Vertical
//            ).background(KafkaTheme.colors.secondary)
//        ) {
//            val openFraction = if (sheetState.offset.value.isNaN()) {
//                0f
//            } else {
//                -sheetState.offset.value / dragRange
//            }.coerceIn(0f, 1f)
//            content()
//            PlayerBottomSheet(
//                openFraction,
//                constraints.maxWidth.toFloat(),
//                constraints.maxHeight.toFloat()
//            ) { state ->
//                sheetState.animateTo(state)
//            }
//        }
//    }
//}
//
//@Composable
//private fun PlayerBottomSheet(
//    openFraction: Float,
//    width: Float,
//    height: Float,
//    updateSheet: (SheetState) -> Unit
//) {
//
//    val viewModel: PlayerViewModel = viewModel()
//    val viewState by viewModel.state.collectAsState()
//
//    // Use the fraction that the sheet is open to drive the transformation from FAB -> Sheet
//    val fabSize = with(DensityAmbient.current) { FabSize.toPx() }
//    val fabSheetHeight = fabSize + AmbientInsets.current.systemBars.bottom
//    val offsetX = lerp(width - fabSize, 0f, 0f, 0.15f, openFraction)
//    val offsetY = lerp(height - fabSheetHeight, 0f, openFraction)
//    val tlCorner = lerp(fabSize, 0f, 0f, 0.15f, openFraction)
//    val surfaceColor = lerp(
//        startColor = KafkaTheme.colors.secondary,
//        endColor = MaterialTheme.colors.primarySurface.copy(alpha = 0.5f),
//        startFraction = 0f,
//        endFraction = 0.3f,
//        fraction = openFraction
//    )
//    Surface(
//        color = surfaceColor,
//        contentColor = contentColorFor(color = MaterialTheme.colors.primarySurface),
//        shape = RoundedCornerShape(topLeft = tlCorner),
//        modifier = Modifier.drawLayer(
//            translationX = offsetX,
//            translationY = offsetY
//        )
//    ) {
//        viewState.queueSongs?.let { QueueFiles(it, openFraction, KafkaTheme.colors.secondary, updateSheet) }
//    }
//}
//
//@Composable
//private fun QueueFiles(
//    queue: List<Song>,
//    openFraction: Float,
//    surfaceColor: Color = MaterialTheme.colors.surface,
//    updateSheet: (SheetState) -> Unit
//) {
//
//    Box(modifier = Modifier.fillMaxWidth()) {
//        // When sheet open, show a list of the lessons
//        val lessonsAlpha = lerp(0f, 1f, 0.2f, 0.8f, openFraction)
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .drawLayer(alpha = lessonsAlpha)
//                .statusBarsPadding()
//        ) {
//            val scroll = rememberScrollState()
//            val appBarElevation = animate(if (scroll.value > 0f) 4.dp else 0.dp)
//            val appBarColor = if (appBarElevation > 0.dp) surfaceColor else Color.Transparent
//            TopAppBar(
//                backgroundColor = appBarColor,
//                elevation = appBarElevation
//            ) {
//                Text(
//                    text = "Name",
//                    style = MaterialTheme.typography.subtitle1,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis,
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .weight(1f)
//                        .align(Alignment.CenterVertically)
//                )
//                IconButton(
//                    onClick = { updateSheet(SheetState.Closed) },
//                    modifier = Modifier.align(Alignment.CenterVertically)
//                ) {
//                    Icon(asset = Icons.Rounded.Create)
//                }
//            }
//            ScrollableColumn(
//                scrollState = scroll,
//                contentPadding = AmbientInsets.current.systemBars.toPaddingValues(
//                    top = false
//                )
//            ) {
//                queue.forEach { song ->
//                    QueueSongItem(song)
//                    Divider(startIndent = 128.dp)
//                }
//            }
//        }
//
//        // When sheet closed, show the FAB
//        val fabAlpha = lerp(1f, 0f, 0f, 0.15f, openFraction)
//        Box(
//            modifier = Modifier
//                .preferredSize(FabSize)
//                .padding(start = 16.dp, top = 8.dp) // visually center contents
//                .drawLayer(alpha = fabAlpha)
//        ) {
//            IconButton(
//                modifier = Modifier.align(Alignment.Center),
//                onClick = { updateSheet(SheetState.Open) }
//            ) {
//                Icon(
//                    asset = Icons.Rounded.Email,
//                    tint = MaterialTheme.colors.onPrimary
//                )
//            }
//        }
//    }
//}
