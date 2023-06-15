package com.mtc.api

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.mtc.R
import com.mtc.general.SharedPreference
import com.mtc.interfaces.EventHandler
import com.mtc.interfaces.onResponseAPI
import com.mtc.kitchen.OrderHistory
import com.mtc.kitchen.VerifyCode
import com.mtc.payment.FragmentPayment
import com.mtc.payment.Seats
import com.mtc.utils.NetworkUtility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject


class CommonApi {


    companion object {
        lateinit var instance: CommonApi
    }

    init {
        instance = this
    }


    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {

      return  android.provider.Settings.Secure.getString(
            context.contentResolver,
            android.provider.Settings.Secure.ANDROID_ID
        );
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            return Settings.Secure.getString(
//                ApplicationProvider.getApplicationContext<Context>().getContentResolver(),
//                Settings.Secure.ANDROID_ID
//            )
//        } else {
//            val telephonyManager =
//                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
//            if (telephonyManager!!.imei != null) {
//                return telephonyManager.imei
//            } else if (telephonyManager.meid != null) {
//                return telephonyManager.meid
//            }
//        }
        return ""
    }

//    fun getProductList(
//        context: Context,
//        /*  onResponseAPI: onResponseAPI?,*/
//        _mList: MutableLiveData<ArrayList<OrderItem>>,
//        _mListF: MutableLiveData<Boolean>
//    ) {
//
//        val arrayList: ArrayList<OrderItem> = ArrayList<OrderItem>()
//        val queue = Volley.newRequestQueue(context)
//        val apiConstant = APIConstant()
//        val restaurantId = SharedPreference.getRestaurantId(context)
//        val categoryId = SharedPreference.getCatId(context)
//        val _url = "get_products?restaurant_id=${restaurantId}&category_id=${categoryId}"
//        val urlLine: String = apiConstant.getApiBaseUrl(_url)
//        HttpsTrustManager.allowAllSSL()
//        val stringRequest = object : StringRequest(Method.GET, urlLine,
//            { response ->
//                val jsonObject = JSONObject(response)
//                Log.v("Response URL is: ", urlLine)
//                Log.v("Response is: ", jsonObject.toString())
//                val statusB = jsonObject.getBoolean("status")
//                if (statusB) {
//                    val data: JSONObject = jsonObject.getJSONObject("data")
//                    val resultArray = data.getJSONArray("result")
//                    if (resultArray.length() > 0) {
//
//                        for (i in 0 until resultArray.length()) {
//                            val product = resultArray.getJSONObject(i)
//                            val product_id = product.getString("product_id")
//                            val restaurant_id = product.getString("restaurant_id")
//                            val category_id = product.getString("category_id")
//                            val product_name = product.getString("product_name")
//                            val price = "$" + product.getString("price")
//                            val description = product.getString("description")
//                            val image = product.getString("image")
//                            val status = product.getString("status")
//                            val entrydt = product.getString("entrydt")
//                            val extra_items = product.getString("extra_items")
//
//                            val productList = OrderItem(
//                                product_id,
//                                restaurant_id,
//                                category_id,
//                                product_name,
//                                price,
//                                description,
//                                image,
//                                status,
//                                entrydt,
//                                false, true,
//                                "1", extra_items
//                            ,"")
//                            arrayList.add(productList)
//                        }
//                        _mList.value = arrayList
//                        //onResponseAPI?.onSuccess(arrayList)
//                    }
//                } else {
//                    _mListF.value = false
//                    //onResponseAPI?.onFailure()
//                }
//                // Display the first 500 characters of the response string.
//                Log.v("Response is: ", response.toString())
//            },
//            {
//                Log.v("Response is: ", urlLine + " : That didn't work!")
//                getProductList(context, _mList, _mListF)
//            }) {
//            override fun getPriority(): Priority {
//                return Priority.HIGH
//            }
//        }
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest)
//    }

//    fun getCategories(context: Context, eventHandler: EventHandler?) {
//
//        val arrayList: ArrayList<Category> = ArrayList<Category>()
//        val queue = Volley.newRequestQueue(context)
//        val apiConstant = APIConstant()
//        val _url = apiConstant.getApiBaseUrl(
//            "get_categories?restaurant_id=${
//                SharedPreference.getRestaurantId(context)
//            }"
//        )
//        val urlLine: String = _url//apiConstant.getApiBaseUrl(APIConstant.GET_CATEGORIES)
//        HttpsTrustManager.allowAllSSL()
//        val stringRequest = object : StringRequest(
//            Method.GET, urlLine,
//            { response ->
//                val jsonObject = JSONObject(response)
//                var hashMap = HashMap<String, ArrayList<SubCategory>>()
//                var subCatList = ArrayList<SubCategory>()
//                Log.v("Response is: ", jsonObject.toString())
//                val statusB = jsonObject.getBoolean("status")
//                if (statusB) {
//                    val data: JSONObject = jsonObject.getJSONObject("data")
//                    val resultArray = data.getJSONArray("result")
//                    if (resultArray.length() > 0) {
//
//                        for (i in 0 until resultArray.length()) {
//                            val category = resultArray.getJSONObject(i)
//                            val subCategories = category.getJSONArray("subCategories")
//                            val categoryObj = Category(
//                                category.getString("category_id"),
//                                category.getString("restaurant_id"),
//                                category.getString("category_name"),
//                                category.getString("category_image"),
//                                category.getString("status"),
//                                category.getString("entrydt")
//                            )
//                            for (j in 0 until subCategories.length()) {
//                                subCatList = ArrayList<SubCategory>()
//                                val _subCatObj = subCategories.getJSONObject(j)
//                                val subCatObj = SubCategory(
//                                    _subCatObj.getString("sub_category_id"),
//                                    _subCatObj.getString("restaurant_id"),
//                                    _subCatObj.getString("category_id"),
//                                    _subCatObj.getString("sub_category_name"),
//                                    _subCatObj.getString("status"),
//                                    _subCatObj.getString("entrydt")
//                                )
//                                subCatList.add(subCatObj)
//                            }
//
//                            APIConstant.categoriesList[categoryObj.category_name] = subCatList
//                            arrayList.add(categoryObj)
//                        }
//                        eventHandler?.onSuccessCategory(arrayList)
//                    }
//                }
//                Log.v("Response is: ", response.toString())
//            },
//            {
//                Log.v("Response is: ", _url + "That didn't work!")
//                getCategories(context, eventHandler)
//            }) {
//            override fun getPriority(): Priority {
//                return Priority.IMMEDIATE
//            }
//        }
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest)
//    }

