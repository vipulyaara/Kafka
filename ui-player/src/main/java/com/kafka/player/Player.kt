//package com.kafka.player
//
//
//@Composable
//@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
//fun VideoPlayer(uri: Uri) {
//    val context = LocalContext.current
//
//    val exoPlayer = remember {
//        ExoPlayer.Builder(context)
//            .build()
//            .apply {
//                val defaultDataSourceFactory = DefaultDataSource.Factory(context)
//                val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(
//                    context,
//                    defaultDataSourceFactory
//                )
//                val source = ProgressiveMediaSource.Factory(dataSourceFactory)
//                    .createMediaSource(MediaItem.fromUri(uri))
//
//                setMediaSource(source)
//                prepare()
//            }
//    }
//
//    exoPlayer.playWhenReady = true
//    exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
//    exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
//
//    DisposableEffect(
//        AndroidView(factory = {
//            PlayerView(context).apply {
//                hideController()
//                useController = false
//                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
//
//                player = exoPlayer
//                layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
//            }
//        })
//    ) {
//        onDispose { exoPlayer.release() }
//    }
//}
