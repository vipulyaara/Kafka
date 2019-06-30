package com.kafka.user.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * @author Vipul Kumar; dated 01/04/19.
 */
class ContextMenu : BottomSheetDialogFragment() {
    private val controller = ContextMenuController()
    private var items: List<BaseMenuItem>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun setData(menuItems: List<BaseMenuItem>?) {
        items = menuItems
        controller.setData(menuItems)
    }
}
