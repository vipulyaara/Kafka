package org.kafka.domain.interactors.homepage

import javax.inject.Inject

class UpdateDefaultHomepage @Inject constructor() {

}

sealed class HomepageItem {
    data class Item(val itemId: String) : HomepageItem()
    object Favorites : HomepageItem()
    object Banners : HomepageItem()
    object RecentItems : HomepageItem()
}
