package com.kafka.ui.home

import com.kafka.data.entities.Item

sealed class HomepageAction
class ContentItemClick(val item: Item) : HomepageAction()
object SearchItemClick : HomepageAction()
