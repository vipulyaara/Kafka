package com.kafka.common.image

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import compose.icons.FeatherIcons
import compose.icons.FontAwesomeIcons
import compose.icons.Octicons
import compose.icons.TablerIcons
import compose.icons.feathericons.AlignJustify
import compose.icons.feathericons.AlignLeft
import compose.icons.feathericons.AlignRight
import compose.icons.feathericons.Search
import compose.icons.feathericons.ThumbsDown
import compose.icons.feathericons.ThumbsUp
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.brands.Google
import compose.icons.fontawesomeicons.regular.Bookmark
import compose.icons.fontawesomeicons.solid.Bookmark
import compose.icons.fontawesomeicons.solid.FileContract
import compose.icons.fontawesomeicons.solid.Headphones
import compose.icons.fontawesomeicons.solid.Star
import compose.icons.fontawesomeicons.solid.UserCircle
import compose.icons.octicons.ArrowDownRight24
import compose.icons.tablericons.Adjustments
import compose.icons.tablericons.ArrowLeft
import compose.icons.tablericons.ArrowRight
import compose.icons.tablericons.Bell
import compose.icons.tablericons.Book
import compose.icons.tablericons.Bookmark
import compose.icons.tablericons.BrandBooking
import compose.icons.tablericons.ChevronDown
import compose.icons.tablericons.ChevronRight
import compose.icons.tablericons.CircleCheck
import compose.icons.tablericons.CircleX
import compose.icons.tablericons.Clock
import compose.icons.tablericons.Copy
import compose.icons.tablericons.Dots
import compose.icons.tablericons.DotsVertical
import compose.icons.tablericons.Download
import compose.icons.tablericons.Edit
import compose.icons.tablericons.Eye
import compose.icons.tablericons.EyeOff
import compose.icons.tablericons.FileText
import compose.icons.tablericons.Gift
import compose.icons.tablericons.Heart
import compose.icons.tablericons.Home
import compose.icons.tablericons.Home2
import compose.icons.tablericons.Language
import compose.icons.tablericons.LayoutGrid
import compose.icons.tablericons.LayoutList
import compose.icons.tablericons.Logout
import compose.icons.tablericons.Message
import compose.icons.tablericons.Microphone
import compose.icons.tablericons.Minus
import compose.icons.tablericons.Moon
import compose.icons.tablericons.Photo
import compose.icons.tablericons.PlayerPause
import compose.icons.tablericons.PlayerPlay
import compose.icons.tablericons.Plus
import compose.icons.tablericons.Search
import compose.icons.tablericons.Settings
import compose.icons.tablericons.Share
import compose.icons.tablericons.Sun
import compose.icons.tablericons.Trash
import compose.icons.tablericons.Umbrella
import compose.icons.tablericons.Upload
import compose.icons.tablericons.X

object Icons {
    val Home: ImageVector = TablerIcons.Home
    val HomeActive: ImageVector = TablerIcons.Home2
    val Search: ImageVector = TablerIcons.Search
    val Copy: ImageVector = TablerIcons.Copy
    val Check: ImageVector = TablerIcons.CircleCheck
    val SearchActive: ImageVector = FeatherIcons.Search
    val Library: ImageVector = TablerIcons.Bookmark
    val LibraryActive: ImageVector = TablerIcons.Book
    val Profile: ImageVector = FontAwesomeIcons.Solid.UserCircle
    val List: ImageVector = TablerIcons.LayoutList
    val Grid: ImageVector = TablerIcons.LayoutGrid
    val Share: ImageVector = TablerIcons.Share
    val Heart = TablerIcons.Heart
    val Download = TablerIcons.Download
    val Downloaded = TablerIcons.CircleCheck
    val Delete = TablerIcons.Trash
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
    val Photo = TablerIcons.Photo
    val Moon = TablerIcons.Moon
    val Sun = TablerIcons.Sun
    val Logout = TablerIcons.Logout
    val Feedback = TablerIcons.Message
    val SafeMode = TablerIcons.Umbrella
    val Gift = TablerIcons.Gift
    val Google = FontAwesomeIcons.Brands.Google
    val Bell = TablerIcons.Bell
    val ReadOffline = TablerIcons.FileText
    val OverflowMenu = TablerIcons.DotsVertical
    val ArrowForwardCircle = Octicons.ArrowDownRight24
    val Minus = TablerIcons.Minus
    val Plus = TablerIcons.Plus
    val DarkMode = TablerIcons.Moon
    val LightMode = TablerIcons.Sun
    val MenuBook = TablerIcons.BrandBooking
    val Book = TablerIcons.Book
    val AlignLeft = FeatherIcons.AlignLeft
    val AlignRight = FeatherIcons.AlignRight
    val AlignJustified = FeatherIcons.AlignJustify
    val Settings = TablerIcons.Settings
    val Bookmark = FontAwesomeIcons.Regular.Bookmark
    val BookmarkFilled = FontAwesomeIcons.Solid.Bookmark
    val Translate = TablerIcons.Language
    val Highlight = TablerIcons.Bookmark
    val Upload = TablerIcons.Upload
    val System = TablerIcons.Adjustments
    val Edit = TablerIcons.Edit
    val Star = FontAwesomeIcons.Solid.Star
    val ThumbUp = FeatherIcons.ThumbsUp
    val ThumbDown = FeatherIcons.ThumbsDown
    val ChevronRight = TablerIcons.ChevronRight
    val ChevronDown = TablerIcons.ChevronDown
    val OverflowMenuHorizontal = TablerIcons.Dots
}
