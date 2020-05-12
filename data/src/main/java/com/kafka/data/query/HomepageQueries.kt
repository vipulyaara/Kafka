package com.kafka.data.query

import com.kafka.data.entities.Language

val queries = arrayListOf(
    ArchiveQuery("Franz Kafka").booksByAuthor("Kafka"),
    ArchiveQuery("राही मासूम रज़ा").booksByAuthor("राही मासूम रज़ा"),
    ArchiveQuery("Mirza Ghalib").booksByAuthor("Mirza Ghalib").copy(resultTye = ResultTye.Banner)
).mapIndexed { index, archiveQuery ->  archiveQuery.copy(position = index) }

val languages = arrayListOf(
    Language("en", "English"),
    Language("hi", "Hindi"),
    Language("ur", "Urdu"),
    Language("de", "German"),
    Language("fr", "French")
)
