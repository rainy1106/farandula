package com.mtc.payment

import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.mtc.BR
import com.mtc.R
import com.mtc.extension.dataBind
import com.mtc.interfaces.EventHandler
import kotlinx.android.synthetic.main.payment_option_view_row.view.*

class PaymentTypeAdapter(
    var paymentOptionsList: ArrayList<PaymentType>,
    var eventHandler: EventHandler
) :
    RecyclerView.Adapter<PaymentTypeAdapter.PaymentTypeViewHolder>() {

    var selectedItemPos = 1
    var lastItemSelectedPos = 1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaymentTypeViewHolder {
        return PaymentTypeViewHolder(
            dataBind(
                parent,
                R.layout.payment_option_view_row
            )
        )
    }

    override fun onBindViewHolder(
        holder: PaymentTypeViewHolder,
        position: Int
    ) {

        val paymentType = paymentOptionsList[position]
        holder.bind(paymentType)
//        Picasso.with(holder.itemView.context).load(paymentType.resource)
//            .into(holder.itemView.choosePaymentMaster)

        if (position == selectedItemPos)
            holder.selectedBg(holder.itemView,paymentType)
        else
            holder.defaultBg(holder.itemView,paymentType)

        holder.itemView.setOnClickListener {
            selectedItemPos = position
            if (lastItemSelectedPos == -1)
                lastItemSelectedPos = selectedItemPos
            else {
                notifyItemChanged(lastItemSelectedPos)
                lastItemSelectedPos = selectedItemPos
            }
            notifyItemChanged(selectedItemPos)
            eventHandler.onClickPaymentType(holder.itemView.choosePaymentText.text)
        }


    }


    override fun getItemCount(): Int = paymentOptionsList.size

    class PaymentTypeViewHolder(val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // region - Public function
        fun bind(image: PaymentType) {
            binding.setVariable(BR.viewModel, image)
//            binding.setVariable(BR.listener, onConfirmOrderClickListener)
        }

        fun defaultBg(view: View, paymentType: PaymentType) {
            view.choosePaymentText.setTextColor(view.context.resources.getColor(R.color.black))
            view.freeLayout.setBackgroundResource(R.drawable.round_background_black_border)
            view.choosePaymentMaster.setImageDrawable(paymentType.resource)

        }

        fun selectedBg(view: View, paymentType: PaymentType) {
            view.choosePaymentText.setTextColor(view.context.resources.getColor(R.color.white))
            view.freeLayout.setBackgroundResource(R.drawable.round_background_black)
            view.choosePaymentMaster.setImageDrawable(paymentType.resource)
        }
        // endregion
    }
}