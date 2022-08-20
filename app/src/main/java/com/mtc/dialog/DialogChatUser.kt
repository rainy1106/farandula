package com.mtc.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieDrawable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mtc.R
import com.mtc.api.APIConstant
import com.mtc.dialogadapters.DialogChatAdapter
import com.mtc.general.SharedPreference
import com.mtc.general.hideKeyboard
import com.mtc.models.Message
import com.mtc.payment.FragmentPayment
import com.mtc.utils.NetworkUtility
import kotlinx.android.synthetic.main.dialog_chat_user.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class DialogChatUser : Dialog, View.OnClickListener {

    private val receiverID: String = "00000000000000"

    private var messageList = ArrayList<Message>()

    private lateinit var _activity: Activity

    //private var senderRoom: String = ""
    //private var receiverRoom: String = ""
    companion object {
        var senderId: String = ""
    }

    constructor(activity: Activity) : super(activity) {
        _activity = activity
    }

    constructor(context: Context, themeResId: Int) : super(context, themeResId)

    constructor(
        context: Context,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_chat_user)
        setWindow()
        setupAnim()

        chatCross.setOnClickListener {
//            auth.currentUser?.delete()
//            auth.signOut()
//            deleteAllChats()
            this.dismiss()
        }

    }

    private fun setupAnim() {
        try {
            lottie_main.setAnimation(R.raw.chatanim)
            lottie_main.repeatCount = LottieDrawable.INFINITE
            lottie_main.playAnimation()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

//    private fun deleteAllChats() {
//        FirebaseDatabase.getInstance().reference.child(APIConstant.CHATS_ROOM)/*.child(senderRoom)*/
//            .child(APIConstant.MESSAGE_ROOM)
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    for (PSnapshot in snapshot.children) {
//                        PSnapshot.ref.removeValue().addOnSuccessListener {
////                            FirebaseDatabase.getInstance().reference.child(APIConstant.CHATS_ROOM)
////                                .child(receiverRoom)
////                                .child(APIConstant.MESSAGE_ROOM)
////                                .addListenerForSingleValueEvent(object : ValueEventListener {
////                                    override fun onDataChange(snapshot: DataSnapshot) {
////                                        for (P1Snapshot in snapshot.children) {
////                                            P1Snapshot.ref.removeValue()
////                                        }
////                                    }
////
////                                    override fun onCancelled(error: DatabaseError) {
////                                        TODO("Not yet implemented")
////                                    }
////                                })
//
//                        }
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//            })
//    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setAdapter()
    }

    private fun setAdapter() {
        progressBarWindow.visibility = View.GONE
        chatWindow.visibility = View.VISIBLE
        val messageAdapter = DialogChatAdapter(context, messageList, senderId)
        messageRecyclerView.adapter = messageAdapter
        messageRecyclerView.setHasFixedSize(false)
        val layoutManager = LinearLayoutManager(context)
        messageRecyclerView.layoutManager = layoutManager

        FirebaseDatabase.getInstance().reference.child(APIConstant.CHATS_ROOM_NEW)
            .child(SharedPreference.getTableId(_activity)!!)
            .child(SharedPreference.getSeatId(_activity)!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    if (snapshot.value != null) {
                        for (postSnapshots in snapshot.children) {
                            val message = postSnapshots.getValue(Message::class.java)
                            messageList.add(message!!)
                        }
                    }
                    try {
                        _activity.hideKeyboard()
                    } catch (ex: Exception) {
                        //no nothing
                    } finally {
                        messageEditText.text = null
                        messageAdapter.notifyDataSetChanged()
                        messageRecyclerView.scrollToPosition(messageList.size - 1)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                }

            })

    }


//    private fun setDefaultMessage() {
//        val message = Message("", context.getString(R.string.welcome_message), "0000000000000001")
//        messageList.add(message)
//    }

    private fun setWindow() {
        val displayMetrics = DisplayMetrics()
        window?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(window?.attributes)
        val displayWidth = displayMetrics.widthPixels
        val displayHeight = displayMetrics.heightPixels
        val dialogWindowWidth = (displayWidth * 0.9f).toInt()
        val dialogWindowHeight = (displayHeight * 0.9f).toInt()

        lp.width = dialogWindowWidth
        lp.height = dialogWindowHeight
        lp.gravity = Gravity.NO_GRAVITY
        window?.attributes = lp
        this.setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        progressBarWindow.visibility = View.VISIBLE
        chatWindow.visibility = View.GONE

        sendButton.setOnClickListener(this)


    }


    override fun onClick(view: View?) {

        if (!this.isShowing)
            this.show()

        if (messageEditText.text.trim().toString().isNotBlank()) {
            //
            val messageFrom =
                SharedPreference.getTableName(context) + "\t" + SharedPreference.getSeatName(context)
            val message = Message(
                messageFrom, messageEditText.text.trim().toString(), senderId,
                SharedPreference.getTableId(context)!!,
                SharedPreference.getSeatId(context)!!,
                getCurrentTime(), true, getEpocTime()
            )

            FirebaseDatabase.getInstance().reference
                .child(APIConstant.CHATS_ROOM_NEW)
                .child(SharedPreference.getTableId(_activity)!!)
                .child(SharedPreference.getSeatId(_activity)!!)
                .child(message.pushKey!!)
                //.push()
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        //message.pushKey = push// snapshot.key
                        snapshot.ref.setValue(message).addOnCompleteListener {
                            callSendNotificationApi()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })

//            FirebaseDatabase.getInstance().reference.child(APIConstant.CHATS_ROOM)
//                /*  .child(senderRoom)*/
//                .child(APIConstant.MESSAGE_ROOM)
//                .push().setValue(message).addOnSuccessListener {
//                    messageEditText.text.clear()
//                }//


        } else {
            Toast.makeText(
                context,
                context.getString(R.string.please_write_some_message),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /*-----------------------------------*/
    private fun callSendNotificationApi() {
        //https://codezens.com/farandula/index.php/api/send_notification?restaurant_id=1
        // &seat_id=1&order_id=1&title=CLEAR&message=to%20clear&sent_to_kitchen=1&sent_to_user=0
        val urlLine = APIConstant().getApiBaseUrl(
            "send_notification?restaurant_id=${SharedPreference.getRestaurantId(_activity)}" +
                    "&seat_id=${SharedPreference.getSeatId(_activity)}" +
                    "&order_id=${FragmentPayment.order_id}" +
                    "&title=MESSAGE" +
                    "&message=NEW MESSAGE RECEIVED FROM USER" +
                    "&sent_to_kitchen=1&sent_to_user=0"
        )
        CoroutineScope(Dispatchers.IO).launch {
            val rss = NetworkUtility.APIrequest(urlLine)
            //
        }

    }

    private fun getCurrentTime(): String {
        return SimpleDateFormat(
            "hh : mm a",
            Locale.getDefault()
        ).format(Calendar.getInstance().time).toString()
    }

    private fun getEpocTime(): String {
        return Calendar.getInstance().timeInMillis.toString()
    }
}