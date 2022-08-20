package com.mtc.order

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.mtc.BR
import com.mtc.R
import com.mtc.extension.dataBind
import com.mtc.interfaces.EventHandler
import kotlinx.android.synthetic.main.layout.view.*
import kotlinx.android.synthetic.main.my_order_list_row.view.*

class MyOrderListAdapter(
    onAdapterUpdate: EventHandler?
) :
    RecyclerView.Adapter<MyOrderListAdapter.OrderViewHolder>() {

    private var onAdapterUpdate = onAdapterUpdate

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderViewHolder {
        return OrderViewHolder(
            dataBind(parent, R.layout.my_order_list_row)
        )
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(OrderListViewModel.orderListSelected[position])
        holder.checkDisable(holder.itemView, OrderListViewModel.orderListSelected[position])

        if (OrderListViewModel.orderListSelected[position].extra_itemsA.trim().isNotEmpty()) {
            holder.itemView.myorderListExtraItem.visibility = View.VISIBLE
        } else {
            holder.itemView.myorderListExtraItem.visibility = View.GONE
        }

        holder.itemView.number_button.setOnValueChangeListener { _, _, newValue ->
            if (OrderListViewModel.orderListSelected[position].isEnable) {
                OrderListViewModel.orderListSelected[position].quatity = newValue.toString()
                onAdapterUpdate?.onRemove()
            }

        }
        holder.itemView.removeOrder.setOnClickListener {
            if (OrderListViewModel.orderListSelected[position].isEnable) {
                OrderListViewModel.orderListSelected.remove(OrderListViewModel.orderListSelected[position])
                onAdapterUpdate?.onRemove()
                notifyDataSetChanged()
            }
        }
        holder.itemView.number_counter.text = OrderListViewModel.orderListSelected[position].quatity
    }

    override fun getItemCount(): Int = OrderListViewModel.orderListSelected.size

    class OrderViewHolder(
        val binding: ViewDataBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(orderItem: OrderItem) {
            binding.setVariable(BR.viewModel, orderItem)
//            binding.setVariable(BR.listener, onAdapterUpdate)
            binding.executePendingBindings()
        }

        fun checkDisable(view: View, order: OrderItem) {
            if (!order.isEnable) {
                view.orderLayoutMyList.isEnabled = false
                view.removeOrder.isEnabled = false
                view.number_button.visibility = View.GONE
                view.orderLayoutMyList.setBackgroundColor(Color.parseColor("#D5F3F1"))
            } else {
                //
            }
        }

        // endregion
    }
}