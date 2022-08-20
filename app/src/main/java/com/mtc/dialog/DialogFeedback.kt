package com.mtc.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mtc.R
import com.mtc.api.APIConstant
import com.mtc.api.CommonApi
import com.mtc.general.SharedPreference
import com.mtc.interfaces.EventHandler
import com.mtc.order.OrderListViewModel
import com.mtc.payment.FragmentPayment
import com.mtc.payment.Seats
import kotlinx.android.synthetic.main.dialog_feedbcak.*


class DialogFeedback : Dialog {

    val onSuccess: LiveData<Boolean> get() = _onSuccess
    var _onSuccess: MutableLiveData<Boolean> = MutableLiveData()

    constructor(context: Context) : super(context)

    constructor(context: Context, themeResId: Int) : super(context, themeResId)

    constructor(
        context: Context,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_feedbcak)


        //val view = this.currentFocus
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)


        val displayMetrics = DisplayMetrics()
        window?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(window?.attributes)
        val displayWidth = displayMetrics.widthPixels
        val displayHeight = displayMetrics.heightPixels
        val dialogWindowWidth = (displayWidth * 0.8f).toInt()
        val dialogWindowHeight =  WindowManager.LayoutParams.WRAP_CONTENT //(displayHeight * 1.0f).toInt()

        lp.width = dialogWindowWidth
        lp.height = dialogWindowHeight
        lp.gravity = Gravity.CENTER
        window?.attributes = lp

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        feedbackSubmit.setOnClickListener {
            dismiss()
            finish()
        }
        dismissId.setOnClickListener {
            dismiss()
            finish()
        }
    }

    private fun finish() {

        sendFeedBackToServer(editText.text.trim().toString(), ratingBar.rating.toString())
    }

    private fun sendFeedBackToServer(mFeedBack: String, mRating: String) {
        try {
            val commonApi = CommonApi()
            commonApi.sendFeedBackToServer(context, mFeedBack, mRating, object : EventHandler {
                override fun onComplete() {
                    super.onComplete()
                    OrderListViewModel.orderListSelected.clear()
                    SharedPreference.setCatId(context, "1")
                    FragmentPayment.order_id = ""
                    // SharedPreference.clear(context)
//                    val intent = Intent(context, HomeActivity::class.java)
//                    intent.flags =
//                        Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_NEW_TASK
//                    context.startActivity(intent)
//                    clearFirebase()
                }

                override fun onSuccess(message: String) {
                    super.onSuccess(message)
//                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }

            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }


    }

    private fun clearFirebase() {
        /*table*/
        FirebaseDatabase.getInstance().reference.child(SharedPreference.getTableId(context)!!)
            .child(SharedPreference.getSeatId(context)!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (sn in snapshot.children) {
                        val _selectedSeat = sn.getValue(Seats::class.java)
                        if (_selectedSeat?.seat_id == SharedPreference.getSeatId(context))
                            sn.ref.removeValue { error, ref ->
                                Log.v("Ref", ref.toString())
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        /* chat*/
        FirebaseDatabase.getInstance().reference.child(APIConstant.CHATS_ROOM_NEW)
            .child(SharedPreference.getTableId(context)!!)
            .child(SharedPreference.getSeatId(context)!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (sn in snapshot.children) {
//                        val _selectedSeat = sn.getValue(Message::class.java)
//                        if (_selectedSeat?.pushKey == sn.key)
                        sn.ref.removeValue { error, ref ->
                            Log.v("Ref", ref.toString())
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }


}