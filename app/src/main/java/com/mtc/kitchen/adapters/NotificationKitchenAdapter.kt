package com.mtc.kitchen.adapters

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.mtc.BR
import com.mtc.R
import com.mtc.extension.dataBind
import com.mtc.models.Notifications

class NotificationKitchenAdapter(private var notificationList: ArrayList<Notifications.ResultNoti>) :
    RecyclerView.Adapter<ItemViewHolder>() {


    private lateinit var mListener: OnItemClick

    interface OnItemClick {
        fun onItemClick(position: Int)
    }

    fun setOnItemClick(_mListener: OnItemClick) {
        mListener = _mListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(dataBind(parent, R.layout.notification_row),mListener)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val order = notificationList[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }


}


class ItemViewHolder(
    val binding: ViewDataBinding,
    mListener: NotificationKitchenAdapter.OnItemClick
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(order: Notifications.ResultNoti) {
        binding.setVariable(BR.viewModel, order)
//        binding.setVariable(BR.listener, onClickNotification)
    }

    init {
        itemView.setOnClickListener {
            mListener.onItemClick(adapterPosition)
        }
    }
    // endregion


}