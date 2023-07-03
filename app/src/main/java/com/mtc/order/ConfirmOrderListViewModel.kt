package com.mtc.order

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.mtc.api.APIConstant
import com.mtc.dialog.DialogChatUser
import com.mtc.general.BaseViewModel
import com.mtc.general.SharedPreference
import com.mtc.interfaces.onResponseAPI
import com.mtc.kitchen.FragmentOrderDetails
import com.mtc.kitchen.OrderHistory
import com.mtc.kitchen.OrderListItem
import com.mtc.payment.FragmentPayment.Companion.order_id
import com.mtc.utils.NetworkUtility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.ArrayList
import java.util.EventListener

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

    fun getOrderDetails(context: Context, orderId: String, onResponseAPI: onResponseAPI) {
        val urlLine =
            APIConstant.ApiBaseUrl + APIConstant.GET_ORDERS +
                    "restaurant_id=${SharedPreference.getRestaurantKitchen(context)}" +
                    "&order_id=${orderId}"
        CoroutineScope(Dispatchers.IO).launch {
            val rss = NetworkUtility.APIrequest(urlLine)
            withContext(Dispatchers.Main) {
                try {
                    val arrayList = getOrderJson(rss.toString())
                    onResponseAPI.onSuccessKitchenOrderListItemRow(arrayList[0])
                } catch (ex: Exception) {
                    onResponseAPI.onFailure()
                    Toast.makeText(context, "Network Error, Please try again..", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    fun getOrderJson(response: String): ArrayList<OrderListItem.Result> {
        val orderJson = ArrayList<OrderListItem.Result>()
        val res = JSONObject(response).getBoolean("status")
        return if (res) {
            val gson = Gson()
            val orderListItem =
                gson.fromJson(response, OrderListItem.Response::class.java)
            if (orderListItem.status) {
                orderListItem.data.result

            } else orderJson//onResponseAPI.onFailure()
        } else {
            orderJson
            // onResponseAPI.onFailure()
        }
        Log.v("Response is: ", response)
    }
}