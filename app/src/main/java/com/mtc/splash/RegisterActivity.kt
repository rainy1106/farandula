package com.mtc.splash

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.mtc.R
import com.mtc.databinding.ActivityRegisterBinding
import com.mtc.dialog.DialogSelectionRes
import com.mtc.dialog.DialogSelectionSeats
import com.mtc.dialog.DialogSelectionTab
import com.mtc.dialog.SelectionClass
import com.mtc.general.BaseActivity
import com.mtc.general.SharedPreference
import com.mtc.general.initViewModel
import com.mtc.home.HomeActivity
import com.mtc.interfaces.EventHandler
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : BaseActivity<ActivityRegisterBinding, SplashViewModel>(),
    View.OnClickListener, EventHandler {
    private var fcmToken: String? = ""

    //private var connectionMode: ConnectionModel? = null
    var dialogRes: DialogSelectionRes? = null
    var dialogTa: DialogSelectionTab? = null
    var dialogSeats: DialogSelectionSeats? = null
    lateinit var progressDialog: ProgressDialog

    // region - Lifecycle functions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding.splashViewModel = getViewModel()
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        progressDialog = ProgressDialog(this)
        linearLayoutRes.setOnClickListener(this)
        linearLayoutTab.setOnClickListener(this)
        linearLayoutSeat.setOnClickListener(this)
        nextButton.setOnClickListener(this)

    }

    private fun callMyRestaurant() {
        try {
            progressDialog.setMessage(getString(R.string.loading_data))
            progressDialog.show()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        mViewModel.callMyRestaurant(this, this)
    }

    override fun getLayoutId(): Int = R.layout.activity_register

    override fun getViewModel(): SplashViewModel {
        return initViewModel {
            SplashViewModel()
        }
    }


    override fun onResume() {
        super.onResume()

//        ConnectionLiveData(this).observe {
//            connectionMode = it
//            restart(it)
//        }
        SharedPreference.setIsKitchen(this@RegisterActivity, false)
        callMyRestaurant()

    }

//    private fun restart(connection: ConnectionModel) {
//        connection.also {
//            mViewModel.setNetworkState(it)
//            if (!connection.isConnected) {
//                mViewModel.showToast(getString(R.string.no_internet_connection), this)
//            } else {
//                callMyRestaurant()
//            }
//        }
//    }

    override fun getBindingVariable(): Int {
        TODO("Not yet implemented")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(view: View?) {

        when (view?.id) {
            R.id.linearLayoutRes -> {
                if (dialogRes != null) {
                    dialogRes?.show()
                } else {
                    mViewModel.showToast(
                        getString(R.string.no_internet_connection),
                        this
                    )
                }

                textViewRes.text = getString(R.string.select_restaurant)
                textViewTab.text = getString(R.string.select_tables)
                textViewSeat.text = getString(R.string.select_seats)
            }
            R.id.linearLayoutTab -> {
                if (SharedPreference.getRestaurantId(this)?.isNotBlank() == true) {
                    if (dialogTa != null)
                        dialogTa?.show()
                    else
                        mViewModel.showToast(
                            getString(R.string.no_internet_connection),
                            this
                        )
                } else {
                    mViewModel.showToast(getString(R.string.select_restaurant), this)
                }


                textViewTab.text = getString(R.string.select_tables)
                textViewSeat.text = getString(R.string.select_seats)

            }
            R.id.linearLayoutSeat -> {
                if ((SharedPreference.getRestaurantId(this)?.isNotBlank() == true
                            && SharedPreference.getTableId(this)?.isNotBlank() == true)
                ) {
                    if (dialogSeats != null)
                        dialogSeats?.show()
                    else
                        mViewModel.showToast(
                            getString(R.string.no_internet_connection),
                            this
                        )
                } else {
                    mViewModel.showToast(
                        getString(R.string.first_select_restaurant_and_table),
                        this
                    )
                }

                textViewSeat.text = getString(R.string.select_seats)
            }
            R.id.nextButton -> {
                val resId = SharedPreference.getRestaurantId(this)
                val tabId = SharedPreference.getTableId(this)
                val seatId = SharedPreference.getSeatId(this)
                if (resId?.isNotBlank() == true) {
                    if (tabId?.isNotBlank() == true) {
                        if (seatId?.isNotBlank() == true) {

                            if (mViewModel.isInternetConnected(this@RegisterActivity)) {


                                progressDialog.setMessage(getString(R.string.registering_please_wait))
                                progressDialog.setCancelable(true)
                                progressDialog.show()


                                if (fcmToken?.isNotEmpty() == true) {
                                    mViewModel.
                                    callLoginAPI(this@RegisterActivity, fcmToken!!,
                                        object : EventHandler {
                                            override fun onComplete() {
                                                super.onComplete()
                                                progressDialog.dismiss()
                                            }

                                            override fun onSuccess() {
                                                super.onSuccess()
                                                SharedPreference.setOneTimeLogin(
                                                    this@RegisterActivity,
                                                    true
                                                )
                                                finish()
                                                intent = Intent(
                                                    this@RegisterActivity,
                                                    HomeActivity::class.java
                                                )
                                                startActivity(intent)
                                            }

                                            override fun onFailure(response: String) {
                                                super.onFailure(response)
                                                Toast.makeText(
                                                    this@RegisterActivity,
                                                    response,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        })
                                } else {
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        getString(R.string.notification_token_is_not_generated),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                mViewModel.showToast(
                                    getString(R.string.no_internet_connection),
                                    this
                                )
                            }

                        } else {
                            mViewModel.showToast(getString(R.string.seat_id_blank), this)
                        }
                    } else {
                        mViewModel.showToast(getString(R.string.table_id_blank), this)
                    }
                } else {
                    mViewModel.showToast(getString(R.string.res_id_blank), this)
                }
            }
        }


//        SharedPreference.setOneTimeLogin(this@RegisterActivity, true)
//        SharedPreference.setRestaurantId(this@RegisterActivity, "1")
//        SharedPreference.setTableId(this@RegisterActivity, "1")
//        SharedPreference.setSeatId(this@RegisterActivity, "1")

//        finish()
//        intent = Intent(this@RegisterActivity, HomeActivity::class.java)
//        startActivity(intent)
    }


    override fun onSuccessSelectionClass(arrayList: ArrayList<SelectionClass>, type: String) {
        super.onSuccessSelectionClass(arrayList, type)
        if (type == "table") {
            dialogTa = DialogSelectionTab(this@RegisterActivity, arrayList, this)
        } else if (type == "seats") {
            dialogSeats = DialogSelectionSeats(this@RegisterActivity, arrayList, this)
        } else if (type == "restaurant") {
            dialogRes = DialogSelectionRes(this@RegisterActivity, arrayList, this)
        }
        if (progressDialog.isShowing)
            progressDialog.hide()
    }


    override fun onSuccess(message: String) {
        super.onSuccess(message)
        if (message == "table") {
            dialogRes?.dismiss()
            textViewRes.text = SharedPreference.getRestaurantName(this@RegisterActivity)
            //if (mViewModel.isInternetConnected(this)) {
            progressDialog.setMessage(getString(R.string.loading_data))
            progressDialog.show()
            mViewModel.callMyTable(this@RegisterActivity, this)
            //} else
            //  mViewModel.showToast(getString(R.string.no_internet_connection), this)
        } else if (message == "seats") {
            dialogTa?.dismiss()
            textViewTab.text = SharedPreference.getTableName(this@RegisterActivity)
            //if (mViewModel.isInternetConnected(this)) {
            progressDialog.setMessage(getString(R.string.loading_data))
            progressDialog.show()
            mViewModel.callMySeats(this@RegisterActivity, this)
            // } else
            //   mViewModel.showToast(getString(R.string.no_internet_connection), this)
        } else {
            dialogSeats?.dismiss()
            if (progressDialog.isShowing)
                progressDialog.dismiss()
            textViewSeat.text = SharedPreference.getSeatName(this@RegisterActivity)
        }
    }

    override fun onStart() {
        super.onStart()

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(
                OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                        return@OnCompleteListener
                    }
                    fcmToken = task.result
                    val msg = resources.getString(R.string.msg_token_fmt, fcmToken)
                    //SplashViewModel.FCM = fcmToken
//                    SharedPreference.setUserFCMToken(this@RegisterActivity, fcmToken)
                    Log.d("TAG", msg)
                })
    }
}
