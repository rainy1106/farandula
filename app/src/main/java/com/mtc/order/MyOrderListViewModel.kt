package com.mtc.order

import android.R
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mtc.api.APIConstant
import com.mtc.api.CommonApi
import com.mtc.general.BaseViewModel
import com.mtc.general.SharedPreference
import com.mtc.interfaces.EventHandler
import com.mtc.payment.FragmentPayment
import com.mtc.utils.NetworkUtility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject


class MyOrderListViewModel : BaseViewModel() {

    private var _totalCost: MutableLiveData<String> = MutableLiveData()
    val totalCost: LiveData<String> get() = _totalCost


    var _animationOne = MutableLiveData<Int>()
    val animationOne: LiveData<Int> get() = _animationOne

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

    fun callAddToCartAPI(context: Context, mEventHandler: EventHandler) {
        val commonApi = CommonApi()
        //  commonApi.addToCart(context, mEventHandler)
    }

    fun placeOrder(context: Context, mEventHandler: EventHandler) {
//        val commonApi = CommonApi()
        if (FragmentPayment.order_id.isEmpty()) {
            placeOrderIfEmpty(context, mEventHandler)
        } else {
            updateOrder(context, mEventHandler)
        }
    }

    fun placeOrderIfEmpty(context: Context, mEventHandler: EventHandler) {
        val urlLine: String = APIConstant().getApiBaseUrl(convertToProduct(context))
        CoroutineScope(Dispatchers.IO).launch {
            val rss = NetworkUtility.APIrequest(urlLine)
            withContext(Dispatchers.Main) {
                try {
                    getPlaceOrder(rss.toString(), mEventHandler)
                } catch (ex: Exception) {
                    Toast.makeText(context, "Network Issue , please retry!", Toast.LENGTH_SHORT)
                        .show()
                    //placeOrderIfEmpty(context, mEventHandler)
                }
            }
        }
    }

    fun updateOrder(context: Context, mEventHandler: EventHandler) {
        // commonApi.updateOrder(context, mEventHandler)
        val urlLine: String = APIConstant().getApiBaseUrl(convertToProductUpdate(context))
        CoroutineScope(Dispatchers.IO).launch {
            val rss = NetworkUtility.APIrequest(urlLine)
            withContext(Dispatchers.Main) {
                try {
                    getUpdateOrderJson(rss.toString(), mEventHandler)
                } catch (ex: Exception) {
                    Toast.makeText(context, "Network Issue , please retry!", Toast.LENGTH_SHORT)
                        .show()
                    // updateOrder(context, mEventHandler)
                }
            }
        }
    }

    private fun getUpdateOrderJson(response: String, mEventHandler: EventHandler) {
        val jsonObject = JSONObject(response)
        Log.v("Response is: ", jsonObject.toString())
        val statusB = jsonObject.getInt("status")
        if (statusB == 1) {
            // val data: JSONObject = jsonObject.getJSONObject("data")
            val result = jsonObject.getJSONObject("data")
            mEventHandler.onSuccess(result)
        } else {
            mEventHandler.onFailure(jsonObject.getString("message"))
        }
    }

