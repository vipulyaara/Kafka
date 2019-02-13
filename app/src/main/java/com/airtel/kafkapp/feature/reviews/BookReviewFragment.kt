package com.airtel.kafkapp.feature.reviews

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airtel.data.data.db.entities.Book
import com.airtel.kafkapp.feature.MainActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_reviews.*

/**
 * @author Vipul Kumar; dated 05/02/19.
 *
 * Fragment to show reviews.
 */
class BookReviewFragment : BottomSheetDialogFragment() {
    private val controller = BookReviewController(
        object : BookReviewController.Callbacks {
            override fun onReviewsClicked() {
                (activity as MainActivity).launchSearchFragment()
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.airtel.kafkapp.R.layout.fragment_reviews, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvReviews.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            setController(controller)
        }

        controller.setData(Book(bookId = ""))
    }
}
