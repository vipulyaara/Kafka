package com.kafka.content.ui.homepage

import com.kafka.content.domain.homepage.HomepageTag
import com.kafka.ui_common.action.Action

sealed class HomepageAction : Action {
    class SelectTag(val tag: HomepageTag) : HomepageAction()
    class OpenSearchFragment(val title: String = "") : HomepageAction()
}
