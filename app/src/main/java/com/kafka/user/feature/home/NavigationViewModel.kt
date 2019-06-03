package com.kafka.user.feature.home

import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.transaction
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kafka.data.data.db.MiddlewareTypeConverters
import com.kafka.data.entities.Item
import com.kafka.data.util.SingleLiveEvent
import com.kafka.user.R
import com.kafka.user.extensions.logger
import com.kafka.user.extensions.show
import com.kafka.user.feature.MainFragment
import com.kafka.user.feature.detail.ItemDetailFragment
import com.kafka.user.feature.downloads.BookDownloadFragment
import com.kafka.user.feature.genre.GenreFragment
import com.kafka.user.feature.profile.ProfileFragment
import com.kafka.user.feature.reviews.BookReviewFragment
import com.kafka.user.feature.search.SearchFragment
import com.kafka.user.ui.SharedElementHelper

/**
 * @author Vipul Kumar; dated 28/03/19.
 */
class NavigationViewModel : ViewModel(), HomeNavigator {

    override fun showReviews() {
        launchReviewsFragment(fragmentManager)
    }

    override fun showDownloads() {
        launchDownloadsFragment(fragmentManager)
    }

    override fun showProfile() {
        launchProfileFragment(fragmentManager)
    }

    lateinit var fragmentManager: FragmentManager

    override fun showMainFragment(sharedElements: SharedElementHelper?) {
        launchMainFragment(fragmentManager)
    }

    override fun showHomepage(sharedElements: SharedElementHelper?) {
    }

    override fun showItemDetail(item: Item, sharedElements: SharedElementHelper?) {
        launchDetailFragment(fragmentManager, sharedElements)
    }

    override fun showSettings() {

    }

    override fun onUpClicked() {
//        _upClickedCall.call()
    }


    private val _showPopularCall = SingleLiveEvent<SharedElementHelper>(errorOnNoObservers = true)
    val showPopularCall: LiveData<SharedElementHelper>
        get() = _showPopularCall

    private val _showTrendingCall = SingleLiveEvent<SharedElementHelper>(errorOnNoObservers = true)
    val showTrendingCall: LiveData<SharedElementHelper>
        get() = _showTrendingCall

    private val _upClickedCall = SingleLiveEvent<Unit>(errorOnNoObservers = true)
    val upClickedCall: LiveData<Unit>
        get() = _upClickedCall

    private fun launchMainFragment(fragmentManager: FragmentManager) {
        fragmentManager.transaction {
            replace(R.id.fragmentContainer, MainFragment())
        }
    }

    private fun launchDownloadsFragment(fragmentManager: FragmentManager) {
        BookDownloadFragment.newInstance("")
            .show(fragmentManager)
    }

    private fun launchReviewsFragment(fragmentManager: FragmentManager) {
        BookReviewFragment()
            .show(fragmentManager)
    }

    private fun launchProfileFragment(fragmentManager: FragmentManager) {
        fragmentManager.transaction {
            add(R.id.fragmentContainer, ProfileFragment())
            addToBackStack("")
        }
    }

    private fun launchSearchFragment(fragmentManager: FragmentManager) {
        fragmentManager.transaction {
            add(R.id.fragmentContainer, SearchFragment())
            addToBackStack("")
        }
    }

    private fun launchDetailFragment(
        fragmentManager: FragmentManager,
        sharedElements: SharedElementHelper?
    ) {
        val detailFragment = ItemDetailFragment.create()
        fragmentManager.transaction {
            addToBackStack("")
            apply {
                if (sharedElements != null && !sharedElements.isEmpty()) {
                    logger.d("shared element is ${sharedElements.sharedElementViews}")
                    sharedElements.applyToTransaction(this)
                } else {
                    logger.d("shared element is null $sharedElements")
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                }
            }
//            detach(fragment)
            add(R.id.fragmentContainer, detailFragment)
        }
    }

    fun onMenuItemClicked(item: MenuItem) =
        when (item.itemId) {
            R.id.menu_search -> {
                launchSearchFragment(fragmentManager)
                true
            }
            else -> false
        }
}
