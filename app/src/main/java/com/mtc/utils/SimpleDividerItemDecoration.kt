package com.mtc.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView

class SimpleDividerItemDecoration(
    context: Context, @SuppressLint("SupportAnnotationUsage")
    @DrawableRes dividerRes: Drawable
) :
    RecyclerView.ItemDecoration() {

    private val mDivider = dividerRes //Drawable = ContextCompat.getDrawable(context, dividerRes)!!

    override fun onDrawOver(c: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child: View = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top: Int = child.bottom + params.bottomMargin
            val bottom = top + mDivider.intrinsicHeight
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }

}