package com.mtc.kitchen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mtc.R
import com.mtc.models.Message
import kotlinx.android.synthetic.main.dialog_chat_row_receive.view.*
import kotlinx.android.synthetic.main.dialog_chat_row_send.view.*

class DialogChatAdapterKitchen(
    private var context: Context,
    private var arrayList: ArrayList<Message>,
    private var senderId: String
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var ITEM_SEND_TYPE = 1
    var ITEM_RECEIVE_TYPE = 2


    class SendItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sentMessage = itemView.findViewById<TextView>(R.id.messageTextViewEnd)

//        fun bind(image: Message) {
//            binding.setVariable(BR.viewModel, image)
//        }
    }

    class ReceiverItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var receiveMessage = itemView.findViewById<TextView>(R.id.messageTextViewStart)
//        fun bind(image: Message) {
//            binding.setVariable(BR.viewModel, image)
//        }
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)
        return if (senderId == arrayList.get(position).senderId)
            ITEM_SEND_TYPE
        else ITEM_RECEIVE_TYPE

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_chat_row_send, parent, false)
            SendItemViewHolder(view)
        } else{
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_chat_row_receive, parent, false)
            ReceiverItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val order = arrayList[position]
        if (holder.javaClass == SendItemViewHolder::class.java) {
            val viewHolder = holder as SendItemViewHolder
            viewHolder.itemView.messageTextViewEnd.text = order.message.toString()
        } else {
            val viewHolder = holder as ReceiverItemViewHolder
            viewHolder.itemView.messageTextViewStart.text = order.message.toString()
            if (order.messageFrom?.isEmpty()?.not() == true)
            {
                viewHolder.itemView.messageTextViewFrom.text = order.messageFrom.toString()
                viewHolder.itemView.messageTextViewFrom.visibility= View.VISIBLE
            }
            else viewHolder.itemView.messageTextViewFrom.visibility= View.GONE
        }

    }

    override fun getItemCount(): Int = arrayList.size
}