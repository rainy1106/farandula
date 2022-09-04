package com.mtc.kitchen

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mtc.BR
import com.mtc.R
import com.mtc.api.APIConstant
import com.mtc.databinding.FragmentMessagesBinding
import com.mtc.dialogadapters.DialogChatAdapter
import com.mtc.general.BaseFragment
import com.mtc.general.hideKeyboard
import com.mtc.general.initViewModel
import com.mtc.models.Message
import kotlinx.android.synthetic.main.fragment_messages.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class FragmentMessages : BaseFragment<FragmentMessagesBinding, MessageViewModel>() {
    private lateinit var adapter: MessageListAdapter
    private var chattitle: String? = ""
    private var lastMessageUser: String? = ""
    private var selected_table_id: String? = ""
    private var selected_seat_id: String? = ""

    //private var connectionMode: ConnectionModel? = null

    private var messageListUser = ArrayList<Message>()

    companion object {
        var messageListTitle = ArrayList<Message>()
        val new_message = MutableLiveData<JSONObject>()
        fun newInstance() = FragmentMessages()
    }

    override fun onResume() {
        super.onResume()
//        ConnectionLiveData(requireContext()).observe {
//            connectionMode = it
//            restart(it)
//        }
        setupRecyclerView()
    }

//    private fun restart(connection: ConnectionModel) {
//        connection.also {
//            mViewModel.setNetworkState(it)
//            if (!connection.isConnected) {
//                mViewModel.showToast(getString(R.string.no_internet_connection), requireContext())
//            } else {
//                setupRecyclerView()
//            }
//        }
//     }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        HomeActivityKitchen._messageHome.postValue("blank")

        mDataBinding.sendButton.setOnClickListener {
//            if (messageListUser.size != null && messageListUser.size > 0)
            setKitchenData()
//            else
//                Toast.makeText(requireContext(), "No table request", Toast.LENGTH_SHORT).show()
        }
        mDataBinding.chatCross.setOnClickListener {
            mDataBinding.chatWindow.visibility = View.INVISIBLE
        }
        mDataBinding.chatTitleTextView.text = chattitle

        observer()

    }

    /**/
    private fun observer() {
        try {
            new_message.observe {
                var send_by = it.getString("send_by")
                var table_id = it.getString("table_id")
                messageListTitle.forEach { message ->
                    if (message.seatId == send_by && message.readChat) {
//                        message.readChat = true
                        updateMessage(message, true)
                    }
                }
                try {
                    if (adapter != null)
                        adapter.notifyDataSetChanged()
                } catch (ex: UninitializedPropertyAccessException) {

                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }//
    }


    /**/

    private fun setKitchenData() {


        if (messageEditTextKitchen.text.trim().toString().isNotBlank()) {
            //
            var messageFrom =
                ""//SharedPreference.getTableName(requireContext()) + "\t" + SharedPreference.getSeatName(requireContext())
            //this is done for kitchen
            if (messageFrom.isBlank())
                messageFrom = chattitle.toString()

            if (chattitle.toString().isBlank()&&chatTitleTextView.text.toString().isBlank()) {
                Toast.makeText(context, "Table name required!", Toast.LENGTH_SHORT).show()
                return
            }

            val message = Message(
                messageFrom, messageEditTextKitchen.text.trim().toString(), "00000000000000",
                selected_table_id!!,
                selected_seat_id!!, getCurrentTime(), false, getEpocTime()
            )

            FirebaseDatabase.getInstance().reference
                .child(APIConstant.CHATS_ROOM_NEW)
                .child(selected_table_id!!)
                .child(selected_seat_id!!)
                .child(message.pushKey!!)
                //.push()
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
//                        message.pushKey = snapshot.key
                        snapshot.ref.setValue(message)
                        try {
                            requireActivity().hideKeyboard()
                        } catch (ex: Exception) {
                            //no nothing
                        } finally {
                            if (messageEditTextKitchen != null)
                                messageEditTextKitchen.setText("")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.please_write_some_message),
                Toast.LENGTH_SHORT
            ).show()
        }

        if (messageListUser.isEmpty())
            setKitchenChatTitle()
    }

    private fun setupRecyclerView() {
//        if (mViewModel.isInternetConnected(requireContext())) {
        setKitchenChatTitle()
//        } else {
//            mViewModel.showToast(getString(R.string.no_internet_connection), requireContext())
//        }

    }

    private fun setKitchenChatTitle() {


        /*---------------------------------------------*/
        FirebaseDatabase.getInstance().reference.child(APIConstant.CHATS_ROOM_NEW)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.hasChildren()) {
                        messageListTitle.clear()
//                        var totalCount = 0
                        for (sp in snapshot.children) {
                            for (_sp in sp.children) {
                                for (s in _sp.children) {
                                    val message = s.getValue(Message::class.java)!!
//                                    if (!message.readChat) {
//                                        totalCount = totalCount.plus(1)
//                                    }
                                    // message.childrenCount = totalCount.toString()
                                    // _sp.childrenCount.toString()
                                    // if (message.senderId != "00000000000000")
                                    // setAnim(true)
                                    if (message.messageFrom?.trim()?.isNotBlank() == true) {
                                        messageListTitle.add(message)
                                        //lastMessageUser = message.message
                                    }
                                }
                            }

                        }
                        val filter: HashSet<Message> = HashSet<Message>(messageListTitle)
                        val _messageListTitle: ArrayList<Message> = ArrayList<Message>(filter)
                        adapter = MessageListAdapter(
                            _messageListTitle,
                            lastMessageUser,
                            object : OnClickChat {
                                override fun onClickSingleTable(message: Message) {
                                    //setAnim(false)
                                    //mViewModel.setMessageToTrue(message)
                                    message.readChat = false
                                    updateMessage(message, false)
                                    mDataBinding.chatWindow.visibility = View.VISIBLE
                                    chatTitleTextView.text = message.messageFrom
                                    refreshMessage(message)

                                }
                            })//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        adapter.notifyDataSetChanged()
                        mDataBinding.recyclerViewMessageList.setHasFixedSize(true)
                        mDataBinding.recyclerViewMessageList.visibility = View.VISIBLE
                        mDataBinding.noTextView.visibility = View.GONE
                        mDataBinding.recyclerViewMessageList.adapter = adapter
                        mDataBinding.recyclerViewMessageList.adapter?.let {
                            mDataBinding.recyclerViewMessageList.adapter?.notifyDataSetChanged()
                        }
                        //adapter.onDashboardClickListener = mViewModel
                        val layoutManager = LinearLayoutManager(activity)
                        mDataBinding.recyclerViewMessageList.layoutManager = layoutManager
//                        chatTitleTextView.text = messageListTitle[0].messageFrom
                        if (messageListTitle.size == 1)
                            refreshMessage(messageListTitle[0])
                    } else {
//                        mDataBinding.recyclerViewMessageList.visibility = GONE
//                        mDataBinding.noTextView.visibility = View.VISIBLE
//                        mDataBinding.chatWholeLayout.visibility = View.GONE
//                        mDataBinding.nochattextview.visibility = View.VISIBLE
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        /*---------------------------------------------*/


    }

    private fun updateMessage(message: Message, value: Boolean) {
//        FirebaseDatabase.getInstance().reference.child(APIConstant.CHATS_ROOM_NEW)
//            .child(message.tableId!!)
//            .child(message.seatId!!)
//            .child(message.pushKey!!).ref.setValue(message)
        FirebaseDatabase.getInstance().reference.child(APIConstant.CHATS_ROOM_NEW)
            .child(message.tableId!!)
            .child(message.seatId!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (sp in snapshot.children) {
                        val message = sp.getValue(Message::class.java)!!
                        message.readChat = value
                        sp.ref.setValue(message)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun refreshMessage(message: Message) {


        selected_table_id = message.tableId
        selected_seat_id = message.seatId

        FirebaseDatabase.getInstance().reference.child(APIConstant.CHATS_ROOM_NEW)
            .child(message.tableId!!)
            .child(message.seatId!!).addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    messageListUser.clear()
                    for (sp in snapshot.children) {
                        val message = sp.getValue(Message::class.java)
                        messageListUser.add(message!!)
                    }

                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    try {
                        val adapter =
                            DialogChatAdapter(requireActivity(), messageListUser, "00000000000000")
                        adapter.notifyDataSetChanged()
                        mDataBinding.recyclerViewMessageListUser.setHasFixedSize(true)
                        mDataBinding.recyclerViewMessageListUser.visibility = View.VISIBLE
                        mDataBinding.noTextView.visibility = View.GONE
                        mDataBinding.recyclerViewMessageListUser.adapter = adapter
                        mDataBinding.recyclerViewMessageListUser.adapter?.let {
                            mDataBinding.recyclerViewMessageListUser.adapter?.notifyDataSetChanged()
                        }
                        //adapter.onDashboardClickListener = mViewModel
                        val layoutManager = LinearLayoutManager(requireContext())
                        mDataBinding.recyclerViewMessageListUser.layoutManager = layoutManager
                        //hideLoader()
                        if (recyclerViewMessageListUser != null)
                            recyclerViewMessageListUser.scrollToPosition(messageListUser.size - 1)
                    } catch (ex: Exception) {
                        // hideLoader()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }


    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_messages

    override fun getViewModel(): MessageViewModel {
        return initViewModel {
            MessageViewModel()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.let {
            selected_seat_id = it.getString("selected_seat_id")
            selected_table_id = it.getString("selected_table_id")
            chattitle = it.getString("selected_table_name")
        }
    }

    private fun getEpocTime(): String {
        return Calendar.getInstance().timeInMillis.toString()
    }

    private fun getCurrentTime(): String {
        return SimpleDateFormat(
            "hh : mm a",
            Locale.getDefault()
        ).format(Calendar.getInstance().time).toString()
    }

    interface OnClickChat {
        fun onClickSingleTable(message: Message)
    }
}