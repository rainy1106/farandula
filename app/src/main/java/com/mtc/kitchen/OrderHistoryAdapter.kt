package com.mtc.kitchen

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.mtc.BR
import com.mtc.R
import com.mtc.extension.dataBind

class OrderHistoryAdapter(private var orderList: ArrayList<OrderHistory.Result>) :

    RecyclerView.Adapter<ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(dataBind(parent, R.layout.order_history_row))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val order = orderList[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}

class ItemViewHolder(val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(order: OrderHistory.Result) {
        binding.setVariable(BR.viewModel, order)
        //binding.setVariable(BR.listener, onDashboardClickListener)
    }
    // endregion
}