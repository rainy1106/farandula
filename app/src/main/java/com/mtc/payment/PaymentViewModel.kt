package com.mtc.payment

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.mtc.R
import com.mtc.api.APIConstant
import com.mtc.api.CommonApi
import com.mtc.dialog.DialogTip
import com.mtc.general.BaseViewModel
import com.mtc.general.SharedPreference
import com.mtc.general.SyncLiveData
import com.mtc.interfaces.EventHandler
import com.mtc.kitchen.OrderListItem
import com.mtc.utils.NetworkUtility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject


class PaymentViewModel : BaseViewModel() {

    var _animationOne = MutableLiveData<Int>()
    var _order_id = MutableLiveData<String>()
    var _seat_id = MutableLiveData<String>()

    init {
        syncLiveDataObserver = Observer { isSynced ->
            this.isSynced = isSynced
            if (isSynced) reportHomeButton()
        }
        SyncLiveData.observeForever(syncLiveDataObserver)
    }

    fun getSeatsInTable(context: Context, mEventHandler: EventHandler) {
        val commonApi = CommonApi()
        commonApi.getSeatsInTable(context, mEventHandler)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun getPaymentOptions(context: Context): ArrayList<PaymentType> {
        val paymentArrayList = ArrayList<PaymentType>()
        var payment = PaymentType(
            context.getString(R.string.credit_card),
            context.resources.getDrawable(R.drawable.master_card)
        )
        paymentArrayList.add(payment)

        payment = PaymentType(
            context.getString(R.string.cash),
            context.resources.getDrawable(R.drawable.cash_on_delivery)
        )
        paymentArrayList.add(payment)
        return paymentArrayList
    }

    fun createBranches(requireContext: Context, param: EventHandler) {
        FirebaseDatabase.getInstance().reference.child(SharedPreference.getTableId(requireContext)!!)
            .child(SharedPreference.getSeatId(requireContext)!!).addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val arraysList = ArrayList<Seats>()
                        for (sp in snapshot.children) {
                            val seats = sp.getValue(Seats::class.java)
                            arraysList.add(seats!!)
                        }
                        param.onSuccessSeats(arraysList)
//                        val arraysList = ArrayList<Seats>()
//                        for (sp in snapshot.children) {
//                            val seats = sp.getValue(Seats::class.java)
//                            arraysList.add(seats!!)
//                        }
//                        param.onSuccessSeats(arraysList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                }
            )
    }

    fun updatePayment(
        total: String,
        mViewModel: PaymentViewModel,
        requireContext: Context,
        mEventHandler: EventHandler
    ) {
        val urlLine = APIConstant.ApiBaseUrl +
                APIConstant.UPDATE_ORDER +
                "order_id=${
                    mViewModel._order_id.value.toString().trim()/*FragmentPayment.order_id*/
                }" +
                "&status=PAID" +
                "&paid_amount=${total.trim()}" +
                "&payment_mode=CASH" +
                "&payment_status=1" +
                "&payment_by=${SharedPreference.getSeatId(requireContext)?.trim()}" +
                "&tip_amount=${DialogTip.lastValue}"

