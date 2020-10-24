package com.kafka.content.ui//package com.kafka.content.ui
//
//import android.view.ViewTreeObserver
//import android.widget.ImageView
//import androidx.fragment.app.Fragment
//import androidx.transition.TransitionInflater
//import com.kafka.data.extensions.debug
//import com.kafka.content.R
//import com.kafka.content.ui.detail.ItemDetailFragment
//import com.kafka.content.ui.homepage.HomepageFragment
//import kotlinx.android.synthetic.main.fragment_item_detail.*
//import java.util.concurrent.TimeUnit
//
//fun Fragment.setEnterSharedElement() {
//    sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.arc_motion)
//    sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(R.transition.arc_motion)
//    postponeEnterTransition(300, TimeUnit.MILLISECONDS)
//}
//
//fun ItemDetailFragment.setSharedElementData() {
//    val image = arguments?.getString("image")
//    debug { "image is $image" }
////    viewModel.currentState().apply {
////        if (itemDetail == null) {
////            itemDetailController.setData(ItemDetailViewState(itemDetail = ItemDetail(coverImage = image)))
////        }
////    }
//
//    recyclerView.viewTreeObserver
//        .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//                recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
//            }
//        })
//}
//
//fun HomepageFragment.navigateWithSharedElement() {
////    val image = action.view.findViewById<ImageView>(R.id.heroImage)
////    val item = action.item
////    image.transitionName = "detail"
////    navController.navigate(
////        R.id.navigation_item_detail, bundleOf(
////            "item_id" to item.itemId,
////            "image" to item.coverImage
////        ), null,
////        FragmentNavigatorExtras(image to image.transitionName)
//}



//private fun getImageView() = recyclerView.findViewHolderForLayoutPosition(0)
//    ?.itemView?.findViewById<MaterialCardView>(R.id.imageCard)
//