//    fun getOrders(context: Context, onResponseAPI: onResponseAPI) {
//        val queue = Volley.newRequestQueue(context)
//        val apiConstant = APIConstant()
//        val restaurantId = SharedPreference.getRestaurantKitchen(context)
//        val urlPath = "get_order?restaurant_id=${restaurantId}"
//        val urlLine: String = apiConstant.getApiBaseUrl(urlPath)
//        HttpsTrustManager.allowAllSSL()
//        val stringRequest = object : StringRequest(
//            Method.GET, urlLine,
//            { response ->
//
//                val res = JSONObject(response).getBoolean("status")
//                if (res) {
//                    val gson = Gson()
//                    val orderListItem =
//                        gson.fromJson(response, OrderListItem.Response::class.java)
//                    if (orderListItem.status)
//                        onResponseAPI.onSuccessKitchenOrderListItem(orderListItem.data.result)
//                    else onResponseAPI.onFailure()
//                } else {
//                    onResponseAPI.onFailure()
//                }
//                // Display the first 500 characters of the response string.
//                Log.v("Response is: ", response.toString())
//            },
//            {
//                Log.v("Response is: ", urlLine + "That didn't work!")
//            }) {
//            override fun getPriority(): Priority {
//                return Priority.HIGH
//            }
//        }
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest)
//    }

