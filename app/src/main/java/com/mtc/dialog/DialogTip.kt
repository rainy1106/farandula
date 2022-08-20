package com.mtc.dialog

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
import android.widget.Toast
import com.mtc.R
import com.mtc.general.BaseViewModel
import com.mtc.payment.FragmentPayment
import kotlinx.android.synthetic.main.dialog_tip.*


class DialogTip : Dialog {

    companion object {
        var lastValue: Int = 0
    }

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
        setContentView(R.layout.dialog_tip)
        setWindow()
        setCancelable(false)
        closeIdTip.setOnClickListener {
            dismiss()
        }
        doneId.setOnClickListener {
            val value = editTextTip.text.toString()
            if (value.isEmpty() || value == "0") {
                Toast.makeText(context, "Add some tip", Toast.LENGTH_SHORT).show()
                FragmentPayment._addTip.value = "Add Tip"
            } else if (value.toInt() > 0) {
                lastValue = value.toInt()
//                BaseViewModel._reportHomeButton.value =
//                    FragmentPayment.placeOrder.grandTotal.toDouble().plus(value.toInt()).toString()
                FragmentPayment._addTip.value = "Remove Tip"
            }

            dismiss()
        }
    }


    private fun setWindow() {
        val displayMetrics = DisplayMetrics()
        window?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(window?.attributes)
        val displayWidth = displayMetrics.widthPixels
        val displayHeight = displayMetrics.heightPixels
        val dialogWindowWidth = (displayWidth * 0.7f).toInt()
        val dialogWindowHeight = (displayHeight * 0.7f).toInt()

        lp.width = dialogWindowWidth
        lp.height = dialogWindowHeight
        lp.gravity = Gravity.NO_GRAVITY
        window?.attributes = lp

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


}