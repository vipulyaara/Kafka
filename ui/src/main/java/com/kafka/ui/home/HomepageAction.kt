package com.kafka.ui.home

sealed class HomepageAction

class ContentItemClick(val contentId: String) : HomepageAction()