package com.mtc.extension

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

// region - Public function
fun <T : RecyclerView.ViewHolder> RecyclerView.Adapter<T>.dataBind(
    parent: ViewGroup,
    layoutId: Int
): ViewDataBinding {
    return DataBindingUtil.inflate(
        LayoutInflater.from(parent.context), layoutId, parent, false
    )
}

//fun Fragment.dataBind(
//    container: ViewGroup?,
//    layoutId: Int,
//    inflater: LayoutInflater
//): LayoutDayTimePickerBinding {
//    return DataBindingUtil.inflate(inflater, layoutId, container, false)
//}
// endregion