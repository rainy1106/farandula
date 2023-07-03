package com.mtc.api

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.mtc.general.SharedPreference
import com.mtc.kitchen.OrderListItem
import com.mtc.order.OrderItem
import com.mtc.utils.NetworkUtility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class BaseRepository {

    companion object {
        lateinit var instance: BaseRepository
    }

    init {
        instance = this
    }


    fun getProductList(context: Context, _mList: MutableLiveData<ArrayList<OrderItem>>) {
//        val commonApi = CommonApi()
//        return commonApi.getProductList(context,/* onResponseAPI,*/_mList,_mListF)
        val restaurantId = SharedPreference.getRestaurantId(context)
        val categoryId = SharedPreference.getCatId(context)
        val _url = "get_products?restaurant_id=${restaurantId}&category_id=${categoryId}"
        val urlLine: String = APIConstant().getApiBaseUrl(_url)
        CoroutineScope(Dispatchers.IO).launch {
            val rss = NetworkUtility.APIrequest(urlLine)
            withContext(Dispatchers.Main) {
                try {
                    val cat = getProductListJson(rss.toString())
                    _mList.value = cat
                } catch (ex: Exception) {
                    Toast.makeText(context, "Network Error, Please try again..", Toast.LENGTH_SHORT)
                        .show()
                    //getProductList(context, _mList)
                }
            }
        }
    }

    private fun getProductListJson(response: String): ArrayList<OrderItem> {
        val arrayList: ArrayList<OrderItem> = ArrayList<OrderItem>()
        val jsonObject = JSONObject(response)
        try {
            val statusB = jsonObject.getBoolean("status")
            if (statusB) {
                val data: JSONObject = jsonObject.getJSONObject("data")
                val resultArray = data.getJSONArray("result")
                if (resultArray.length() > 0) {

                    for (i in 0 until resultArray.length()) {
                        val product = resultArray.getJSONObject(i)
                        val product_id = product.getString("product_id")
                        val restaurant_id = product.getString("restaurant_id")
                        val category_id = product.getString("category_id")
                        val product_name = product.getString("product_name")
                        val price = "$" + product.getString("price")
                        val description = product.getString("description")
                        val image = product.getString("image")
                        val status = product.getString("status")
                        val entrydt = product.getString("entrydt")
                        val extra_itemsO = product.getString("extra_items")

                        val productList = OrderItem(
                            product_id,
                            restaurant_id,
                            category_id,
                            product_name,
                            price,
                            description,
                            image,
                            status,
                            entrydt,
                            false, true,
                            "1", extra_itemsO, "", ""
                        )
                        arrayList.add(productList)
                    }
                    return arrayList
                    //onResponseAPI?.onSuccess(arrayList)
                }
            }
        } catch (ex: Exception) {
            return arrayList
        }
        Log.v("Response is: ", response.toString())
        return arrayList
    }

    fun getOrders(
        context: Context,
        productList: MutableLiveData<ArrayList<OrderListItem.Result>>,
        updateBadgeCount: MutableLiveData<ArrayList<OrderListItem.Result>>
    ) {
        //val commonApi = CommonApi()
        //commonApi.getOrders(context, onResponseAPI)
        val restaurantId = SharedPreference.getRestaurantKitchen(context)
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())
        val urlPath =
            "get_order?restaurant_id=${restaurantId}&status=ACCEPTED&date=${currentDate}"
        val urlLine: String = APIConstant().getApiBaseUrl(urlPath)
        CoroutineScope(Dispatchers.IO).launch {
            val rss = NetworkUtility.APIrequest(urlLine)
            withContext(Dispatchers.Main) {
                try {
                    val arrayList = getOrderJson(rss.toString())
                    productList.value = arrayList
                    updateBadgeCount.value = arrayList
                } catch (ex: Exception) {
                    Toast.makeText(context, "Network Error, please try again..", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    //
    fun getOrderJson(response: String): ArrayList<OrderListItem.Result> {
        val orderJson = ArrayList<OrderListItem.Result>()
        val res = JSONObject(response).getBoolean("status")
        if (res) {
            val gson = Gson()
            val orderListItem =
                gson.fromJson(response, OrderListItem.Response::class.java)
            if (orderListItem.status) {
                return orderListItem.data.result
                //onResponseAPI.onSuccessKitchenOrderListItem(orderListItem.data.result)
            } else return orderJson//onResponseAPI.onFailure()
        } else {
            return orderJson
            // onResponseAPI.onFailure()
        }
        Log.v("Response is: ", response.toString())
    }
}