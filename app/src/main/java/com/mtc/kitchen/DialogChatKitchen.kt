//package com.mtc.kitchen
//
////import com.google.firebase.auth.FirebaseAuth
////import com.google.firebase.auth.ktx.auth
////import com.google.firebase.ktx.Firebase
//import android.app.Activity
//import android.app.Dialog
//import android.content.Context
//import android.content.DialogInterface
//import android.os.Bundle
//import android.util.DisplayMetrics
//import android.view.Gravity
//import android.view.View
//import android.view.Window
//import android.view.WindowManager
//import android.widget.Toast
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.airbnb.lottie.LottieDrawable
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener
//import com.mtc.R
//import com.mtc.api.APIConstant
//import com.mtc.models.Message
//import kotlinx.android.synthetic.main.dialog_chat.*
//
//class DialogChatKitchen : Dialog, View.OnClickListener {
//    private val senderId: String = "00000000000000"
//    private var messageList = ArrayList<Message>()
//    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
//    private lateinit var _activity: Activity
////    private var senderRoom: String = ""
////    private var receiverRoom: String = ""
//
//    constructor(activity: Activity) : super(activity) {
//        _activity = activity
//    }
//
//    constructor(context: Context, themeResId: Int) : super(context, themeResId)
//
//    constructor(
//        context: Context,
//        cancelable: Boolean,
//        cancelListener: DialogInterface.OnCancelListener?
//    ) : super(context, cancelable, cancelListener)
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.dialog_chat);
//        setWindow()
//        // setupAnim()
//
//        chatCross.setOnClickListener {
//            //auth.currentUser?.delete()
//            //auth.signOut()
//            // deleteAllChats()
//            this.dismiss()
//        }
//
//    }
//
//    private fun setupAnim() {
//        lottie_main.setAnimation(R.raw.chat_anim)
//        lottie_main.repeatCount = LottieDrawable.INFINITE
//        lottie_main.playAnimation()
//    }
//
////    private fun deleteAllChats() {
////        FirebaseDatabase.getInstance().reference.child(APIConstant.CHATS_ROOM)/*.child(senderRoom)*/
////            .child(APIConstant.MESSAGE_ROOM)
////            .addListenerForSingleValueEvent(object : ValueEventListener {
////                override fun onDataChange(snapshot: DataSnapshot) {
////                    for (PSnapshot in snapshot.children) {
////                        PSnapshot.ref.removeValue().addOnSuccessListener {
////                            FirebaseDatabase.getInstance().reference.child(APIConstant.CHATS_ROOM)
////                                /* .child(receiverRoom)*/
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
////
////                        }
////                    }
////                }
////
////                override fun onCancelled(error: DatabaseError) {
////                    TODO("Not yet implemented")
////                }
////            })
////    }
//
//    override fun onAttachedToWindow() {
//        super.onAttachedToWindow()
////        signInAnonymously()
//        setAdapter()
//    }
//
//    private fun setAdapter() {
//        progressBarWindow.visibility = View.GONE
//        chatWindow.visibility = View.VISIBLE
//
//        //setDefaultMessage()
//        val messageAdapter = DialogChatAdapterKitchen(context, messageList, senderId)
//        messageRecyclerView.adapter = messageAdapter
//        messageRecyclerView.setHasFixedSize(false)
//        val layoutManager = LinearLayoutManager(context)
//        messageRecyclerView.layoutManager = layoutManager
//        //val lineDivider = ContextCompat.getDrawable(context, R.drawable.line_divider)
//        // messageRecyclerView.addItemDecoration(SimpleDividerItemDecoration(context, lineDivider!!))
//
//
//        FirebaseDatabase.getInstance().reference.child(APIConstant.CHATS_ROOM)/*.child(receiverRoom)*/
//            .child(APIConstant.MESSAGE_ROOM).addValueEventListener(object : ValueEventListener {
//
//                override fun onDataChange(snapshot: DataSnapshot) {
//
//                    messageList.clear()
//                    for (postSnapshots in snapshot.children) {
//                        val message = postSnapshots.getValue(Message::class.java)
//                        messageList.add(message!!)
//                    }
//                    messageAdapter.notifyDataSetChanged()
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                }
//
//            })
//
//    }
//
////    private fun setDefaultMessage() {
////        val message = Message(context.getString(R.string.welcome_message), "0000000000000001")
////        messageList.add(message)
////    }
//
//    private fun setWindow() {
//        val displayMetrics = DisplayMetrics()
//        window?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
//        val lp = WindowManager.LayoutParams()
//        lp.copyFrom(window?.attributes)
//        val displayWidth = displayMetrics.widthPixels
//        val displayHeight = displayMetrics.heightPixels
//        val dialogWindowWidth = (displayWidth * 0.9f).toInt()
//        val dialogWindowHeight = (displayHeight * 0.9f).toInt()
//
//        lp.width = dialogWindowWidth
//        lp.height = dialogWindowHeight
//        lp.gravity = Gravity.NO_GRAVITY
//        window?.attributes = lp
//        this.setCancelable(false)
//
//        progressBarWindow.visibility = View.VISIBLE
//        chatWindow.visibility = View.GONE
//
//        sendButton.setOnClickListener(this)
//
//
//    }
//
////    private fun signInAnonymously() {
////
////        // [START signing_anonymously]
////        auth.signInAnonymously()
////            .addOnCompleteListener(_activity) { task ->
////                if (task.isSuccessful) {
////                    // Sign in success, update UI with the signed-in user's information
////                    Log.d("TAG", "signInAnonymously:success")
////                    val user = auth.currentUser
////                    Log.d("TAG", user?.uid.toString())
////                    senderRoom = senderId + user?.uid
////                    receiverRoom = user?.uid + senderId
////                    //senderId = user?.uid.toString()
////                    setAdapter()
////                } else {
////                    // If sign in fails, display a message to the user.
////                    Log.d("TAG", "signInAnonymously:failure", task.exception)
////                    Toast.makeText(
////                        context, "Authentication failed.",
////                        Toast.LENGTH_SHORT
////                    ).show()
////                    //updateUI(null)
////                }
////            }
////        // [END signin_anonymously]
////    }
//
//    override fun onClick(view: View?) {
//
//        if (messageEditText.text.trim().toString().isNotBlank()) {
//            //
//            val message =
//                Message(
//                    "Farandula Kitchen", messageEditText.text.trim().toString(), senderId,
//                    "", "", "", false,""
//                )
//            FirebaseDatabase.getInstance().reference.child(APIConstant.CHATS_ROOM)
//                /* .child(senderRoom)*/
//                .child(APIConstant.MESSAGE_ROOM)
//                .push().setValue(message).addOnSuccessListener {
//                    messageEditText.text.clear()
////                    FirebaseDatabase.getInstance().reference.child(APIConstant.CHATS_ROOM)
////                        .child(receiverRoom)
////                        .child(APIConstant.MESSAGE_ROOM)
////                        .push().setValue(message).addOnCompleteListener {
////                            messageEditText.text.clear()
////                        }
//                }//
//
//
//        } else {
//            Toast.makeText(
//                context,
//                context.getString(R.string.please_write_some_message),
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }
//
//
//}