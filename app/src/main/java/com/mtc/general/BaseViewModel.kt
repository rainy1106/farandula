package com.mtc.general

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.mtc.order.OrderListViewModel
import java.math.RoundingMode
import java.text.DecimalFormat

open class BaseViewModel : ViewModel() {

    companion object {
        val reportHomeButton: LiveData<String> get() = _reportHomeButton
        var _reportHomeButton: MutableLiveData<String> = MutableLiveData()
        fun reportHomeButton() {
            var total: Double = 0.0
            OrderListViewModel.orderListSelected.forEach {
                //  total = it.price.replace("$", " ").trim().toDouble().plus(total)
                val tempTotal =
                    it.quatity.toInt().times(it.price.replace("$", " ").trim().toDouble())
                total = total.plus(tempTotal)
            }
            _reportHomeButton.value = BaseViewModel().roundOffDecimal(total).toString()
//            val total = OrderListViewModel.orderListSelected.sumOf { it.price.replace("$","").toDouble() }
//            _reportHomeButton.value = total
        }

//        val updateHomeButton: LiveData<String> get() = _updateHomeButton
//        var _updateHomeButton: MutableLiveData<String> = MutableLiveData()
//        fun updatePlaceOrder() {
//            _reportHomeButton.value = "0.0"
//        }


    }

    val readyCount: LiveData<String> get() = _readyCount
    var _readyCount: MutableLiveData<String> = MutableLiveData()

    val newOrderCount: LiveData<String> get() = _newOrderCount
    var _newOrderCount: MutableLiveData<String> = MutableLiveData()

    val upComingCount: LiveData<String> get() = _upComingCount
    var _upComingCount: MutableLiveData<String> = MutableLiveData()


    val toBePrint: LiveData<String> get() = _toBePrint
    var _toBePrint: MutableLiveData<String> = MutableLiveData()

    var isSynced: Boolean? = false
    lateinit var syncLiveDataObserver: Observer<Boolean>

    fun updateNewOrderCount(valueN: String, valueU: String, valueR: String, valueP: String) {
        _newOrderCount.value = valueN
        _upComingCount.value = valueU
        _readyCount.value = valueR
        _toBePrint.value = valueP
    }

    fun isInternetConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
//        if (networkInfo?.type == ConnectivityManager.TYPE_ETHERNET)
//            return networkInfo.isConnected
//        else return false
    }


    fun roundOffDecimal(number: Double): Double {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(number).toDouble()
    }

    fun showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun calculateTaxValue(grandTotal: String): String {
        val withTax = (grandTotal.toDouble() * 7) / 100
        return roundOffDecimal(grandTotal.toDouble().plus(withTax)).toString()
    }
}
