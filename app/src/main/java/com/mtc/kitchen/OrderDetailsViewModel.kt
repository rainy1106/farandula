package com.mtc.kitchen

import android.content.Context
import android.widget.Toast
import com.mtc.api.APIConstant
import com.mtc.general.BaseViewModel
import com.mtc.interfaces.EventHandler
import com.mtc.utils.NetworkUtility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class OrderDetailsViewModel : BaseViewModel() {

    // private var networkState: ConnectionModel? = null
    var arrayList = java.util.ArrayList<OrderListItem.Result>()

//    fun setNetworkState(connectionModel: ConnectionModel) {
//        networkState = connectionModel
//    }

    fun setList(arrayList: ArrayList<OrderListItem.Result>) {
        this.arrayList = arrayList
    }

    fun getTotalCost(list: OrderListItem.Result): Double {
        var totalCost = 0.0
        list.cart.forEach {
            val tempTotal = it.quantity.toInt().times(it.price.toDouble())
            totalCost = totalCost.plus(tempTotal)
        }
        return totalCost
    }

    fun updateOrderIsReady(requireContext: Context, orderId: String, mEventHandler: EventHandler) {
//            val commmonApi = CommonApi()
//            commmonApi.updateOrderIsReady(requireContext, orderId, mEventHandler)

        val urlLine = APIConstant.ApiBaseUrl +
                APIConstant.UPDATE_ORDER +
                "order_id=${orderId}" +
                "&status=READY"
        CoroutineScope(Dispatchers.IO).launch {
            val rss = NetworkUtility.APIrequest(urlLine)
            withContext(Dispatchers.Main) {
                try {
                    val _res = JSONObject(rss.toString()).getInt("status")
                    if (_res == 1) {
//                    val gson = Gson()
//                    val updateReponse =  gson.fromJson(rss.toString(), UpdateReponse.Response::class.java)
                        mEventHandler.onComplete()
                        mEventHandler.onSuccess()
                    } else {
                        mEventHandler.onFailure(JSONObject(rss.toString()).getString("message"))
                    }
                } catch (ex: Exception) {
                    mEventHandler.onFailure(ex.toString())
                    Toast.makeText(requireContext,"Network Error, Please try again...",Toast.LENGTH_SHORT).show()
                    //updateOrderIsReady(requireContext, orderId, mEventHandler)
                }
            }
        }

    }

    fun updateOrderAccept(requireContext: Context, orderId: String, mEventHandler: EventHandler) {
//        val commmonApi = CommonApi()
//        commmonApi.updateOrderAccept(requireContext, orderId, mEventHandler)

        val urlLine = APIConstant.ApiBaseUrl +
                APIConstant.UPDATE_ORDER +
                "order_id=${orderId}" +
                "&status=ACCEPTED"
        CoroutineScope(Dispatchers.IO).launch {
            val rss = NetworkUtility.APIrequest(urlLine)
            withContext(Dispatchers.Main) {
                try {
                    val _res = JSONObject(rss.toString()).getInt("status")
                    if (_res == 1) {
//                    val gson = Gson()
                        //val updateReponse =  gson.fromJson(response, UpdateReponse.Response::class.java)
                        mEventHandler.onComplete()
                        mEventHandler.onSuccess()
                    } else {
                        mEventHandler.onFailure(JSONObject(rss.toString()).getString("message"))
                    }
                } catch (ex: Exception) {
                    Toast.makeText(requireContext,"Network Error , please try again...",Toast.LENGTH_SHORT).show()
                    //updateOrderAccept(requireContext, orderId, mEventHandler)
                }
            }
        }
    }


//    fun getOrderDetails(context: Context, onResponseAPI: onResponseAPI) {
//        val commonApi = CommonApi()
//        commonApi.getOrderDetails(context, onResponseAPI)
//    }
}