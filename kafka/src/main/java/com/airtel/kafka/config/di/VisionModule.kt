package com.airtel.kafka.config.di

import com.airtel.kafka.feature.content.BooksViewModel
import com.airtel.kafka.feature.content.ItemDetailViewModel
import com.airtel.kafka.feature.content.ItemRailViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

/**
 * @author Vipul Kumar; dated 19/01/19.
 */
val visionModule = Kodein.Module("visionModule") {

    bind<BooksViewModel>() with singleton {
        BooksViewModel()
    }

    bind<ItemDetailViewModel>() with singleton {
        ItemDetailViewModel()
    }

    bind<ItemRailViewModel>() with singleton {
        ItemRailViewModel()
    }
}