//    fun getSeats(context: Context, eventHandler: EventHandler?) {
////        {"seat_id":"1","restaurant_id":"1","table_id":"1","seat_name":"Seat A","status":"1","entrydt":"2022-05-20 04:53:43"}
//        val arrayList: ArrayList<SelectionClass> = ArrayList<SelectionClass>()
//        val queue = Volley.newRequestQueue(context)
//        val apiConstant = APIConstant()
//        val pathUrl =
//            "get_seats?restaurant_id=${SharedPreference.getRestaurantId(context)}&table_id=${
//                SharedPreference.getTableId(context)
//            }"
//        val urlLine: String = apiConstant.getApiBaseUrl(pathUrl)
//        HttpsTrustManager.allowAllSSL()
//        val stringRequest = object : StringRequest(
//            Method.GET, urlLine,
//            { response ->
//                val jsonObject = JSONObject(response)
//                Log.v("Response is: ", jsonObject.toString())
//                val statusB = jsonObject.getBoolean("status")
//                if (statusB) {
//                    val data: JSONObject = jsonObject.getJSONObject("data")
//                    val resultArray = data.getJSONArray("result")
//                    if (resultArray.length() > 0) {
//
//                        for (i in 0 until resultArray.length()) {
//                            val seats = resultArray.getJSONObject(i)
//                            val seat_id = seats.getString("seat_id")
//                            val restaurant_id = seats.getString("restaurant_id")
//                            val table_id = seats.getString("table_id")
//                            val seat_name = seats.getString("seat_name")
//                            val status = seats.getString("status")
//                            val entrydt = seats.getString("entrydt")
//                            val categoryObj = SelectionClass(
//                                seat_id = seat_id,
//                                restaurant_id = restaurant_id,
//                                table_id = table_id,
//                                restaurant_name = "",
//                                table_name = "",
//                                seat_name = seat_name,
//                                status = status,
//                                entrydt = entrydt,
//                                restaurant_code = "",
//                                restaurant_image = ""
//                            )
//                            arrayList.add(categoryObj)
//                        }
//                        eventHandler?.onSuccessSelectionClass(arrayList, "seats")
//                    }
//                }
//                // Display the first 500 characters of the response string.
//                Log.v("Response is: ", response.toString())
//            },
//            {
//                Log.v("Response is: ", urlLine + "That didn't work!")
//                getSeats(context, eventHandler)
////                eventHandler?.onFailure()
//            }) {
//            override fun getPriority(): Priority {
//                return Priority.HIGH
//            }
//        }
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest)
//    }

    fun getSeatsInTable(context: Context, eventHandler: EventHandler?) {
        val arrayList: ArrayList<Seats> = ArrayList<Seats>()
//        val queue = Volley.newRequestQueue(context)
        val apiConstant = APIConstant()
        val pathUrl =
            "get_seats?restaurant_id=${SharedPreference.getRestaurantId(context)}&table_id=${
                SharedPreference.getTableId(context)
            }"
        val urlLine: String = apiConstant.getApiBaseUrl(pathUrl)
        CoroutineScope(Dispatchers.IO).launch {
            val rss = NetworkUtility.APIrequest(urlLine)
            if (rss.toString().isEmpty().not())
                withContext(Dispatchers.Main) {
                    try {

                        val jsonObject = JSONObject(rss.toString())
                        val statusB = jsonObject.getBoolean("status")
                        if (statusB) {
                            val data: JSONObject = jsonObject.getJSONObject("data")
                            val resultArray = data.getJSONArray("result")
                            if (resultArray.length() > 0) {

                                for (i in 0 until resultArray.length()) {
                                    val seats = resultArray.getJSONObject(i)
                                    val seat_id = seats.getString("seat_id")
                                    val restaurant_id = seats.getString("restaurant_id")
                                    val table_id = seats.getString("table_id")
                                    val seat_name = seats.getString("seat_name")
                                    val status = seats.getString("status")
                                    val entrydt = seats.getString("entrydt")
                                    val categoryObj = Seats(
                                        seat_id,
                                        restaurant_id,
                                        table_id,
                                        seat_name,
                                        status,
                                        entrydt,
                                        false,
                                        "0.0"/*, false*/
                                    )
                                    arrayList.add(categoryObj)
                                }
                                eventHandler?.onSuccessSeats(arrayList)
                            }
                        }
                        //
                    } catch (ex: Exception) {
                        Toast.makeText(
                            context,
                            "Network Error, Please try again..",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        // getSeatsInTable(context, eventHandler)
                    }
                }
            else eventHandler?.onFailure()

        }


        //---------------------------------------------------------------------------
//        HttpsTrustManager.allowAllSSL()
//        val stringRequest = object : StringRequest(
//            Method.GET, urlLine,
//            { response ->
//                val jsonObject = JSONObject(response)
//                Log.v("Response is: ", jsonObject.toString())
//                val statusB = jsonObject.getBoolean("status")
//                if (statusB) {
//                    val data: JSONObject = jsonObject.getJSONObject("data")
//                    val resultArray = data.getJSONArray("result")
//                    if (resultArray.length() > 0) {
//
//                        for (i in 0 until resultArray.length()) {
//                            val seats = resultArray.getJSONObject(i)
//                            val seat_id = seats.getString("seat_id")
//                            val restaurant_id = seats.getString("restaurant_id")
//                            val table_id = seats.getString("table_id")
//                            val seat_name = seats.getString("seat_name")
//                            val status = seats.getString("status")
//                            val entrydt = seats.getString("entrydt")
//                            val categoryObj = Seats(
//                                seat_id, restaurant_id,
//                                table_id, seat_name, status, entrydt, false, "0.0"/*, false*/
//                            )
//                            arrayList.add(categoryObj)
//                        }
//                        eventHandler?.onSuccessSeats(arrayList)
//                    }
//                }
//                // Display the first 500 characters of the response string.
//                Log.v("Response is: ", response.toString())
//            },
//            {
//                Log.v("Response is: ", urlLine + "That didn't work!")
//                eventHandler?.onFailure()
//            }) {
//            override fun getPriority(): Priority {
//                return Priority.HIGH
//            }
//        }
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest)
    }

    /**/
//    fun placeOrder(context: Context, eventHandler: EventHandler) {
//        val queue = Volley.newRequestQueue(context)
//        val apiConstant = APIConstant()
//        val urlLine: String = apiConstant.getApiBaseUrl(convertToProduct(context))
//        HttpsTrustManager.allowAllSSL()
//        val stringRequest = object : StringRequest(
//            Method.GET, urlLine,
//            { response ->
//                val jsonObject = JSONObject(response)
//                Log.v("Response is: ", jsonObject.toString())
//                val statusB = jsonObject.getBoolean("status")
//                if (statusB) {
//                    val data: JSONObject = jsonObject.getJSONObject("data")
//                    val result = data.getJSONObject("result")
//                    //{"promocode":"","instractions":"","status":"PENDING","entrydt":"2022-06-14 13:18:23","txn_id":"abc","sub_total":"7","payment_mode":"card","seat_id":"1","txn_amount":"7","datetime":"2022-06-14 18:48:23","date":"2022-06-14","order_id":"5","discount":"0"}
//                    eventHandler.onSuccess(result)
//                } else {
//                    eventHandler.onSuccess("Failed")
//                }
//                // Display the first 500 characters of the response string.
//                Log.v("Response is: ", response.toString())
//            },
//            {
//                Log.v("Response is: ", urlLine + "That didn't work!")
//                eventHandler.onFailure()
//            }) {
//            override fun getPriority(): Priority {
//                return Priority.IMMEDIATE
//            }
//        }
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest)
//    }

