package com.kafka.content.ui.detail

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import com.kafka.content.R
import com.kafka.ui_common.Navigation
import com.kafka.ui_common.action.RealActioner
import com.kafka.ui_common.base.BaseFragment
import com.kafka.ui_common.navigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_item_detail.*

@AndroidEntryPoint
class ItemDetailFragment : BaseFragment() {
    private val viewModel: ItemDetailViewModel by viewModels()

    //    private val readerViewModel: ReaderViewModel by viewModels()
    private val itemDetailController = ItemDetailController()
    private val pendingActions = RealActioner<ItemDetailAction>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            setController(itemDetailController)
            itemDetailController.actioner = pendingActions
        }

        viewModel.liveData.observe(viewLifecycleOwner, Observer {
            itemDetailController.setData(it)
        })

        lifecycleScope.launchWhenCreated {
            pendingActions.observe { action ->
                when (action) {
                    is ItemDetailAction.RelatedItemClick -> findNavController().navigate(Navigation.ItemDetail(action.item.itemId))
                    is ItemDetailAction.Play -> findNavController().navigate(Navigation.Player(action.itemId))
                    is ItemDetailAction.Read -> {
//                        readerViewModel.read(requireContext(), action.readerUrl)
                    }
                    is ItemDetailAction.FavoriteClick -> {
                        viewModel.sendAction(action)
                        viewModel.showFavoriteToast(requireContext())
                    }
                    else -> {
                    }
                }
            }
        }

        requireArguments().getString("item_id")?.let {
            viewModel.observeItemDetail(it)
            viewModel.updateItemDetail(it)
        }

        addTransitionableTarget(view, R.id.endCard)
        addTransitionableTarget(view, R.id.fab)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item_detail, container, false)
    }

    private var startView: View? = null

    private fun addTransitionableTarget(view: View, @IdRes id: Int) {
        val target = view.findViewById<View>(id)
        if (target != null) {
            ViewCompat.setTransitionName(target, id.toString())
            if (id == R.id.endCard) {
                target.setOnClickListener { v: View? -> showStartView() }
            } else {
                target.setOnClickListener { startView: View? -> showEndView(startView!!) }
            }
        }
    }

    private fun showEndView(startView: View) {
        // Save a reference to the start view that triggered the transition in order to know which view
        // to transition into during the return transition.
        this.startView = startView

        // Construct a container transform transition between two views.
        val transition: MaterialContainerTransform =
            buildContainerTransform(true)
        transition.startView = startView
        transition.endView = endCard

        // Add a single target to avoid the container transform from running on both the start
        // and end view
        transition.addTarget(startView)

        // Trigger the container transform transition.
        TransitionManager.beginDelayedTransition(root, transition)
        startView.visibility = View.INVISIBLE
        endCard?.visibility = View.VISIBLE
    }

    private fun showStartView() {
        checkNotNull(startView) { "startView must not be null" }

        // Construct a container transform transition between two views.
        val transition: MaterialContainerTransform =
            buildContainerTransform(false)
        transition.startView = endCard
        transition.endView = startView

        // Add a single target to avoid the container transform from running on both the start
        // and end view
        transition.addTarget(startView!!)

        // Trigger the container transform transition.
        TransitionManager.beginDelayedTransition(root, transition)
        startView?.visibility = View.VISIBLE
        endCard?.visibility = View.INVISIBLE
    }

    private fun buildContainerTransform(entering: Boolean): MaterialContainerTransform {
        val transform =
            MaterialContainerTransform()
        transform.scrimColor = Color.TRANSPARENT
        configure(transform, entering)
        return transform
    }

    private fun configure(
        transform: MaterialContainerTransform,
        entering: Boolean
    ) {
        transform.duration = 500
        transform.interpolator = FastOutSlowInInterpolator()
            transform.setPathMotion(MaterialArcMotion())
        transform.fadeMode = MaterialContainerTransform.FADE_MODE_IN
    }
}
