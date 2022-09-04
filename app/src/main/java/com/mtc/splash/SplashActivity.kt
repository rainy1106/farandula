package com.mtc.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.mtc.R
import com.mtc.databinding.ActivitySplashBinding
import com.mtc.general.BaseActivity
import com.mtc.general.SharedPreference
import com.mtc.general.initViewModel
import com.mtc.home.HomeActivity
import com.mtc.interfaces.EventHandler
import com.mtc.kitchen.ActivityEnterCode


class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>(), EventHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding.splashViewModel = getViewModel()
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        validateUser()
    }

    override fun getLayoutId(): Int = R.layout.activity_splash

    override fun getViewModel(): SplashViewModel {
        return initViewModel {
            SplashViewModel()
        }
    }

    private fun validateUser() {
        playVideo()
    }

    private fun playVideo() {

         setKitchen()
        //setFarandula()
    }

    private fun setFarandula() {
        if (SharedPreference.getUserStatus(this)) {
            finish()
            intent = Intent(this@SplashActivity, HomeActivity::class.java)
            startActivity(intent)
        } else {
            finish()
            intent = Intent(this@SplashActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    fun setKitchen() {
        finish()
        intent = Intent(this@SplashActivity, ActivityEnterCode::class.java)
        startActivity(intent)
    }

    override fun getBindingVariable(): Int {
        TODO("Not yet implemented")
    }
}
