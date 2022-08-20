package com.mtc.splash

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.mtc.api.APIConstant
import com.mtc.dialog.SelectionClass
import com.mtc.general.BaseViewModel
import com.mtc.general.SharedPreference
import com.mtc.interfaces.DispatcherProvider
import com.mtc.interfaces.DispatcherProviderImpl
import com.mtc.interfaces.EventHandler
import com.mtc.kitchen.VerifyCode
import com.mtc.utils.NetworkUtility
import kotlinx.coroutines.*
import org.json.JSONObject

class SplashViewModel(
    private val dispatcher: DispatcherProvider = DispatcherProviderImpl()
) : BaseViewModel() {

    // region - Companion object
    companion object {
        //        var FCM: String?=""
        const val MIN_DELAY = 1500L
    }
    // endregion

    // region - Public properties
    var navigateToHome: MutableLiveData<Boolean> = MutableLiveData()
    var navigateToMainScreen: MutableLiveData<Boolean> = MutableLiveData()

    // endregion
//    private var networkState: ConnectionModel? = null
//    fun setNetworkState(connectionModel: ConnectionModel) {
//        networkState = connectionModel
//
//    }

    // region - Public function
    fun delayAndNavigateToLogin() {
        CoroutineScope(dispatcher.main).launch {
            delay(MIN_DELAY)
            navigateToHome.postValue(true)
//            val isUserStatusPending =
//                loginManager.getUserStatus().equals(Constants.USER_STATUS_PENDING, true)
//            if (loginManager.getIsUserLoggedIn()/* && !isUserStatusPending*/)
//                navigateToMainScreen.postValue(true)
//            else navigateToLogin.postValue(true)
        }
    }

    fun onClickNext() {
        navigateToHome.postValue(true)
    }

    fun callMyRestaurant(context: Context, onEventHandler: EventHandler) {
//        val commonApi = CommonApi()
//        commonApi.getRestaurants(context, onEventHandler)

        val _url: String = APIConstant().getApiBaseUrl(APIConstant.GET_RESTAURANTS)
        val urlLine: String = _url
        CoroutineScope(Dispatchers.IO).launch {
            val rss = NetworkUtility.APIrequest(urlLine)
            withContext(Dispatchers.Main) {
//                val commonApi = CommonApi()
                try {
                    val arrayList = getRestaurantJson(rss.toString())
                    onEventHandler.onSuccessSelectionClass(arrayList, "restaurant")
                } catch (ex: Exception) {
                    Toast.makeText(context, "Network Error, Please try again..", Toast.LENGTH_SHORT)
                        .show()
                    //callMyRestaurant(context, onEventHandler)
                }

                // call to UI thread
                //isLoadingData = false
                //showProgress.value = false
                // usersList.value?.addAll(parseJsonString(rss.toString()))
            }
        }


    }

    private fun getRestaurantJson(response: String): ArrayList<SelectionClass> {
        val arrayList: ArrayList<SelectionClass> = ArrayList()
        val jsonObject = JSONObject(response)
        Log.v("Response is: ", jsonObject.toString())
        val statusB = jsonObject.getBoolean("status")
        if (statusB) {
            val data: JSONObject = jsonObject.getJSONObject("data")
            val resultArray = data.getJSONArray("result")
            if (resultArray.length() > 0) {

                for (i in 0 until resultArray.length()) {
                    val product = resultArray.getJSONObject(i)
                    val restaurantCode = product.getString("restaurant_code")
                    val restaurantId = product.getString("restaurant_id")
                    val restaurantName = product.getString("restaurant_name")
                    val restaurantImage = product.getString("restaurant_image")
                    val status = product.getString("status")
                    val entrydt = product.getString("entrydt")
                    val selectionClass = SelectionClass(
                        table_id = "",
                        restaurant_id = restaurantId,
                        seat_id = "",
                        restaurant_name = restaurantName,
                        table_name = "",
                        seat_name = "",
                        restaurant_code = restaurantCode,
                        restaurant_image = restaurantImage,
                        status = status, entrydt = entrydt
                    )
                    arrayList.add(selectionClass)
                }

            }
        }
        return arrayList
    }


    fun callMyTable(context: Context, onEventHandler: EventHandler) {
//        val commonApi = CommonApi()
//        commonApi.getTables(context, onEventHandler)

        val restaurantId = SharedPreference.getRestaurantId(context)
        val urlLine: String = APIConstant().getApiBaseUrl(APIConstant.GET_TABLES + restaurantId)//
        CoroutineScope(Dispatchers.IO).launch {
            val rss = NetworkUtility.APIrequest(urlLine)
            withContext(Dispatchers.Main) {
//                val commonApi = CommonApi()
                try {
                    val arrayList = getTableJson(rss.toString())
                    onEventHandler.onSuccessSelectionClass(arrayList, "table")
                } catch (ex: Exception) {
                    Toast.makeText(context, "Network Error, Please try again..", Toast.LENGTH_SHORT)
                        .show()
                    // callMyTable(context, onEventHandler)
                }

                // call to UI thread
                //isLoadingData = false
                //showProgress.value = false
                // usersList.value?.addAll(parseJsonString(rss.toString()))
            }
        }
    }

    private fun getTableJson(response: String): ArrayList<SelectionClass> {
        val arrayList: ArrayList<SelectionClass> = ArrayList()
        val jsonObject = JSONObject(response)
        Log.v("Response is: ", jsonObject.toString())
        val statusB = jsonObject.getBoolean("status")
        if (statusB) {
            val data: JSONObject = jsonObject.getJSONObject("data")
            val resultArray = data.getJSONArray("result")
            if (resultArray.length() > 0) {

                for (i in 0 until resultArray.length()) {
                    val product = resultArray.getJSONObject(i)
                    val table_id = product.getString("table_id")
                    val restaurantId = product.getString("restaurant_id")
                    val table_name = product.getString("table_name")
                    val status = product.getString("status")
                    val entrydt = product.getString("entrydt")
                    val selectionClass = SelectionClass(
                        table_id = table_id, seat_id = "", restaurant_code = "",
                        restaurant_id = restaurantId,
                        table_name = table_name, seat_name = "", restaurant_name = "",
                        restaurant_image = "",
                        status = status, entrydt = entrydt
                    )
                    arrayList.add(selectionClass)
                }

            }
        }
        // Display the first 500 characters of the response string.
        Log.v("Response is: ", response.toString())
        return arrayList
    }

    fun callMySeats(context: Context, onEventHandler: EventHandler) {
//        val commonApi = CommonApi()
//        commonApi.getSeats(context, onEventHandler)

        val pathUrl =
            "get_seats?restaurant_id=${SharedPreference.getRestaurantId(context)}&table_id=${
                SharedPreference.getTableId(context)
            }"
        val urlLine: String = APIConstant().getApiBaseUrl(pathUrl)
        CoroutineScope(Dispatchers.IO).launch {
            val rss = NetworkUtility.APIrequest(urlLine)
            withContext(Dispatchers.Main) {
                try {
                    val arrayList = getSeatJson(rss.toString())
                    onEventHandler.onSuccessSelectionClass(arrayList, "seats")
                } catch (ex: Exception) {
                    Toast.makeText(context, "Network Error, Please try again..", Toast.LENGTH_SHORT)
                        .show()
                    // callMySeats(context, onEventHandler)
                }
            }
        }
    }

    private fun getSeatJson(response: String): ArrayList<SelectionClass> {
        val arrayList: ArrayList<SelectionClass> = ArrayList<SelectionClass>()
        val jsonObject = JSONObject(response)
        Log.v("Response is: ", jsonObject.toString())
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
                    val categoryObj = SelectionClass(
                        seat_id = seat_id,
                        restaurant_id = restaurant_id,
                        table_id = table_id,
                        restaurant_name = "",
                        table_name = "",
                        seat_name = seat_name,
                        status = status,
                        entrydt = entrydt,
                        restaurant_code = "",
                        restaurant_image = ""
                    )
                    arrayList.add(categoryObj)
                }

            }
        }
        // Display the first 500 characters of the response string.
        Log.v("Response is: ", response.toString())
        return arrayList
    }

    fun callLoginAPI(context: Context, fcmToken: String, onEventHandler: EventHandler) {
//        val commonApi = CommonApi()
//        commonApi.loginUser(context, fcmToken, mEventHandler)
//        val urlLine = "https://codezens.com/farandula/index.php/api/seat_login?seat_id=" +
//                "${SharedPreference.getSeatId(context)}&device_type=Android&register_id=$fcmToken"
        val urlLine = APIConstant.ApiBaseUrl + "seat_login?seat_id=" +
                "${SharedPreference.getSeatId(context)}&device_type=Android&register_id=$fcmToken"
        CoroutineScope(Dispatchers.IO).launch {
            val rss = NetworkUtility.APIrequest(urlLine)
            withContext(Dispatchers.Main) {
                try {
                    getLoginResponse(rss.toString(), onEventHandler)
                } catch (ex: Exception) {
                    Toast.makeText(context, "Network Error, Please try again..", Toast.LENGTH_SHORT)
                        .show()
                    //  callLoginAPI(context, fcmToken, onEventHandler)
                }
            }
        }
    }

    private fun getLoginResponse(response: String, mEventHandler: EventHandler) {
        val _res = JSONObject(response).getBoolean("status")
        if (_res) {
            val gson = Gson()
            val verifyCodeResponse =
                gson.fromJson(response, VerifyCode.Response::class.java)
            mEventHandler.onComplete()
            if (verifyCodeResponse.status) {
                mEventHandler.onSuccess()
            } else mEventHandler.onFailure()
            Log.v("Response is: ", response.toString())
        } else {
            mEventHandler.onFailure(JSONObject(response).getString("message"))
        }
    }
    // endregion
}
