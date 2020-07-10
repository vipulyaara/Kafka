package com.kafka.data.query

import com.data.base.model.ArchiveQuery
import com.data.base.model.booksByAuthor
import com.kafka.data.entities.Language

val queries = arrayListOf(
    ArchiveQuery("Mirza Ghalib").booksByAuthor("Mirza Ghalib"),
    ArchiveQuery("Franz Kafka").booksByAuthor("Franz Kafka"),
    ArchiveQuery("राही मासूम रज़ा").booksByAuthor("राही मासूम रज़ा")
).mapIndexed { index, archiveQuery ->  archiveQuery.copy(position = index) }

val languages = arrayListOf(
    Language("en", "English"),
    Language("hi", "Hindi"),
    Language("ur", "Urdu"),
    Language("de", "German"),
    Language("fr", "French")
)
