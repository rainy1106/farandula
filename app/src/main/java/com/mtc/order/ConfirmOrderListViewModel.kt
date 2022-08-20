package com.mtc.order

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mtc.dialog.DialogChatUser
import com.mtc.general.BaseViewModel

class ConfirmOrderListViewModel : BaseViewModel() {
    // [START declare_auth]
    private var _totalCost: MutableLiveData<String> = MutableLiveData()
    val totalCost: LiveData<String> get() = _totalCost

    // [END declare_auth]

    /**/
    fun updateTotalCost() {
        var total: Double = 0.0
        OrderListViewModel.orderListSelected.forEach {
            val tempTotal = it.quatity.toInt().times(it.price.replace("$", " ").trim().toDouble())
            total = total.plus(tempTotal)
        }
        // _totalCost.value = "$ " + total.roundToLong().toString()
        _totalCost.value = "$ " + BaseViewModel().roundOffDecimal(total)

    }


    fun goToChat(activity: Activity) {
        val dialogChat = DialogChatUser(activity)
        if (!dialogChat.isShowing)
        dialogChat.show()
    }
}