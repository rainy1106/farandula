package com.mtc.kitchen

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.mtc.BR
import com.mtc.R
import com.mtc.extension.dataBind
import com.mtc.general.SharedPreference
import kotlinx.android.synthetic.main.order_history_row.view.*
import java.math.RoundingMode
import java.text.DecimalFormat

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
        binding.root.itemPrice.text = roundOffDecimal(order.sub_total.toDouble()).toString()
        //binding.setVariable(BR.listener, onDashboardClickListener)
    }

    private fun roundOffDecimal(number: Double): Double {
        val withTax = (number * SharedPreference.getKitchenTax(binding.root.context)) / 100
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(number.plus(withTax)).toDouble()
    }
    // endregion
}