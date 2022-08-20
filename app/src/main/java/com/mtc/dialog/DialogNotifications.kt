//package com.mtc.dialog
//
//import android.app.Activity
//import android.app.Dialog
//import android.content.Context
//import android.content.DialogInterface
//import android.graphics.Color
//import android.graphics.drawable.ColorDrawable
//import android.os.Bundle
//import android.util.DisplayMetrics
//import android.view.Gravity
//import android.view.View
//import android.view.Window
//import android.view.WindowManager
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.google.gson.Gson
//import com.mtc.R
//import com.mtc.api.APIConstant
//import com.mtc.general.SharedPreference
//import com.mtc.kitchen.adapters.NotificationKitchenAdapter
//import com.mtc.models.Notifications
//import com.mtc.utils.NetworkUtility
//import kotlinx.android.synthetic.main.dialg_notifications.*
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import org.json.JSONObject
//
//
//class DialogNotifications : Dialog {
//
//
//    private lateinit var _activity: Activity
//
//    companion object;
//
//    constructor(activity: Activity) : super(activity) {
//        _activity = activity
//    }
//
//    constructor(context: Context, themeResId: Int) : super(context, themeResId)
//
//    constructor(
//        context: Context,
//        cancelable: Boolean,
//        cancelListener: DialogInterface.OnCancelListener?
//    ) : super(context, cancelable, cancelListener)
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        setContentView(R.layout.dialg_notifications)
//        setWindow()
//    }
//
//
//    override fun onAttachedToWindow() {
//        super.onAttachedToWindow()
//        callNotifications()
//    }
//
//    private fun callNotifications() {
//        try {
//            getNotificationListUser(_activity)
//        } catch (ex: Exception) {
//            getNotificationListUser(_activity)
//        }
//    }
//
//    fun getNotificationListUser(requireContext: Context) {
//        val urlPath =
//            "get_notification?user_id=${SharedPreference.getSeatId(requireContext)}&type=user"
//        val urlLine: String = APIConstant().getApiBaseUrl(urlPath)
//        CoroutineScope(Dispatchers.IO).launch {
//            val rss = NetworkUtility.APIrequest(urlLine)
//            withContext(Dispatchers.Main) {
//                try {
//                    val notificationList = getNotificationJson(rss.toString())
//                    setAdapter(notificationList)
//                }catch (ex:Exception){
//                    getNotificationListUser(requireContext)
//                }
//            }
//        }
//    }
//
//    private fun getNotificationJson(json: String): ArrayList<Notifications.ResultNoti> {
//
//        val arrayListNoti = ArrayList<Notifications.ResultNoti>()
//        val res = JSONObject(json).getInt("status")
//        if (res == 1) {
//            val gson = Gson()
//            val notificationResponse = gson.fromJson(json, Notifications.Response::class.java)
//            return notificationResponse.result
//        } else
//            emptyArray<Notifications>()
//        return arrayListNoti
//    }
//
//    private fun setAdapter(messageList: ArrayList<Notifications.ResultNoti>) {
//        if (messageList.isEmpty().not()) {
//            val notificationAdapter = NotificationKitchenAdapter(messageList)
//            recyclerViewUserNotificationList.adapter = notificationAdapter
//            recyclerViewUserNotificationList.visibility = View.VISIBLE
//            noTextView.visibility = View.GONE
//            notificationAdapter.setOnItemClick(object : NotificationKitchenAdapter.OnItemClick {
//                override fun onItemClick(position: Int) {
//                    //val order_id = mNotificationList[position].order_id
//                   // getOrders("247"/*order_id*/)
//                }
//            })
//        } else {
//            recyclerViewUserNotificationList.visibility = View.GONE
//            noTextView.visibility = View.VISIBLE
//        }
//        recyclerViewUserNotificationList.setHasFixedSize(false)
//        val layoutManager = LinearLayoutManager(context)
//        recyclerViewUserNotificationList.layoutManager = layoutManager
//    }
//
//
//    private fun setWindow() {
//        val displayMetrics = DisplayMetrics()
//        window?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
//        val lp = WindowManager.LayoutParams()
//        lp.copyFrom(window?.attributes)
//        val displayWidth = displayMetrics.widthPixels
//        val displayHeight = displayMetrics.heightPixels
//        val dialogWindowWidth = (displayWidth * 0.8f).toInt()
//        val dialogWindowHeight = (displayHeight * 0.8f).toInt()
//
//        lp.width = dialogWindowWidth
//        lp.height = dialogWindowHeight
//        lp.gravity = Gravity.NO_GRAVITY
//        window?.attributes = lp
//        this.setCancelable(false)
//
//        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//        chatCross.setOnClickListener {
//            dismiss()
//        }
//    }
//
//
//
//}