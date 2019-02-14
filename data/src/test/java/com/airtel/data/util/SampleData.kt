package com.airtel.data.util

import com.airtel.data.data.db.MiddlewareDb
import com.airtel.data.entities.Book
import java.time.OffsetDateTime

const val book1Id = 1L
val book = Book(id = book1Id)

const val book2Id = 2L
val book2 = Book(id = book2Id)

fun insertShow(db: MiddlewareDb) = db.contentDao().insertBooks()

fun deleteShow(db: TiviDatabase) = db.showDao().delete(show)

const val seasonSpecialsId = 1L
val seasonSpecials = Season(
        id = seasonSpecialsId,
        showId = book1Id,
        title = "Specials",
        number = Season.NUMBER_SPECIALS,
        traktId = 7042
)

const val seasonOneId = 2L
val seasonOne = Season(
        id = seasonOneId,
        showId = book1Id,
        title = "Season 1",
        number = 1,
        traktId = 5443
)

const val seasonTwoId = 3L
val seasonTwo = Season(
        id = seasonTwoId,
        showId = book1Id,
        title = "Season 2",
        number = 2,
        traktId = 5434
)

fun deleteSeason(db: TiviDatabase) = db.seasonsDao().delete(seasonOne)

fun insertSeason(db: TiviDatabase) = db.seasonsDao().insert(seasonOne)

val episodeOne = Episode(id = 1, title = "Kangaroo Court", seasonId = seasonOne.id, number = 0, traktId = 59830)
val episodeTwo = Episode(id = 2, title = "Bushtucker", seasonId = seasonOne.id, number = 1, traktId = 33435)
val episodeThree = Episode(id = 3, title = "Wallaby Bungee", seasonId = seasonOne.id, number = 2, traktId = 44542)

val episodes = listOf(episodeOne, episodeTwo, episodeThree)

fun insertEpisodes(db: TiviDatabase) = episodes.forEach { db.episodesDao().insert(it) }

fun deleteEpisodes(db: TiviDatabase) = episodes.forEach { db.episodesDao().delete(it) }

const val episodeWatch1Id = 1L
val episodeWatch1 = EpisodeWatchEntry(
        id = episodeWatch1Id,
        watchedAt = OffsetDateTime.now(),
        episodeId = episodeOne.id,
        traktId = 435214
)

const val episodeWatch2Id = 2L
val episodeWatch2 = episodeWatch1.copy(id = episodeWatch2Id, traktId = 4385783)

val episodeWatch2PendingSend = episodeWatch2.copy(pendingAction = PendingAction.UPLOAD)
val episodeWatch2PendingDelete = episodeWatch2.copy(pendingAction = PendingAction.DELETE)

fun insertFollowedShow(db: TiviDatabase) = db.followedShowsDao().insert(followedShow1)

const val followedShowId = 1L
val followedShow1 = FollowedShowEntry(followedShowId, book1Id)
val followedShow1PendingDelete = followedShow1.copy(pendingAction = PendingAction.DELETE)
val followedShow1PendingUpload = followedShow1.copy(pendingAction = PendingAction.UPLOAD)

const val followedShow2Id = 2L
val followedShow2 = FollowedShowEntry(followedShow2Id, book2Id)
