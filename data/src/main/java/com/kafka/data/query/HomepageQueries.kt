package com.kafka.data.query

import com.kafka.data.entities.Language

val queries = arrayListOf(
    ArchiveQuery("Franz Kafka").booksByAuthor("Kafka"),
    ArchiveQuery("Dostoyevsky").booksByAuthor("Dostoyevsky"),
    ArchiveQuery("Mirza Ghalib").booksByAuthor("Mirza Ghalib").copy(resultTye = ResultTye.Banner)
).mapIndexed { index, archiveQuery ->  archiveQuery.copy(position = index) }

val languages = arrayListOf(
    Language("en", "English"),
    Language("hi", "Hindi"),
    Language("ur", "Urdu"),
    Language("de", "German"),
    Language("fr", "French")
)
