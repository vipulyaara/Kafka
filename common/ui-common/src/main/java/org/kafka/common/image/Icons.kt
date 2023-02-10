package org.kafka.common.image

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import compose.icons.FeatherIcons
import compose.icons.TablerIcons
import compose.icons.feathericons.File
import compose.icons.feathericons.Search
import compose.icons.feathericons.User
import compose.icons.tablericons.ArrowLeft
import compose.icons.tablericons.ArrowRight
import compose.icons.tablericons.Book
import compose.icons.tablericons.Bookmark
import compose.icons.tablericons.ChevronUpLeft
import compose.icons.tablericons.CircleCheck
import compose.icons.tablericons.CircleX
import compose.icons.tablericons.Clock
import compose.icons.tablericons.Download
import compose.icons.tablericons.Heart
import compose.icons.tablericons.Home
import compose.icons.tablericons.Home2
import compose.icons.tablericons.LayoutGrid
import compose.icons.tablericons.LayoutList
import compose.icons.tablericons.Microphone
import compose.icons.tablericons.PlayerPause
import compose.icons.tablericons.PlayerPlay
import compose.icons.tablericons.Search
import compose.icons.tablericons.Share
import compose.icons.tablericons.World
import compose.icons.tablericons.X

object Icons {
    val Home: ImageVector
        get() = TablerIcons.Home
    val HomeActive: ImageVector
        get() = TablerIcons.Home2
    val Search: ImageVector
        get() = TablerIcons.Search
    val Check: ImageVector
        get() = TablerIcons.CircleCheck
    val SearchActive: ImageVector
        get() = FeatherIcons.Search
    val Library: ImageVector
        get() = TablerIcons.Bookmark
    val LibraryActive: ImageVector
        get() = TablerIcons.Book
    val Profile: ImageVector
        get() = FeatherIcons.User
    val Files: ImageVector
        get() = FeatherIcons.File
    val ProfileActive: ImageVector
        get() = FeatherIcons.User
    val GoToTop: ImageVector
        get() = TablerIcons.ChevronUpLeft
    val List: ImageVector
        get() = TablerIcons.LayoutList
    val Grid: ImageVector
        get() = TablerIcons.LayoutGrid
    val Web: ImageVector
        get() = TablerIcons.World
    val Share: ImageVector
        get() = TablerIcons.Share
    val Heart
        @Composable get() = TablerIcons.Heart
    val Download
        @Composable get() = TablerIcons.Download
    val Downloaded
        @Composable get() = TablerIcons.CircleCheck
    val Pause
        @Composable get() = TablerIcons.PlayerPause
    val Play
        @Composable get() = TablerIcons.PlayerPlay
    val Queue
        @Composable get() = TablerIcons.Clock
    val Retry
        @Composable get() = TablerIcons.Download
    val HeartFilled
        @Composable get() = Icons.Default.Favorite
    val X
        @Composable get() = TablerIcons.X
    val XCircle
        @Composable get() = TablerIcons.CircleX
    val Mic
        @Composable get() = TablerIcons.Microphone
    val Back
        @Composable get() = if (LocalLayoutDirection.current == LayoutDirection.Ltr)
            TablerIcons.ArrowLeft
        else
            TablerIcons.ArrowRight
}
