package com.kafka.content.ui.homepage

import com.kafka.ui_common.action.Action

sealed class HomepageAction : Action {
    class SelectTag(val tag: HomepageTag) : HomepageAction()
}
