package com.mtc.order

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.mtc.BR

/**
 *
 * @author GWL
 */
class ItemViewHolder(val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    // region - Public function
    fun bind(order: OrderItem) {
        binding.setVariable(BR.viewModel, order)
        //binding.setVariable(BR.listener, onDashboardClickListener)
    }
    // endregion
}
