package org.kafka.common.image

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import compose.icons.FeatherIcons
import compose.icons.FontAwesomeIcons
import compose.icons.TablerIcons
import compose.icons.feathericons.File
import compose.icons.feathericons.Search
import compose.icons.feathericons.User
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.FileContract
import compose.icons.fontawesomeicons.solid.Headphones
import compose.icons.fontawesomeicons.solid.UserCircle
import compose.icons.tablericons.ArrowForward
import compose.icons.tablericons.ArrowLeft
import compose.icons.tablericons.ArrowRight
import compose.icons.tablericons.Book
import compose.icons.tablericons.Bookmark
import compose.icons.tablericons.ChevronUpLeft
import compose.icons.tablericons.CircleCheck
import compose.icons.tablericons.CircleX
import compose.icons.tablericons.Clock
import compose.icons.tablericons.Download
import compose.icons.tablericons.Eye
import compose.icons.tablericons.EyeOff
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
import org.kafka.common.image.kafkaicons.Vinyl

object Icons {
    val Home: ImageVector = TablerIcons.Home
    val HomeActive: ImageVector = TablerIcons.Home2
    val Search: ImageVector = TablerIcons.Search
    val Check: ImageVector = TablerIcons.CircleCheck
    val SearchActive: ImageVector = FeatherIcons.Search
    val Library: ImageVector = TablerIcons.Bookmark
    val LibraryActive: ImageVector = TablerIcons.Book
    val Profile: ImageVector = FontAwesomeIcons.Solid.UserCircle
    val Files: ImageVector = FeatherIcons.File
    val ProfileActive: ImageVector = FeatherIcons.User
    val GoToTop: ImageVector = TablerIcons.ChevronUpLeft
    val List: ImageVector = TablerIcons.LayoutList
    val Grid: ImageVector = TablerIcons.LayoutGrid
    val Web: ImageVector = TablerIcons.World
    val Share: ImageVector = TablerIcons.Share
    val Heart = TablerIcons.Heart
    val Download = TablerIcons.Download
    val Downloaded = TablerIcons.CircleCheck
    val Pause = TablerIcons.PlayerPause
    val Play = TablerIcons.PlayerPlay
    val Queue = TablerIcons.Clock
    val Retry = TablerIcons.Download
    val HeartFilled = Icons.Default.Favorite
    val X = TablerIcons.X
    val XCircle = TablerIcons.CircleX
    val Mic = TablerIcons.Microphone
    val Audio = FontAwesomeIcons.Solid.Headphones
    val Texts = FontAwesomeIcons.Solid.FileContract
    val Back
        @Composable get() = if (LocalLayoutDirection.current == LayoutDirection.Ltr)
            TablerIcons.ArrowLeft
        else
            TablerIcons.ArrowRight
    val Eye = TablerIcons.Eye
    val EyeOff = TablerIcons.EyeOff
    val ArrowForward = TablerIcons.ArrowRight
    val Vinyl = KafkaIcons.Vinyl
}
