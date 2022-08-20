package com.mtc.kitchen

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.mtc.BR
import com.mtc.R
import com.mtc.extension.dataBind
import com.mtc.general.BaseViewModel
import kotlinx.android.synthetic.main.order_details_row.view.*

class OrderDetailsAdapter(private var orderList: ArrayList<OrderListItem.Cart>) :

    RecyclerView.Adapter<OrderDetailsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
        return OrderDetailsViewHolder(
            dataBind(
                parent,
                R.layout.order_details_row
            )
        )
    }

    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {
        val card = orderList[position]
        holder.bind(card)
        ("$ "+BaseViewModel().roundOffDecimal(card.price.toDouble().times(card.quantity.toInt())).toString()).also { holder.itemView.totalPrice.text = it }

    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}

class OrderDetailsViewHolder(val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    // region - Public function
    fun bind(order: OrderListItem.Cart) {
        binding.setVariable(BR.viewModel, order)
        //binding.setVariable(BR.listener, onDashboardClickListener)
    }
    // endregion

}