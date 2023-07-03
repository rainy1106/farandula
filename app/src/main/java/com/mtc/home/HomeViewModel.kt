package com.mtc.home

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.mtc.api.APIConstant
import com.mtc.api.CommonApi
import com.mtc.general.BaseViewModel
import com.mtc.general.SharedPreference
import com.mtc.interfaces.EventHandler
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

    fun setBanner(context: Context) {
        val commonApi = CommonApi()
        try {
            commonApi.getBanner(context, object : EventHandler {
                override fun onSuccess() {
                    super.onSuccess()
                    banner.value = APIConstant.BANNER_URL
                }
            })

        } catch (ex: Exception) {
//            setBannerV()
        }
    }

    private var networkState: ConnectionModel? = null
    fun setNetworkState(connectionModel: ConnectionModel) {
        networkState = connectionModel

    }
}