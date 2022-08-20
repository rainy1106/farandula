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
import androidx.lifecycle.MutableLiveData
import com.airbnb.lottie.LottieDrawable
import com.google.firebase.auth.FirebaseAuth
import com.mtc.R
import com.mtc.models.Message
import com.mtc.order.FragmentConfirmOrder
import kotlinx.android.synthetic.main.dialog_placeorder.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DialogPlaceOrder : Dialog {

    private lateinit var _returnEvent: FragmentConfirmOrder.ReturnToFragment
    private var __animationOne = MutableLiveData<Int>()
    private var _value: Int = 0
    private val receiverID: String = "00000000000000"
    private var messageList = ArrayList<Message>()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var _activity: Activity

    //private var senderRoom: String = ""
    //private var receiverRoom: String = ""
    private var senderId: String = ""

    constructor(
        activity: Activity,
        value: Int,
        _animationOne: MutableLiveData<Int>,
        returnEvent: FragmentConfirmOrder.ReturnToFragment
    ) : super(
        activity
    ) {
        _activity = activity
        _value = value
        __animationOne = _animationOne
        _returnEvent = returnEvent
    }


    constructor(
        context: Context,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_placeorder)
        setWindow()
        try {
            setupAnim()
        } catch (ex: Exception) {
            _returnEvent.returnToFragment()
        }
    }

    private fun setupAnim() {

        try {
            lottie_main2.setAnimation(R.raw.placeordersuccess)
            lottie_main2.repeatCount = LottieDrawable.INFINITE
            lottie_main2.playAnimation()
        } catch (ex: Exception) {
            _returnEvent.returnToFragment()
        }


        try {
            lottie_main1.setAnimation(R.raw.placeorder)
            lottie_main1.repeatCount = LottieDrawable.INFINITE
            lottie_main1.playAnimation()
        }catch (ex :Exception){
            _returnEvent.returnToFragment()
        }


        if (_value == 1) {
            lottie_main1.visibility = View.VISIBLE
            lottie_main2.visibility = View.GONE
            GlobalScope.launch {
                delay(2000)
                dismiss()
                __animationOne.postValue(2)
            }
        } else if (_value == 2) {
            lottie_main1.visibility = View.GONE
            lottie_main2.visibility = View.VISIBLE
            GlobalScope.launch {
                delay(2000)
                dismiss()
                _returnEvent.returnToFragment()
            }
        }


    }


    private fun setWindow() {
        val displayMetrics = DisplayMetrics()
        window?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(window?.attributes)
        val displayWidth = displayMetrics.widthPixels
        val displayHeight = displayMetrics.heightPixels
        val dialogWindowWidth = (displayWidth * 0.8f).toInt()
        val dialogWindowHeight = (displayHeight * 0.8f).toInt()

        lp.width = dialogWindowWidth
        lp.height = dialogWindowHeight
        lp.gravity = Gravity.NO_GRAVITY
        window?.attributes = lp
        this.setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }


}