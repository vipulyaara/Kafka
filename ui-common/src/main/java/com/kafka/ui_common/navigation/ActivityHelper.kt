@file:JvmName("ActivityHelper")

package com.kafka.ui_common.navigation

import android.content.Intent

/**
 * Helpers to start activities in a modularized world.
 */

private const val PACKAGE_NAME = "com.kafka.user"

/**
 * Create an Intent with [Intent.ACTION_VIEW] to an [AddressableActivity].
 */
fun intentTo(addressableActivity: AddressableActivity): Intent {
    return Intent(Intent.ACTION_VIEW).setClassName(
        PACKAGE_NAME,
            addressableActivity.className)
}

/**
 * An [android.app.Activity] that can be addressed by an intent.
 */
interface AddressableActivity {
    /**
     * The activity class name.
     */
    val className: String
}

/**
 * All addressable activities.
 *
 * Can contain intent extra names or functions associated with the activity creation.
 */
object Activities {

    /**
     * AboutActivity
     */
    object Main : AddressableActivity {
        override val className = "$PACKAGE_NAME.about.ui.AboutActivity"
    }

    /**
     * Base object for DesignerNews activities.
     */
    object Reader {
        /**
         * DesignerNews LoginActivity
         */
        object Reader : AddressableActivity {
            override val className = "com.kafka.reader.ui.ReaderActivity"
        }
    }

    /**
     * Base object for Dribbble activities.
     */
    object Dribbble {
        /**
         * ShotActivity
         */
        object Shot : AddressableActivity {
            override val className = "$PACKAGE_NAME.dribbble.ui.shot.ShotActivity"

            const val EXTRA_SHOT_ID = "EXTRA_SHOT_ID"
            const val RESULT_EXTRA_SHOT_ID = "RESULT_EXTRA_SHOT_ID"
        }
    }

    /**
     * SearchActivity
     */
    object Search : AddressableActivity {
        override val className = "$PACKAGE_NAME.search.ui.SearchActivity"

        const val EXTRA_QUERY = "EXTRA_QUERY"
        const val EXTRA_SAVE_DRIBBBLE = "EXTRA_SAVE_DRIBBBLE"
        const val EXTRA_SAVE_DESIGNER_NEWS = "EXTRA_SAVE_DESIGNER_NEWS"
        const val RESULT_CODE_SAVE = 7
    }
}
