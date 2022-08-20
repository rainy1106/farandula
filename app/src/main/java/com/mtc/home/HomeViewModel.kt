package com.mtc.home

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.mtc.api.APIConstant
import com.mtc.general.BaseViewModel
import com.mtc.general.SharedPreference
import com.mtc.network.ConnectionModel
import com.mtc.utils.MyAppContext
import com.mtc.utils.NetworkUtility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class HomeViewModel : BaseViewModel() {

    var categoryList: MutableLiveData<ArrayList<Category>> = MutableLiveData(arrayListOf())

    var banner: MutableLiveData<String> = MutableLiveData()


    var isLoadingData = true
    var showProgress = MutableLiveData<Boolean>()


    fun setCategories() {
        //   showProgress.value = true
        val _url = APIConstant().getApiBaseUrl(
            "get_categories?restaurant_id=${
                MyAppContext.get()?.let { SharedPreference.getRestaurantId(it) }
            }"
        )
        val urlLine: String = _url
        CoroutineScope(Dispatchers.IO).launch {
            val rss = NetworkUtility.APIrequest(urlLine)
            withContext(Dispatchers.Main) {
                var cat: ArrayList<Category>
                try {
                    cat = getCategories(rss.toString())
                    if (cat.isEmpty()) {
                        cat = getCategories(rss.toString())
                        categoryList.value = cat
                    } else {
                        categoryList.value = cat
                    }
                } catch (ex: Exception) {
//                    Toast.makeText(context, "Network Error, Please try again..", Toast.LENGTH_SHORT)
//                        .show()
                    // setCategories()
                }
            }
        }
    }

    private fun getCategories(response: String): ArrayList<Category> {
        val arrayList: ArrayList<Category> = ArrayList<Category>()
        val jsonObject = JSONObject(response)
        var hashMap = HashMap<String, ArrayList<SubCategory>>()
        var subCatList = ArrayList<SubCategory>()
        Log.v("Response is: ", jsonObject.toString())

        if (jsonObject.toString().isEmpty()) {
            return arrayList
        }


        val statusB = jsonObject.getBoolean("status")
        if (statusB) {
            val data: JSONObject = jsonObject.getJSONObject("data")
            val resultArray = data.getJSONArray("result")
            if (resultArray.length() > 0) {

                for (i in 0 until resultArray.length()) {
                    val category = resultArray.getJSONObject(i)
                    val subCategories = category.getJSONArray("subCategories")
                    val categoryObj = Category(
                        category.getString("category_id"),
                        category.getString("restaurant_id"),
                        category.getString("category_name"),
                        category.getString("category_image"),
                        category.getString("status"),
                        category.getString("entrydt")
                    )
                    for (j in 0 until subCategories.length()) {
                        subCatList = ArrayList<SubCategory>()
                        val _subCatObj = subCategories.getJSONObject(j)
                        val subCatObj = SubCategory(
                            _subCatObj.getString("sub_category_id"),
                            _subCatObj.getString("restaurant_id"),
                            _subCatObj.getString("category_id"),
                            _subCatObj.getString("sub_category_name"),
                            _subCatObj.getString("status"),
                            _subCatObj.getString("entrydt")
                        )
                        subCatList.add(subCatObj)
                    }

                    APIConstant.categoriesList[categoryObj.category_name] = subCatList
                    arrayList.add(categoryObj)
                }
//                eventHandler?.onSuccessCategory(arrayList)
            }
        }
        Log.v("Response is: ", response.toString())
        return arrayList
    }

    fun setBanner() {
        //   showProgress.value = true
        val urlLine: String = APIConstant().getApiBaseUrl(APIConstant.BANNER)
        CoroutineScope(Dispatchers.IO).launch {
            val rss = NetworkUtility.APIrequest(urlLine)
            withContext(Dispatchers.Main) {
//                val commonApi = CommonApi()
                try {
                    val _banner = getBanner(rss.toString())
                    banner.value = _banner
                } catch (ex: Exception) {
//                    Toast.makeText(context, "Network Error, Please try again..", Toast.LENGTH_SHORT)
//                        .show()
                    //setBanner()
                }

                // call to UI thread
                //isLoadingData = false
                //showProgress.value = false
            }
        }
    }

    private fun getBanner(response: String): String {
        val jsonObject = JSONObject(response)
        Log.v("Response is: ", jsonObject.toString())
        val statusB = jsonObject.getBoolean("status")
        if (statusB) {
            val data: JSONObject = jsonObject.getJSONObject("data")
            val resultArray = data.getJSONArray("result")
            if (resultArray.length() > 0) {
                for (i in 0 until resultArray.length()) {
                    val category = resultArray.getJSONObject(i)
                    return category.getString("banner_image")
                }
            }
            // mEventHandler.onSuccess()
        }
        Log.v("Response is: ", response.toString())
        return ""
    }

//    fun parseJsonString(str: String): ArrayList<OrderItem> {
//        val jsonOb = JSONObject(str)
//        val list: ArrayList<OrderItem> = arrayListOf()
//        val jsonArray = jsonOb.getJSONArray("data");
//
//        for (j in 0..jsonArray.length() - 1) {
//            val user = OrderItem(
//                jsonArray.getJSONObject(j).getString("id"),
//                jsonArray.getJSONObject(j).getString("email"),
//                jsonArray.getJSONObject(j).getString("first_name"),
//                jsonArray.getJSONObject(j).getString("last_name"),
//                jsonArray.getJSONObject(j).getString("avatar")
//            )
//            list.add(user)
//        }
//        return list
//    }

    //    fun setCategories(activity: HomeActivity) {
//        CoroutineScope(Dispatchers.IO).launch {
//            val commonApi = CommonApi()
//            commonApi.getCategories(activity, activity)
//        }
//    }
    private var networkState: ConnectionModel? = null
    fun setNetworkState(connectionModel: ConnectionModel) {
        networkState = connectionModel

    }
}