//    private fun convertToProduct(context: Context): String {
//
//        val arrayListProductId = ArrayList<String>()
//        val arrayListProductQ = ArrayList<String>()
//        val arrayListProductP = ArrayList<String>()
//        OrderListViewModel.orderListSelected.forEach {
//            arrayListProductId.add(it.product_id)
//            arrayListProductQ.add(it.quatity)
//            arrayListProductP.add(it.price.replace("$", ""))
//        }
//
//
//        val PLACE_AN_ORDER =
//            "place_an_order?restaurant_id=${SharedPreference.getRestaurantId(context)}" +
//                    "&table_id=${SharedPreference.getTableId(context)}" +
//                    "&seat_id=${SharedPreference.getSeatId(context)}" +
//                    "&product_id=" + arrayListProductId.joinToString(separator = ",") +
//                    "&quantity=" + arrayListProductQ.joinToString(separator = ",") +
//                    "&price=" + arrayListProductP.joinToString(separator = ",") +
//                    "&sub_total=${FragmentPayment.placeOrder.grandTotal}" +
//                    "&discount=0&txn_amount=0&txn_id=abc&payment_mode=${FragmentPayment.placeOrder.paymentType}" +
//                    "&instractions=" + OrderListViewModel.instractions +
//                    "&extra_items=" + OrderListViewModel.extraItems.joinToString(separator = ",")
//
//        Log.v("PLACE_AN_ORDER", PLACE_AN_ORDER)
//        return PLACE_AN_ORDER
//    }

//    fun addToCart(context: Context, mEventHandler: EventHandler) {
//        val queue = Volley.newRequestQueue(context)
//        val apiConstant = APIConstant()
//        ADD_TO_CART = "add_to_cart?seat_id=1&product_id=1&quantity=1&price=7"
//        val urlLine: String = apiConstant.getApiBaseUrl(ADD_TO_CART)
//        //   HttpsTrustManager.allowAllSSL()
//    }

//    fun getRestaurants(context: Context, onEventHandler: EventHandler) {
//        val arrayList: ArrayList<SelectionClass> = ArrayList()
//        val queue = Volley.newRequestQueue(context)
//        val urlLine: String = APIConstant().getApiBaseUrl(APIConstant.GET_RESTAURANTS)
//        HttpsTrustManager.allowAllSSL()
//        val stringRequest = object : StringRequest(
//            Method.GET, urlLine,
//            { response ->
//                val jsonObject = JSONObject(response)
//                Log.v("Response is: ", jsonObject.toString())
//                val statusB = jsonObject.getBoolean("status")
//                if (statusB) {
//                    val data: JSONObject = jsonObject.getJSONObject("data")
//                    val resultArray = data.getJSONArray("result")
//                    if (resultArray.length() > 0) {
//
//                        for (i in 0 until resultArray.length()) {
//                            val product = resultArray.getJSONObject(i)
//                            val restaurantCode = product.getString("restaurant_code")
//                            val restaurantId = product.getString("restaurant_id")
//                            val restaurantName = product.getString("restaurant_name")
//                            val restaurantImage = product.getString("restaurant_image")
//                            val status = product.getString("status")
//                            val entrydt = product.getString("entrydt")
//                            val selectionClass = SelectionClass(
//                                table_id = "",
//                                restaurant_id = restaurantId,
//                                seat_id = "",
//                                restaurant_name = restaurantName,
//                                table_name = "",
//                                seat_name = "",
//                                restaurant_code = restaurantCode,
//                                restaurant_image = restaurantImage,
//                                status = status, entrydt = entrydt
//                            )
//                            arrayList.add(selectionClass)
//                        }
//                        onEventHandler.onSuccessSelectionClass(arrayList, "restaurant")
//                    }
//                }
//                // Display the first 500 characters of the response string.
//                Log.v("Response is: ", response.toString())
//            },
//            {
//                Log.v("Response is: ", urlLine + "That didn't work!")
//                getRestaurants(context, onEventHandler)
//                //onEventHandler.onFailure()
//            }) {
//            override fun getPriority(): Priority {
//                return Priority.HIGH
//            }
//        }
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest)
//    }

