//package com.mtc.general
//
//import android.app.Activity
//import android.app.Application
//import android.os.Bundle
//import androidx.multidex.MultiDexApplication
//import com.google.gson.Gson
//
//class FarandulaApplication : MultiDexApplication(), Application.ActivityLifecycleCallbacks {
//
//    private var activityReferences = 0
//    private var isActivityChangingConfigurations = false
//
//    override fun onCreate() {
//        super.onCreate()
//        registerActivityLifecycleCallbacks(this)
//        FarandulaApiApplication.init(instance.applicationContext)
//    }
//
//
//    companion object {
//        var instance: FarandulaApplication = FarandulaApplication()
//        var gson: Gson = Gson()
//
//        fun getContext(): FarandulaApplication {
//            return instance
//        }
//    }
//
//
//
//    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
//        TODO("Not yet implemented")
//    }
//
//    override fun onActivityStarted(p0: Activity) {
//        TODO("Not yet implemented")
//    }
//
//    override fun onActivityResumed(p0: Activity) {
//        TODO("Not yet implemented")
//    }
//
//    override fun onActivityPaused(p0: Activity) {
//        TODO("Not yet implemented")
//    }
//
//    override fun onActivityStopped(p0: Activity) {
//        TODO("Not yet implemented")
//    }
//
//    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
//        TODO("Not yet implemented")
//    }
//
//    override fun onActivityDestroyed(p0: Activity) {
//        TODO("Not yet implemented")
//    }
//
//}