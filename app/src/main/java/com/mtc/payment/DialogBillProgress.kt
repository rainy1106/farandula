package com.mtc.payment

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.MutableLiveData
import com.airbnb.lottie.LottieDrawable
import com.mtc.R
import kotlinx.android.synthetic.main.dialog_bill_progress.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DialogBillProgress : Dialog {

    private var __animationOne = MutableLiveData<Int>()
    private var _activity = Activity()

    constructor(activity: Activity,_animationOne: MutableLiveData<Int>) : super(activity) {
        _activity = activity
        __animationOne = _animationOne
    }

    constructor(context: Context) : super(context)


    constructor(
        context: Context,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_bill_progress)
        setWindow()
        setCancelable(true)
        try {
            lottie_main_bill.setAnimation(R.raw.bill)
            lottie_main_bill.repeatCount = LottieDrawable.INFINITE
            lottie_main_bill.playAnimation()
            GlobalScope.launch {
                delay(2000)
                dismiss()
            }
        } catch (ex: Exception) {
           //
        }
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
}