        CoroutineScope(Dispatchers.IO).launch {
            val rss = NetworkUtility.APIrequest(urlLine)
            withContext(Dispatchers.Main) {
                try {
                    val jsonObject = JSONObject(rss.toString())
                    val status = jsonObject.getInt("status")
                    if (status == 1)
                        mEventHandler.onSuccess()
                    else
                        mEventHandler.onFailure()
                } catch (ex: Exception) {
                    mEventHandler.onFailure()
                }

            }
        }
    }

    fun callPay(
        total: String,
        mViewModel: PaymentViewModel,
        requireContext: Context,
        resultLauncher: ActivityResultLauncher<Intent>,
        progressDialog: ProgressDialog
    ) {
        val urlLine = APIConstant().getApiBaseUrl(
            "make_payment?order_id=${
                mViewModel._order_id.value.toString().trim()/*FragmentPayment.order_id*/
            }&seat_id=${
                SharedPreference.getSeatId(requireContext)
            }&amount=${total.trim()}&tip_amount=${DialogTip.lastValue}"
        )
        CoroutineScope(Dispatchers.IO).launch {
            val rss = NetworkUtility.APIrequest(urlLine)
            withContext(Dispatchers.Main) {
                try {
                    val paymentLink = getPaymentLink(rss.toString())
                    resultLauncher.launch(Intent(
                        requireContext,
                        WebViewAct::class.java
                    ).apply {
                        this.putExtra("url", paymentLink)
                    })
                } catch (ex: Exception) {
                    if (progressDialog != null)
                        progressDialog.dismiss()
                    Toast.makeText(
                        requireContext,
                        "Network Error, Please try again..",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    //callPay(total, mViewModel, requireContext, resultLauncher)
                } finally {
                    if (progressDialog != null)
                        progressDialog.dismiss()
                }
            }
        }
    }

    private fun getPaymentLink(reponse: String): String {
        val jsonObject = JSONObject(reponse)
        val status = jsonObject.getBoolean("status")
        if (status) {
            val data = jsonObject.getJSONObject("data")
            val result = data.getJSONObject("result")
            val payment_link = result.getJSONObject("payment_link")
            return payment_link.getString("url")
        } else {
            return ""
        }
    }

    fun setPaymentType(
        requireContext: Context,
        resultLauncher: ActivityResultLauncher<Intent>,
        mViewModel: PaymentViewModel, total: String, param: EventHandler
    ) {

        if (FragmentPayment.placeOrder.paymentType == "Cash"
            || FragmentPayment.placeOrder.paymentType.isEmpty()
        ) {
            updatePayment(total, mViewModel, requireContext, object : EventHandler {
                override fun onSuccess() {
                    super.onSuccess()
                    param.onComplete()
                    showToast(
                        requireContext.getString(R.string.payment_is_successfully_done),
                        requireContext
                    )
                }

                override fun onFailure() {
                    super.onFailure()
                    param.onComplete()
                }
            })

        } else if (FragmentPayment.placeOrder.paymentType == "Credit Card") {
            _animationOne.value = 2
            val progressDialog = ProgressDialog(requireContext)
            progressDialog.setCancelable(true)
            progressDialog.setMessage("Processing , Please wait...")
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progressDialog.show()
            callPay(total, mViewModel, requireContext, resultLauncher, progressDialog)
        }
    }

    fun getPaymentScreen(requireContext: Context, arrayListDuplicate: ArrayList<Seats>) {
        val urlLine = APIConstant().getApiBaseUrl(
            "get_payment_screen?table_id=${
                SharedPreference.getTableId(requireContext)
            }"
        )
        CoroutineScope(Dispatchers.IO).launch {
            val rss = NetworkUtility.APIrequest(urlLine)
            withContext(Dispatchers.Main) {
                try {
                    val arrayListOrderId = ArrayList<String>()
                    val arrayListSeatId = ArrayList<String>()
                    val responseJson = getPaymentJson(rss.toString())
                    responseJson.forEach { _order ->
                        arrayListDuplicate.forEach { seats ->
                            if (_order.seat_id == seats.seat_id) {
                                arrayListOrderId.add(_order.order_id)
                                arrayListSeatId.add(_order.seat_id)
                            }
                        }
                    }
                    _seat_id.value = arrayListSeatId.joinToString(separator = ",")
                    _order_id.value = arrayListOrderId.joinToString(separator = ",")
                } catch (ex: Exception) {
                    //getPaymentScreen(requireContext, FragmentPayment.arrayListDuplicate)
                }
            }
        }


    }

    /*---------------------*/
    private fun getPaymentJson(response: String): java.util.ArrayList<OrderListItem.Result> {
        val orderJson = java.util.ArrayList<OrderListItem.Result>()
        val jsonObject = JSONObject(response)
        val status = jsonObject.getBoolean("status")
        return if (status) {
            val gson = Gson()
            val orderListItem = gson.fromJson(response, OrderListItem.Response::class.java)
            orderListItem.data.result
        } else {
            orderJson
        }
    }
    /*---------------------*/


}