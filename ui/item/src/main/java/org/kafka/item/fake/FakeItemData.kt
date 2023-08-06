package org.kafka.item.fake

import com.kafka.data.entities.Creator
import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemDetail
import kotlinx.collections.immutable.persistentListOf

object FakeItemData {
    private val coverUrl =
        "https://images.unsplash.com/photo-1461988320302-91bde64fc8e4?ixid=2yJhcHBfaWQiOjEyMDd9&&fm=jpg&w=400&fit=max"

    private val item0 = Item(
        itemId = "0",
        title = "Metamorphosis",
        creator = Creator("Franz Kafka", "Franz Kafka"),
        coverImage = coverUrl,
    )

    private val item1 = Item(
        itemId = "1",
        title = "The Trial",
        creator = Creator("Franz Kafka", "Franz Kafka"),
        coverImage = coverUrl,
    )

    private val item2 = Item(
        itemId = "2",
        title = "The Castle",
        creator = Creator("Franz Kafka", "Franz Kafka"),
        coverImage = coverUrl,
    )

    val items = persistentListOf(item0, item1, item2)

    val itemDetail = ItemDetail(
        itemId = "123",
        title = "Metamorphosis",
        creator = "Franz Kafka",
        coverImage = coverUrl,
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget aliquam ultricies, nunc nisl aliquet nunc, vitae aliquam nisl nisl eu nunc. Donec euismod, nisl eget aliquam ultricies, nunc nisl aliquet nunc, vitae aliquam nisl nisl eu nunc.",
        subject = listOf("Insect", "Existential", "Classics", "Fiction"),
    )
}