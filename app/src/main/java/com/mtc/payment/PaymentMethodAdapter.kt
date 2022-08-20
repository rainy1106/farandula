//package com.mtc.Payment
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import android.widget.Toast
//import androidx.recyclerview.widget.RecyclerView
//import com.mtc.R
//
//
//class PaymentMethodAdapter(var paymentMethodList: ArrayList<PaymentMethod>) :
//
//    RecyclerView.Adapter<PaymentMethodAdapter.PaymentViewHolder>() {
//    lateinit var context: Context
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
//        context = parent.context
//        val inflater = LayoutInflater.from(context)
//        val paymentView: View = inflater.inflate(R.layout.choose_payment_row, parent, false)
//        return PaymentViewHolder(paymentView)
//    }
//
//    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
//        holder.bind(paymentMethodList[position])
//        holder.payment.setOnClickListener {
//            if (holder.choosePaymentID.text.equals(context.getString(R.string.master_card))) {
//                Toast.makeText(context, "master_card", Toast.LENGTH_SHORT).show()
//            } else if (holder.choosePaymentID.text.equals(context.getString(R.string.cash_on_delivery))) {
//                Toast.makeText(context, "cash_on_delivery", Toast.LENGTH_SHORT).show()
//            } else if (holder.choosePaymentID.text.equals(context.getString(R.string.paypal))) {
//                Toast.makeText(context, "paypal", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    override fun getItemCount(): Int = paymentMethodList.size
//
//    class PaymentViewHolder(itemView: View) :
//        RecyclerView.ViewHolder(itemView) {
//        val payment: TextView = itemView.findViewById(R.id.payment)
//        val choosePaymentID: TextView = itemView.findViewById(R.id.choosePaymentID)
//        val choosePaymentImageId: ImageView = itemView.findViewById(R.id.choosePaymentImageId)
//
//        fun bind(payment: PaymentMethod) {
//            choosePaymentID.text = payment.name
//            choosePaymentImageId.setImageDrawable(payment.image)
//        }
//
//
//    }
//}