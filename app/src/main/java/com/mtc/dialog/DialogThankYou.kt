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
import android.view.Window
import android.view.WindowManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mtc.R
import com.mtc.general.BaseViewModel
import com.mtc.general.SharedPreference
import com.mtc.kitchen.OrdersViewModel
import com.mtc.order.OrderListViewModel
import com.mtc.payment.FragmentPayment
import com.mtc.payment.Seats
import kotlinx.android.synthetic.main.dialog_thankyou.*


class DialogThankYou : Dialog {

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
        setContentView(R.layout.dialog_thankyou)
        setWindow()
        setCancelable(false)
        clearApp(/*context*/)
        closeId.setOnClickListener {
            dismiss()
            OrdersViewModel.showDialog.postValue(false)
            showFeedbackDialog()
        }
    }

    fun clearApp(/*requireContext: Context*/) {
        //clear(requireContext)
        DialogChatUser.senderId = ""
        OrderListViewModel.orderListSelected.clear()
//        OrderListViewModel.extraItems.clear()
        OrderListViewModel.instractions = ""
        FragmentPayment.costToAdd = ""
        FragmentPayment.calculatedValue = 0.0
        FragmentPayment.placeOrder.paymentType = ""
        FragmentPayment.placeOrder.grandTotal = "0"
        FragmentPayment.placeOrder.promoCode = ""

        try{
            BaseViewModel.reportHomeButton()
        }catch (e : Exception){
            e.printStackTrace()
        }
        clearFirebase()
    }

    private fun setWindow() {
        val displayMetrics = DisplayMetrics()
        window?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(window?.attributes)
        val displayWidth = displayMetrics.widthPixels
        val displayHeight = displayMetrics.heightPixels
        val dialogWindowWidth = (displayWidth * 0.6f).toInt()
        val dialogWindowHeight = (displayHeight * 0.6f).toInt()

        lp.width = dialogWindowWidth
        lp.height = dialogWindowHeight
        lp.gravity = Gravity.NO_GRAVITY
        window?.attributes = lp

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    /**/
    private fun showFeedbackDialog() {
        val dialog = DialogFeedback(context)
        dialog.show()
    }
    /**/
    private fun clearFirebase() {
        /*table*/
        FirebaseDatabase.getInstance().reference.child(SharedPreference.getTableId(context)!!)
            .child(SharedPreference.getSeatId(context)!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (sn in snapshot.children) {
                        val _selectedSeat = sn.getValue(Seats::class.java)
                        if (_selectedSeat?.seat_id == SharedPreference.getSeatId(context))
                            snapshot.ref.removeValue { error, ref ->
                                Log.v("Ref", ref.toString())
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        /* chat particular*/
//        FirebaseDatabase.getInstance().reference.child(APIConstant.CHATS_ROOM_NEW)
//            .child(SharedPreference.getTableId(context)!!).ref.removeValue { error, ref -> Log.v("Ref", ref.toString()) }
//            .child(SharedPreference.getSeatId(context)!!)
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    for (sn in snapshot.children) {
//                        val _selectedSeat = sn.getValue(Message::class.java)
//                        //if (_selectedSeat?.pushKey == sn.key)
//                        sn.ref.removeValue { error, ref ->
//                            Log.v("Ref", ref.toString())
//                        }
//                    }
//
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//
//            })
    }
}