package org.rekhta.domain.interactors.query

import com.kafka.data.model.*
import org.rekhta.base.debug
import javax.inject.Inject

class BuildRemoteQuery @Inject constructor() {

    operator fun invoke(archiveQuery: ArchiveQuery): String {
        var query = ""
        val groups = archiveQuery.queries.groupBy { it.key }

        fun String.toRemoteJoiner() = if (this.isEmpty()) "" else "+$this+"

        groups.keys.forEach {
            val value = groups[it] ?: error("")
            val newKey = replaceCreatorNameForRemoteQuery(it)
            query += buildSameKeyQueries(newKey, value)
            query += value.last().joiner.toRemoteJoiner()
        }

        query += joinerAnd.toRemoteJoiner()
        query += "$_mediaType:($_mediaTypeText OR $_mediaTypeAudio)"

//    query = query.plus(keyValueRemoteQuery(_mediaType, _mediaTypeText, joinerOr.toRemoteJoiner()))
//    query = query.plus(keyValueRemoteQuery(_mediaType, _mediatypeAudio, ""))

        debug { "Remote query is $query" }

        return query
    }

    private fun buildSameKeyQueries(key: String, items: List<QueryItem>): String {
        var query = "${key}:("

        items.forEach {
            query += "${it.value} ${it.joiner} "
        }

        query = query.removeSuffix(" ${items.last().joiner} ")
        query += ")"

        return query
    }

    private fun replaceCreatorNameForRemoteQuery(key: String) =
        if (key == _creator) _creator_remote else key
}
