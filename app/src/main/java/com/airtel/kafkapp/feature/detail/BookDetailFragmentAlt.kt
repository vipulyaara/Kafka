//package com.airtel.kafkapp.feature.detail
//
//import android.os.Build
//import android.os.Bundle
//import android.os.Handler
//import android.view.View
//import androidx.core.app.SharedElementCallback
//import androidx.transition.TransitionInflater
//import com.airtel.data.data.db.entities.BookDetail
//import com.airtel.data.model.data.Resource
//import com.airtel.kafka.ServiceRequest
//import com.airtel.kafka.observe
//import com.airtel.kafkapp.R
//import com.airtel.kafkapp.databinding.FragmentBookDetailBinding
//import com.airtel.kafkapp.extensions.viewModel
//import com.airtel.kafkapp.feature.common.DataBindingFragment
//import com.airtel.kafkapp.feature.reviews.BookReviewFragment
//import kotlinx.android.synthetic.main.fragment_book_detail.*
//
///**
// * @author Vipul Kumar; dated 27/12/18.
// *
// * Fragment to host detail page.
// */
//class BookDetailFragmentAlt :
//    DataBindingFragment<FragmentBookDetailBinding>(
//        R.layout.fragment_book_detail
//    ) {
//    private val viewModel: BookDetailViewModel by viewModel()
//    private val controller = BookDetailController(
//        object : BookDetailController.Callbacks {
//            override fun onReviewsClicked() {
//                BookReviewFragment().show(activity?.supportFragmentManager, "")
//            }
//        }
//    )
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        callback()
////        postponeEnterTransition()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            sharedElementEnterTransition = TransitionInflater.from(context)
//                .inflateTransition(android.R.transition.move)
//        }
//    }
//
//    private val contentId by lazy { arguments?.getString("contentId") }
//    private lateinit var request: ServiceRequest<Resource<BookDetail>>
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
////        Handler().postDelayed({ startPostponedEnterTransition() }, 1000)
//
//        rvDetail.apply {
//            setController(controller)
//        }
//
//        request = viewModel.contentDetailRequest(contentId).apply {
//            observe(this@BookDetailFragmentAlt, ::onContentDetailFetched)
////            execute()
//        }
//    }
//
//    private fun onContentDetailFetched(it: Resource<BookDetail>?) {
//        it?.let {
//            logger.d("Detail status ${it.status} error ${it.error} data ${it.data}")
//            controller.setData(it.data)
//        }
//    }
//
//    fun callback() {
//        setEnterSharedElementCallback(object : SharedElementCallback() {
//            override fun onSharedElementEnd(
//                sharedElementNames: MutableList<String>?,
//                sharedElements: MutableList<View>?,
//                sharedElementSnapshots: MutableList<View>?
//            ) {
//                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots)
//                logger.d("end $sharedElementNames")
//            }
//
//            override fun onSharedElementStart(
//                sharedElementNames: MutableList<String>?,
//                sharedElements: MutableList<View>?,
//                sharedElementSnapshots: MutableList<View>?
//            ) {
//                super.onSharedElementStart(
//                    sharedElementNames,
//                    sharedElements,
//                    sharedElementSnapshots
//                )
//                logger.d("start $sharedElementNames")
//            }
//
//            override fun onSharedElementsArrived(
//                sharedElementNames: MutableList<String>?,
//                sharedElements: MutableList<View>?,
//                listener: OnSharedElementsReadyListener?
//            ) {
//                super.onSharedElementsArrived(sharedElementNames, sharedElements, listener)
//                logger.d("arrived $sharedElementNames")
//            }
//
//            override fun onRejectSharedElements(rejectedSharedElements: MutableList<View>?) {
//                super.onRejectSharedElements(rejectedSharedElements)
//                logger.d("reject $rejectedSharedElements")
//            }
//        })
//    }
//}
