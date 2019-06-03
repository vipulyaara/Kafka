
package com.kafka.user.feature.home

import com.kafka.data.entities.Item
import com.kafka.user.ui.SharedElementHelper

interface HomeNavigator {
    fun showMainFragment(sharedElements: SharedElementHelper?)
    fun showHomepage(sharedElements: SharedElementHelper?)
    fun showItemDetail(item: Item, sharedElements: SharedElementHelper?)
    fun showReviews()
    fun showDownloads()
    fun showProfile()
    fun onUpClicked()
    fun showSettings()
}
