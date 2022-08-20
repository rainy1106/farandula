package com.mtc.kitchen

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.gson.Gson
import com.mtc.api.APIConstant
import com.mtc.api.BaseRepository
import com.mtc.general.BaseViewModel
import com.mtc.general.SharedPreference
import com.mtc.models.Notifications
import com.mtc.network.ConnectionModel
import com.mtc.utils.NetworkUtility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class OrdersViewModel(
    var baseRepository: BaseRepository
) : BaseViewModel() {

    companion object {
        val showDialog = MutableLiveData<Boolean>()
        var newOrder = MutableLiveData<String>()
        var userUpdates = MutableLiveData<String>()
        var kitchenNotifications = MutableLiveData<Boolean>()
    }
//    var showProgress = MutableLiveData<Boolean>()

    val _mNotificationList = MutableLiveData<ArrayList<Notifications.ResultNoti>>()
    val updateBadgeCount = MutableLiveData<ArrayList<OrderListItem.Result>>()
    val productList = MutableLiveData<ArrayList<OrderListItem.Result>>()
    val type = MutableLiveData<String>()
    private var networkState: ConnectionModel? = null

    //    val orderList = MutableLiveData<Responses>()
    val errorMessage = MutableLiveData<String>()


    fun setNetworkState(connectionModel: ConnectionModel) {
        networkState = connectionModel
    }


    fun getOrders(context: Context) {
        baseRepository.getOrders(context, productList, updateBadgeCount)
    }


    fun getNotificationListKitchen(requireContext: Context) {
        val urlPath =
            "get_notification?user_id=${SharedPreference.getRestaurantId(requireContext)}&type=kitchen"
        val urlLine: String = APIConstant().getApiBaseUrl(urlPath)
        CoroutineScope(Dispatchers.IO).launch {
            val rss = NetworkUtility.APIrequest(urlLine)
            withContext(Dispatchers.Main) {
                val notificationList = getNotificationJson(rss.toString())
                _mNotificationList.value = notificationList
            }
        }
    }

    private fun getNotificationJson(json: String): ArrayList<Notifications.ResultNoti> {

        val arrayListNoti = ArrayList<Notifications.ResultNoti>()
        val res = JSONObject(json).getInt("status")
        if (res == 1) {
            val gson = Gson()
            val notificationResponse = gson.fromJson(json, Notifications.Response::class.java)
            return notificationResponse.result
        } else
            emptyArray<Notifications>()
        return arrayListNoti
    }


    fun visibleShimmer(shimmerLayoutK: ShimmerFrameLayout, shimmerLayoutK1: ShimmerFrameLayout) {
        shimmerLayoutK.startShimmer()
        shimmerLayoutK1.startShimmer()

        shimmerLayoutK.visibility = View.VISIBLE
        shimmerLayoutK1.visibility = View.VISIBLE
    }


    fun inVisibleShimmer(shimmerLayoutK: ShimmerFrameLayout, shimmerLayoutK1: ShimmerFrameLayout) {
        shimmerLayoutK.stopShimmer()
        shimmerLayoutK1.stopShimmer()

        shimmerLayoutK.visibility = View.GONE
        shimmerLayoutK1.visibility = View.GONE
    }

//    fun getAllOrders() {
//        val baseRepository = BaseRepository(ApiInterface.getInstance())
//        val response = baseRepository.getAllOrders()
//        response.enqueue(object : Callback<Responses> {
//            override fun onResponse(
//                call: Call<Responses>,
//                response: Response<Responses>
//            ) {
//                Log.v("Response : ", Gson().toJson(response.raw()))
//                Log.v("Response : ", Gson().toJson(response.body()))
//                //orderList.postValue(response.body()?.data?.result)
//                //response.body()?.data?.result?.getItemNameByPosition(2)
//            }
//
//            override fun onFailure(call: Call<Responses>, t: Throwable) {
//
//                Log.v("Response : ", Gson().toJson(t.message))
//                errorMessage.postValue(t.message)
//            }
//
//        })
//    }
//
//    fun <T> List<T>.getItemNameByPosition(item: T): String {
//        this.forEachIndexed { _, it ->
//            if ((it as Results).restaurantId.toInt() == item) {
//                Log.d("---", (it as Results).restaurant_name)
//                return (it as Results).restaurant_name
//            }
//            //return index
//        }
//        return "0"
//    }
}