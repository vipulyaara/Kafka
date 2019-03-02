package com.airtel.kafkapp.feature

import com.airtel.data.entities.Item

/**
 * @author Vipul Kumar; dated 01/03/19.
 */
abstract class ItemClickNavigator {
    fun onItemClicked(item: Item) {}
}
