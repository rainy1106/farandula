package com.mtc.kitchen

import android.app.Dialog
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.mtc.BR
import com.mtc.R
import com.mtc.extension.dataBind
import com.mtc.models.Message
import com.mtc.models.OrderChoice
import kotlinx.android.synthetic.main.dialog_choice.*
import kotlinx.android.synthetic.main.fragment_message_row.view.*


class MessageListAdapter(
    private val messageList: ArrayList<Message>,
    private val lastMessageUser: String?,
    private val onClickChat: FragmentMessages.OnClickChat
) : RecyclerView.Adapter<MessageListAdapter.ItemViewHolder>() {


    // region - Override function
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(dataBind(parent, R.layout.fragment_message_row))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val message = messageList[position]
        holder.bind(message)

        if (!message.readChat) {
            holder.itemView.countChat.visibility = View.INVISIBLE
        } else {
            message.readChat = false
            holder.itemView.countChat.visibility = View.VISIBLE
        }

        //holder.itemView.countChat.text = message.childrenCount
        holder.itemView.lastmessage.text = lastMessageUser
        holder.itemView.linearLayout.setOnClickListener {
            holder.itemView.countChat.visibility = View.INVISIBLE
            onClickChat.onClickSingleTable(message)
        }
    }


    override fun getItemCount(): Int {
        return messageList.size
    }

    class ItemViewHolder(val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // region - Public function
        fun bind(message: Message) {
            binding.setVariable(BR.viewModel, message)
//            binding.setVariable(BR.listener, onDashboardClickListener)
        }
        // endregion
    }

    private fun setVisibility(dialog: Dialog, visible: Int) {
        dialog.sideChoice.visibility = visible
        dialog.recyclerViewDialogChoice.visibility = visible
        dialog.sideChoiceMessage.visibility = visible
    }

    private fun getSamlpeList(mList: List<String>): ArrayList<OrderChoice> {

        val arrayListOrder = ArrayList<OrderChoice>()

        mList.forEach {
            val orderChoice = OrderChoice(it, false)
            arrayListOrder.add(orderChoice)
        }
        return arrayListOrder
    }
    // endregion
}