//    fun getTables(context: Context, onEventHandler: EventHandler) {
////        {"table_id":"26","restaurant_id":"3","table_name":"Table 1","status":"1","entrydt":"2022-05-20 14:55:43"}
//        val arrayList: ArrayList<SelectionClass> = ArrayList()
//        val queue = Volley.newRequestQueue(context)
//        val apiConstant = APIConstant()
//        val restaurantId = SharedPreference.getRestaurantId(context)
//        val url: String = APIConstant.GET_TABLES + restaurantId//
//        val urlLine: String = apiConstant.getApiBaseUrl(url)
//        HttpsTrustManager.allowAllSSL()
//        val stringRequest = object : StringRequest(
//            Method.GET, urlLine,
//            { response ->
//                val jsonObject = JSONObject(response)
//                Log.v("Response is: ", jsonObject.toString())
//                val statusB = jsonObject.getBoolean("status")
//                if (statusB) {
//                    val data: JSONObject = jsonObject.getJSONObject("data")
//                    val resultArray = data.getJSONArray("result")
//                    if (resultArray.length() > 0) {
//
//                        for (i in 0 until resultArray.length()) {
//                            val product = resultArray.getJSONObject(i)
//                            val table_id = product.getString("table_id")
//                            val restaurantId = product.getString("restaurant_id")
//                            val table_name = product.getString("table_name")
//                            val status = product.getString("status")
//                            val entrydt = product.getString("entrydt")
//                            val selectionClass = SelectionClass(
//                                table_id = table_id, seat_id = "", restaurant_code = "",
//                                restaurant_id = restaurantId,
//                                table_name = table_name, seat_name = "", restaurant_name = "",
//                                restaurant_image = "",
//                                status = status, entrydt = entrydt
//                            )
//                            arrayList.add(selectionClass)
//                        }
//                        onEventHandler.onSuccessSelectionClass(arrayList, "table")
//                    }
//                }
//                // Display the first 500 characters of the response string.
//                Log.v("Response is: ", response.toString())
//            },
//            {
//                Log.v("Response is: ", urlLine + "That didn't work!")
//                getTables(context, onEventHandler)
//            }) {
//            override fun getPriority(): Priority {
//                return Priority.HIGH
//            }
//        }
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest)
//    }

    /**/
    @RequiresApi(Build.VERSION_CODES.O)
    fun verifyCode(context: Context, code: String, fcmToken: String, mEventHandler: EventHandler) {
        val urlLine: String = APIConstant().getApiBaseUrl(APIConstant.VERIFY_CODE) + code +
                "&register_id=" + fcmToken + "&device_type=Android&device_id=${getDeviceId(context)}"
        CoroutineScope(Dispatchers.IO).launch {
            val rss = NetworkUtility.APIrequest(urlLine)
            if (rss.toString().isEmpty().not())
                withContext(Dispatchers.Main) {
                    try {
                        getVerifyCodeJson(context, rss.toString(), mEventHandler)
                    } catch (ex: Exception) {
                        Toast.makeText(
                            context,
                            "Network Error, Please try again..",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        //verifyCode(context, code, fcmToken, mEventHandler)
                    }
                    // call to UI thread
                    //isLoadingData = false
                    //showProgress.value = false
                    // usersList.value?.addAll(parseJsonString(rss.toString()))
                }
            else mEventHandler.onFailure("No Response from server!!")

        }
        /*-------------------------------------------------------*/
//        val queue = Volley.newRequestQueue(context)
//        val urlLine: String = APIConstant().getApiBaseUrl(APIConstant.VERIFY_CODE) + code +
//                "&register_id=" + fcmToken + "&device_type=Android"
//        HttpsTrustManager.allowAllSSL()
//        val stringRequest = object : StringRequest(
//            Method.GET, urlLine,
//            { response ->
//
//                val _res = JSONObject(response).getBoolean("status")
//                if (_res) {
//                    val gson = Gson()
//                    val verifyCodeResponse =
//                        gson.fromJson(response, VerifyCode.Response::class.java)
//                    mEventHandler.onComplete()
//                    if (verifyCodeResponse.status) {
//                        SharedPreference.setRestaurantKitchen(
//                            context,
//                            verifyCodeResponse.data.result
//                        )
//                        mEventHandler.onSuccess()
//                    } else mEventHandler.onFailure()
//                    Log.v("Response is: ", response.toString())
//                } else {
//                    mEventHandler.onFailure(JSONObject(response).getString("message"))
//                }
//            }, {
//                Log.v("Response is: ", urlLine + "That didn't work!")
//            }) {
//            override fun getPriority(): Priority {
//                return Priority.HIGH
//            }
//        }
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest)
    }

    private fun getVerifyCodeJson(context: Context, response: String, mEventHandler: EventHandler) {
        val _res = JSONObject(response).getBoolean("status")
        if (_res) {
            val gson = Gson()
            val verifyCodeResponse =
                gson.fromJson(response, VerifyCode.Response::class.java)
            mEventHandler.onComplete()
            if (verifyCodeResponse.status) {
                SharedPreference.setRestaurantKitchen(context, verifyCodeResponse.data.result)
                SharedPreference.setKitchenAddress(context,verifyCodeResponse.data.result.restaurant_address)
                APIConstant.restaurant_address_cons = verifyCodeResponse.data.result.restaurant_address
                mEventHandler.onSuccess()
            } else mEventHandler.onFailure()
            Log.v("Response is: ", response.toString())
        } else {
            mEventHandler.onFailure(JSONObject(response).getString("message"))
        }
    }
    /*-----------------------------*/


    fun getBanner(context: Context, mEventHandler: EventHandler) {
        val queue = Volley.newRequestQueue(context)
        val apiConstant = APIConstant()
        val urlLine: String = apiConstant.getApiBaseUrl(APIConstant.BANNER)
        HttpsTrustManager.allowAllSSL()
        val stringRequest = object : StringRequest(
            Method.GET, urlLine,
            { response ->
                val jsonObject = JSONObject(response)
                Log.v("Response is: ", jsonObject.toString())
                val statusB = jsonObject.getBoolean("status")
                if (statusB) {
                    val data: JSONObject = jsonObject.getJSONObject("data")
                    val resultArray = data.getJSONArray("result")
                    if (resultArray.length() > 0) {
                        for (i in 0 until resultArray.length()) {
                            val category = resultArray.getJSONObject(i)
                            APIConstant.BANNER_URL = category.getString("banner_image")
                        }
                    }
                    mEventHandler.onSuccess()
                }
                Log.v("Response is: ", response.toString())
            },
            {
                Log.v("Response is: ", urlLine + "That didn't work!")
                getBanner(context, mEventHandler)
            }) {
            override fun getPriority(): Request.Priority {
                return Request.Priority.HIGH
            }
        }
        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    fun getOrderHistory(context: Context, onResponseAPI: onResponseAPI) {
        val queue = Volley.newRequestQueue(context)
        val urlLine =
            APIConstant.ApiBaseUrl + APIConstant.GET_ORDERS +
                    "restaurant_id=${SharedPreference.getRestaurantKitchen(context)}" +
                    "&date=${SharedPreference.getDate(context)}"
        HttpsTrustManager.allowAllSSL()
        val stringRequest = object : StringRequest(
            Method.GET, urlLine,
            { response ->
                Log.v("Response is: ", response.toString())
                val _res = JSONObject(response).getBoolean("status")
                if (_res) {
                    val gson = Gson()
                    val orderHistory =
                        gson.fromJson(response, OrderHistory.OrderHistoryRoot::class.java)
                    if (orderHistory.status)
                        onResponseAPI.onOrderHistory(orderHistory.data.result)
                    else onResponseAPI.onFailure()
                } else {
                    onResponseAPI.onFailure()
                }
            },
            {
                Log.v("Response is: ", urlLine + "That didn't work!")
            }) {
            override fun getPriority(): Priority {
                return Priority.HIGH
            }
        }

        queue.add(stringRequest)

    }

//    fun loginUser(context: Context, fcmToken: String, mEventHandler: EventHandler) {
//        val queue = Volley.newRequestQueue(context)
////     val urlLine = "https://codezens.com/farandula/index.php/api/seat_login?seat_id=" +
////                "${SharedPreference.getSeatId(context)}&device_type=Android&register_id=$fcmToken"
//        val urlLine = "https://multitouchscreentables.com/index.php/api/seat_login?seat_id=" +
//                "${SharedPreference.getSeatId(context)}&device_type=Android&register_id=$fcmToken"
//        Log.v("urlLine", urlLine)
//        HttpsTrustManager.allowAllSSL()
//        val stringRequest = object : StringRequest(
//            Method.GET, urlLine,
//            { response ->
//
//                val _res = JSONObject(response).getBoolean("status")
//                if (_res) {
//                    val gson = Gson()
//                    val verifyCodeResponse =
//                        gson.fromJson(response, VerifyCode.Response::class.java)
//                    mEventHandler.onComplete()
//                    if (verifyCodeResponse.status) {
//                        mEventHandler.onSuccess()
//                    } else mEventHandler.onFailure()
//                    Log.v("Response is: ", response.toString())
//                } else {
//                    mEventHandler.onFailure(JSONObject(response).getString("message"))
//                }
//            }, {
//                Log.v("Response is: ", urlLine + "That didn't work!")
//                loginUser(context, fcmToken, mEventHandler)
//            }) {
//            override fun getPriority(): Priority {
//                return Priority.HIGH
//            }
//        }
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest)
//    }

