package com.mtc.general

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.mtc.R
import com.mtc.api.APIConstant
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation

@BindingAdapter("imageUrl")
fun ImageView.loadRoundedCornerImage(str: String?) {
    val radius = 0
    val margin = 0
    val transformation: Transformation =
        RoundedCornersTransform(radius, margin)
    try {
        Log.v("URL IMAGE",APIConstant.BASE_IMAGE_URL + str!!)
        Picasso.get()
            .load(APIConstant.BASE_IMAGE_URL + str)
            .placeholder(R.drawable.na)
            .error(R.drawable.na)
            .fit()
            .into(this)
    } catch (ex: Exception) {
        ex.printStackTrace()
        this.setBackgroundResource(R.drawable.na)
    }


//    Glide.with(this.context).load(APIConstant.BASE_IMAGE_URL + str)
//        .placeholder(R.drawable.na)
//        .error(R.drawable.na).into(this);
}


//@BindingAdapter("fullImageUrl")
//fun ImageView.loadRoundedCornerImageFullUrl(str: String?) {
//    val radius = 3
//    val margin = 3
//    val transformation: Transformation =
//        RoundedCornersTransform(radius, margin)
//    try {
//        Picasso.get()
//            .load(str)
//            .placeholder(R.drawable.na)
//            .error(R.drawable.na)
//            .transform(transformation)
//            .into(this)
//    } catch (ex: Exception) {
//        this.setImageResource(R.drawable.na)
//    }
//
//}