    private fun convertToProductUpdate(context: Context): String {
//        val urlLine = APIConstant.ApiBaseUrl +
//                APIConstant.UPDATE_ORDER +
//                "order_id=${orderId}" +
//                "&product_id=ACCEPTED" +
//                "&quantity=" +
//                "&price=" +
//                "&subtotal"
        val arrayListProductId = ArrayList<String>()
        val arrayListProductQ = ArrayList<String>()
        val arrayListProductP = ArrayList<String>()
        val arrayListProductIns = ArrayList<String>()
        val arrayListProductExtra = ArrayList<String>()
        OrderListViewModel.orderListSelected.filter { it.isEnable }.forEach {
            arrayListProductId.add(it.product_id)
            arrayListProductQ.add(it.quatity)
            arrayListProductP.add(it.price.replace("$", ""))
            arrayListProductIns.add(it.instruction)
            arrayListProductExtra.add(it.extra_itemsA)
        }


        val UPDATE_ORDER =
            "update_order?order_id=${FragmentPayment.order_id}" +
                    "&product_id=" + arrayListProductId.joinToString(separator = ",") +
                    "&general_note=${OrderListViewModel.generalNote}" +
                    "&quantity=" + arrayListProductQ.joinToString(separator = ",") +
                    "&price=" + arrayListProductP.joinToString(separator = ",") +
                    "&sub_total=${FragmentPayment.placeOrder.grandTotal.trim()}" +
                    "&instractions=" + OrderListViewModel.instractions +
                    "&extra_items=" + arrayListProductExtra.joinToString(separator = ",") +//OrderListViewModel.extraItems.joinToString(separator = ",").trim() +
                    "&remark=" + arrayListProductIns.joinToString(separator = ",") +
                    "&action=update"

        Log.v("UPDATE_ORDER", UPDATE_ORDER)
        return UPDATE_ORDER
    }

    private fun getPlaceOrder(response: String, mEventHandler: EventHandler) {
        val jsonObject = JSONObject(response)
        Log.v("Response is: ", jsonObject.toString())
        val statusB = jsonObject.getBoolean("status")
        if (statusB) {
            val data: JSONObject = jsonObject.getJSONObject("data")
            val result = data.getJSONObject("result")
            //{"promocode":"","instractions":"","status":"PENDING","entrydt":"2022-06-14 13:18:23","txn_id":"abc","sub_total":"7","payment_mode":"card","seat_id":"1","txn_amount":"7","datetime":"2022-06-14 18:48:23","date":"2022-06-14","order_id":"5","discount":"0"}
            mEventHandler.onSuccess(result)
        } else {
            mEventHandler.onFailure(jsonObject.getString("message"))
        }
    }

    private fun convertToProduct(context: Context): String {

        val arrayListProductId = ArrayList<String>()
        val arrayListProductQ = ArrayList<String>()
        val arrayListProductP = ArrayList<String>()
        val arrayListProductIns = ArrayList<String>()
        val arrayListProductEX = ArrayList<String>()
        OrderListViewModel.orderListSelected.forEach {
            arrayListProductId.add(it.product_id)
            arrayListProductQ.add(it.quatity)
            arrayListProductP.add(it.price.replace("$", ""))
            arrayListProductIns.add(it.instruction)
            arrayListProductEX.add(it.extra_itemsA)
        }


        val PLACE_AN_ORDER =
            "place_an_order?restaurant_id=${SharedPreference.getRestaurantId(context)}" +
                    "&table_id=${SharedPreference.getTableId(context)}" +
                    "&seat_id=${SharedPreference.getSeatId(context)}" +
                    "&general_note=${OrderListViewModel.generalNote}" +
                    "&product_id=" + arrayListProductId.joinToString(separator = ",") +
                    "&quantity=" + arrayListProductQ.joinToString(separator = ",") +
                    "&price=" + arrayListProductP.joinToString(separator = ",") +
                    "&sub_total=${FragmentPayment.placeOrder.grandTotal.trim()}" +
                    "&discount=0&txn_amount=0&txn_id=abc&payment_mode=${FragmentPayment.placeOrder.paymentType}" +
                    "&instractions=" + OrderListViewModel.instractions +
                    "&remark=" + arrayListProductIns.joinToString(separator = ",") +
                    "&status=ACCEPTED&extra_items=" + arrayListProductEX.joinToString(separator = ",")//OrderListViewModel.extraItems.joinToString(separator = ",").trim() +


        Log.v("PLACE_AN_ORDER", PLACE_AN_ORDER)
        return PLACE_AN_ORDER
    }

    fun updateOrderList() {
        for (i in OrderListViewModel.orderListSelected.indices) {
            OrderListViewModel.orderListSelected[i].isEnable = false
            OrderListViewModel.orderListSelected[i].isAdded = false
        }
    }


}