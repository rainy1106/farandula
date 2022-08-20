package com.mtc.utils

import android.app.Application
import android.content.Context

class MyAppContext : Application() {
    //    override fun attachBaseContext(base: Context?) {
//        super.attachBaseContext(base)
//        MultiDex.install(this);
//    }
    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }


    //
    companion object {
        lateinit var mInstance: MyAppContext
        fun getContext(): Context? {
            return mInstance.applicationContext
        }

        fun get(): Context? {
            return mInstance
        }
    }
}