//    fun updatePayment(context: Context, mEventHandler: EventHandler) {
//        val queue = Volley.newRequestQueue(context)
//        val urlLine = APIConstant.ApiBaseUrl +
//                APIConstant.UPDATE_ORDER +
//                "order_id=${FragmentPayment.order_id}" +
//                "&txn_amount=0&txn_id=xyz" +
//                "&payment_mode=${FragmentPayment.placeOrder.paymentType}" +
//                "&payment_status=1" +
//                "&payment_by=${SharedPreference.getSeatId(context)}" + "&status=PAID"
//        Log.v("urlLine", urlLine)
//        HttpsTrustManager.allowAllSSL()
//        val stringRequest = object : StringRequest(
//            Method.GET, urlLine,
//            { response ->
//
//                val _res = JSONObject(response).getInt("status")
//                if (_res == 1) {
//                    val gson = Gson()
//                    val updateReponse =
//                        gson.fromJson(response, UpdateReponse.Response::class.java)
//                    mEventHandler.onComplete()
//                    mEventHandler.onSuccess()
//                    Log.v("Response is: ", response.toString())
//                } else {
//                    mEventHandler.onFailure(JSONObject(response).getString("message"))
//                }
//            }, {
//                Log.v("Response is: ", urlLine + "That didn't work!")
//            }) {
//            override fun getPriority(): Priority {
//                return Priority.HIGH
//            }
//        }
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest)
//    }

