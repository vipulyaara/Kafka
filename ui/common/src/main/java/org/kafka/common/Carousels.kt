package org.kafka.common

data class CarouselItem(val image: Int, val itemId: String)

val carousels = listOf(
    CarouselItem(image = R.drawable.img_kafka_banner, itemId = "kafka_archives"),
    CarouselItem(image = R.drawable.img_banner_adbi_duniya, itemId = "adbi-duniya"),
    CarouselItem(image = R.drawable.img_short_stories_kafka, itemId = "kafka-short-stories"),
    CarouselItem(image = R.drawable.img_chand_nagar_insha, itemId = "ibne-insha-OK_compressed"),
    CarouselItem(image = R.drawable.img_ghalib, itemId = "ghazals_ghalib_0809_librivox"),
    CarouselItem(image = R.drawable.img_hamlet, itemId = "hamlet_0911_librivox"),
    CarouselItem(image = R.drawable.img_neem_ka_ped_raza, itemId = "neemkapedhindiedition"),
    CarouselItem(image = R.drawable.img_short_stories_poe, itemId = "poe-short-stories"),
)
