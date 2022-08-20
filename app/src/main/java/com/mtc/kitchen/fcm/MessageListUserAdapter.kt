//package com.mtc.kitchen.fcm
//
//import android.app.Dialog
//import android.view.ViewGroup
//import androidx.databinding.ViewDataBinding
//import androidx.recyclerview.widget.RecyclerView
//import com.mtc.BR
//import com.mtc.R
//import com.mtc.extension.dataBind
//import com.mtc.kitchen.FragmentMessages
//import com.mtc.models.Message
//import com.mtc.models.OrderChoice
//import kotlinx.android.synthetic.main.dialog_choice.*
//import kotlinx.android.synthetic.main.fragment_message_row.view.*
//
//
//class MessageListUserAdapter(
//    private val messageList: ArrayList<Message>,
//    private val onClickChat: FragmentMessages.OnClickChat
//) :
//    RecyclerView.Adapter<MessageListUserAdapter.ItemViewHolder>() {
//
//
//    // region - Override function
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
//        return ItemViewHolder(dataBind<ItemViewHolder>(parent, R.layout.dialog_chat_user))
//    }
//
//    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
//        val message = messageList[position]
//        holder.bind(message)
////        holder.itemView.linearLayout.setOnClickListener {
////            onClickChat.onClickSingleTable(message)
////        }
//    }
//
//    override fun getItemCount(): Int {
//        return messageList.size
//    }
//
//    class ItemViewHolder(val binding: ViewDataBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        // region - Public function
//        fun bind(order: Message) {
//            binding.setVariable(BR.viewModel, order)
////            binding.setVariable(BR.listener, onDashboardClickListener)
//        }
//        // endregion
//    }
//
//    private fun setVisibility(dialog: Dialog, visible: Int) {
//        dialog.sideChoice.visibility = visible
//        dialog.recyclerViewDialogChoice.visibility = visible
//        dialog.sideChoiceMessage.visibility = visible
//    }
//
//    private fun getSamlpeList(mList: List<String>): ArrayList<OrderChoice> {
//
//        val arrayListOrder = ArrayList<OrderChoice>()
//
//        mList.forEach {
//            val orderChoice = OrderChoice(it, false)
//            arrayListOrder.add(orderChoice)
//        }
//        return arrayListOrder
//    }
//    // endregion
//}
//
