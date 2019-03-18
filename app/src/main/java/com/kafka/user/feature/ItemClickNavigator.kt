package com.kafka.user.feature

import com.kafka.data.entities.Item

/**
 * @author Vipul Kumar; dated 01/03/19.
 */
abstract class ItemClickNavigator {
    fun onItemClicked(item: Item) {}
}
