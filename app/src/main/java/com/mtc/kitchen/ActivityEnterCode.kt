package com.mtc.kitchen

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.mtc.R
import com.mtc.api.CommonApi
import com.mtc.databinding.ActivityEnterCodeBinding
import com.mtc.general.BaseViewModel
import com.mtc.general.SharedPreference
import com.mtc.general.hideKeyboard
import com.mtc.interfaces.EventHandler
import kotlinx.android.synthetic.main.activity_enter_code.*

class ActivityEnterCode : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityEnterCodeBinding
    private var fcmToken: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnterCodeBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        initialize()
    }

    private fun initialize() {
        binding.goButton.setOnClickListener(this@ActivityEnterCode)
    }

    override fun onResume() {
        super.onResume()
        hideKeyboard()
        SharedPreference.setIsKitchen(this@ActivityEnterCode, true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.goButton -> {
                navigateToActivity()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun navigateToActivity() {
        if (BaseViewModel().isInternetConnected(this@ActivityEnterCode)) {
            if (fcmToken.isNotEmpty())
                verifyEnteredCode()
            else
                Toast.makeText(
                    this,
                    getString(R.string.notification_token_is_not_generated),
                    Toast.LENGTH_SHORT
                )
                    .show()
        } else {
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT)
                .show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun verifyEnteredCode() {

        if (enterCode.text.trim().isNotEmpty()) {


            val progressDialog = ProgressDialog(this@ActivityEnterCode)
            progressDialog.setMessage(getString(R.string.verifying_code_please_wait))
            progressDialog.setCancelable(true)
            progressDialog.show()


            val commonApi = CommonApi()
            commonApi.verifyCode(this@ActivityEnterCode, enterCode.text.trim().toString(), fcmToken,
                object : EventHandler {

                    override fun onComplete() {
                        super.onComplete()
                        progressDialog.dismiss()
                    }

                    override fun onSuccess() {
                        super.onSuccess()
                        intent = Intent(this@ActivityEnterCode, HomeActivityKitchen::class.java)
                        startActivity(intent)
                    }

                    override fun onFailure(response: String) {
                        super.onFailure(response)
                        Toast.makeText(this@ActivityEnterCode, response, Toast.LENGTH_SHORT)
                            .show()
                    }
                })

        } else {
            Toast.makeText(
                this@ActivityEnterCode,
                getString(R.string.please_enter_code),
                Toast.LENGTH_SHORT
            ).show()
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
                    Log.d("TAG", msg)
                })
    }
}