//    fun updateOrderIsReady(context: Context, orderId: String, mEventHandler: EventHandler) {
//        val queue = Volley.newRequestQueue(context)
//        val urlLine = APIConstant.ApiBaseUrl +
//                APIConstant.UPDATE_ORDER +
//                "order_id=${orderId}" +
//                "&status=READY"
//        Log.v("urlLine", urlLine)
//        HttpsTrustManager.allowAllSSL()
//        val stringRequest = object : StringRequest(
//            Method.GET, urlLine,
//            { response ->
//
//                val _res = JSONObject(response).getInt("status")
//                if (_res == 1) {
//                    val gson = Gson()
//                    val updateReponse =
//                        gson.fromJson(response, UpdateReponse.Response::class.java)
//                    mEventHandler.onComplete()
//                    mEventHandler.onSuccess()
//                    Log.v("Response is: ", response.toString())
//                } else {
//                    mEventHandler.onFailure(JSONObject(response).getString("message"))
//                }
//            }, {
//                Log.v("Response is: ", urlLine + "That didn't work!")
//            }) {
//            override fun getPriority(): Priority {
//                return Priority.HIGH
//            }
//        }
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest)
//    }

//    fun updateOrderAccept(context: Context, orderId: String, mEventHandler: EventHandler) {
//        val queue = Volley.newRequestQueue(context)
//        val urlLine = APIConstant.ApiBaseUrl +
//                APIConstant.UPDATE_ORDER +
//                "order_id=${orderId}" +
//                "&status=ACCEPTED"
//        Log.v("urlLine", urlLine)
//        HttpsTrustManager.allowAllSSL()
//        val stringRequest = object : StringRequest(
//            Method.GET, urlLine,
//            { response ->
//
//                val _res = JSONObject(response).getInt("status")
//                if (_res == 1) {
//                    val gson = Gson()
//                    val updateReponse =
//                        gson.fromJson(response, UpdateReponse.Response::class.java)
//                    mEventHandler.onComplete()
//                    mEventHandler.onSuccess()
//                    Log.v("Response is: ", response.toString())
//                } else {
//                    mEventHandler.onFailure(JSONObject(response).getString("message"))
//                }
//            }, {
//                Log.v("Response is: ", urlLine + "That didn't work!")
//            }) {
//            override fun getPriority(): Priority {
//                return Priority.HIGH
//            }
//        }
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest)
//    }

//    fun updateOrder(context: Context, mEventHandler: EventHandler) {
//        val queue = Volley.newRequestQueue(context)
//        val apiConstant = APIConstant()
//        HttpsTrustManager.allowAllSSL()
//        val urlLine: String = apiConstant.getApiBaseUrl(convertToProductUpdate(context))
//        val stringRequest = object : StringRequest(
//            Method.GET, urlLine,
//            { response ->
//                val jsonObject = JSONObject(response)
//                Log.v("Response is: ", jsonObject.toString())
//                val statusB = jsonObject.getInt("status")
//                if (statusB == 1) {
//                    // val data: JSONObject = jsonObject.getJSONObject("data")
//                    val result = jsonObject.getJSONObject("result")
//                    mEventHandler.onSuccess(result)
//                } else {
//                    mEventHandler.onSuccess("Failed")
//                }
//            }, {
//                Log.v("Response is: ", urlLine + "That didn't work!")
//            }) {
//            override fun getPriority(): Priority {
//                return Priority.IMMEDIATE
//            }
//        }
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest)
//    }

