package com.mtc.order

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.mtc.BR
import com.mtc.R
import com.mtc.extension.dataBind

class PaymentInformationAdapter :
    RecyclerView.Adapter<PaymentInformationAdapter.PaymentViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaymentViewHolder {
        return PaymentViewHolder(
            dataBind<PaymentViewHolder>(
                parent,
                R.layout.payment_information_row
            )
        )
    }

    override fun onBindViewHolder(
        holder: PaymentViewHolder,
        position: Int
    ) {
        holder.bind(OrderListViewModel.orderListSelected .get(position))
    }

    override fun getItemCount(): Int = OrderListViewModel.orderListSelected .size

    class PaymentViewHolder(val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // region - Public function
        fun bind(image: OrderItem) {
            binding.setVariable(BR.viewModel, image)
//            binding.setVariable(BR.listener, onConfirmOrderClickListener)
        }
        // endregion
    }

}