//    private fun convertToProductUpdate(context: Context): String {
////        val urlLine = APIConstant.ApiBaseUrl +
////                APIConstant.UPDATE_ORDER +
////                "order_id=${orderId}" +
////                "&product_id=ACCEPTED" +
////                "&quantity=" +
////                "&price=" +
////                "&subtotal"
//        val arrayListProductId = ArrayList<String>()
//        val arrayListProductQ = ArrayList<String>()
//        val arrayListProductP = ArrayList<String>()
//        OrderListViewModel.orderListSelected.filter { it.isEnable }.forEach {
//            arrayListProductId.add(it.product_id)
//            arrayListProductQ.add(it.quatity)
//            arrayListProductP.add(it.price.replace("$", ""))
//        }
//
//
//        val UPDATE_ORDER =
//            "update_order?order_id=${FragmentPayment.order_id}" +
//                    "&product_id=" + arrayListProductId.joinToString(separator = ",") +
//                    "&quantity=" + arrayListProductQ.joinToString(separator = ",") +
//                    "&price=" + arrayListProductP.joinToString(separator = ",") +
//                    "&sub_total=${FragmentPayment.placeOrder.grandTotal}" +
//                    "&instractions=" + OrderListViewModel.instractions +
//                    "&extra_items=" + OrderListViewModel.extraItems.joinToString(separator = ",") +
//                    "&action=update"
//
//        Log.v("UPDATE_ORDER", UPDATE_ORDER)
//        return UPDATE_ORDER
//    }

    fun sendFeedBackToServer(
        context: Context,
        mFeedBack: String,
        mRating: String,
        mEventHandler: EventHandler
    ) {
        // val queue = Volley.newRequestQueue(context)
        val apiConstant = APIConstant()
        HttpsTrustManager.allowAllSSL()
        val url =
            "update_order?order_id=${FragmentPayment.order_id}&rating=${mRating}&review=${mFeedBack}&action=rating"
        val urlLine: String = apiConstant.getApiBaseUrl(url)
        CoroutineScope(Dispatchers.IO).launch {
            val response = NetworkUtility.APIrequest(urlLine)
            if (response.toString().isEmpty().not())
                withContext(Dispatchers.Main) {
                    try {
                        val jsonObject = JSONObject(response.toString())
                        val statusB = jsonObject.getInt("status")
                        if (statusB == 1) {
                            FragmentPayment.order_id = ""
                            mEventHandler.onSuccess(context.getString(R.string.successfully_submitted))
                        } else {
                            mEventHandler.onSuccess(context.getString(R.string.failed))
                        }
                        mEventHandler.onComplete()
                    } catch (ex: Exception) {
//                        sendFeedBackToServer(
//                            context,
//                            mFeedBack,
//                            mRating,
//                            mEventHandler
//                        )
                        // mEventHandler.onSuccess(context.getString(R.string.failed))
                    }
                    // call to UI thread
                    //isLoadingData = false
                    //showProgress.value = false
                    // usersList.value?.addAll(parseJsonString(rss.toString()))
                }
            else mEventHandler.onFailure(context.getString(R.string.no_response_from_server))
        }
//        val stringRequest = object : StringRequest(
//            Method.GET, urlLine,
//            { response ->
//                val jsonObject = JSONObject(response)
//                Log.v("Response is: ", jsonObject.toString())
//                val statusB = jsonObject.getInt("status")
//                if (statusB == 1) {
//                    mEventHandler.onSuccess("Successfully Submitted")
//                } else {
//                    mEventHandler.onSuccess("Failed")
//                }
//                mEventHandler.onComplete()
//            }, {
//                Log.v("Response is: ", urlLine + "That didn't work!")
//            }) {
//            override fun getPriority(): Priority {
//                return Priority.HIGH
//            }
//        }
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest)
    }

//    fun getNotificationList(context: Context) {
//        val queue = Volley.newRequestQueue(context)
//        val apiConstant = APIConstant()
//        val restaurantId = SharedPreference.getRestaurantKitchen(context)
//        val urlPath = "get_notification?user_id=${SharedPreference.getS}"
//        val urlLine: String = apiConstant.getApiBaseUrl(urlPath)
//        //HttpsTrustManager.allowAllSSL()
//        val stringRequest = StringRequest(
//            Request.Method.GET, urlLine,
//            { response ->
//
//                val res = JSONObject(response).getBoolean("status")
//                if (res) {
//                    val gson = Gson()
//                    val orderListItem =
//                        gson.fromJson(response, OrderListItem.Response::class.java)
//                    if (orderListItem.status)
//                        onResponseAPI.onSuccessKitchenOrderListItem(orderListItem.data.result)
//                    else onResponseAPI.onFailure()
//                } else {
//                    onResponseAPI.onFailure()
//                }
//                // Display the first 500 characters of the response string.
//                Log.v("Response is: ", response.toString())
//            },
//            {
//                Log.v("Response is: ", "That didn't work!")
//            })
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest)
//    }

//    fun downloadPdf(context: Context, _onComplete: MutableLiveData<String>) {
//        val urlLine = APIConstant.ApiBaseUrl +
//                APIConstant.DOWNLOAD_ORDER +
//                "restaurant_id=${SharedPreference.getRestaurantKitchen(context)}" +
//                "&date=${SharedPreference.getDate(context)}"
//        var manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
//        val uri =
//            Uri.parse(urlLine)
//        val request = DownloadManager.Request(uri)
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
//        val reference: Long = manager?.enqueue(request)
//
//        val queue = Volley.newRequestQueue(context)
//        // https://codezens.com/farandula/index.php/api/download_order?restaurant_id=1&date=2022-07-20
//        val urlLine1 = APIConstant.ApiBaseUrl +
//                APIConstant.DOWNLOAD_ORDER +
//                "restaurant_id=${SharedPreference.getRestaurantKitchen(context)}" +
//                "&date=${SharedPreference.getDate(context)}"
//        Log.v("urlLine", urlLine)
//        HttpsTrustManager.allowAllSSL()
//        val stringRequest = StringRequest(
//            Request.Method.GET, urlLine,
//            { response ->
//
//
//            }, {
//                Log.v("Response is: ", "That didn't work!")
//            })
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest)
//    }
}