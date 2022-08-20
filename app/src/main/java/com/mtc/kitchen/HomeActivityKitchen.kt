package com.mtc.kitchen

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.airbnb.lottie.LottieDrawable
import com.mtc.R
import com.mtc.api.APIConstant
import com.mtc.api.CommonApi
import com.mtc.databinding.ActivityHomeKitchenBinding
import com.mtc.general.SharedPreference
import com.mtc.general.hideKeyboard
import com.mtc.interfaces.EventHandler
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home_kitchen.*
import org.json.JSONObject


class HomeActivityKitchen : FragmentActivity(), View.OnClickListener, EventHandler {

    private var tab_value: Int = 0
    private lateinit var binding: ActivityHomeKitchenBinding
    var banner: MutableLiveData<String> = MutableLiveData()

    companion object {
        var _messageHome = MutableLiveData<String>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeKitchenBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        replaceFragment(FragmentOrderListKitchen.newInstance())
        initialize()
    }

    private fun initialize() {
        setBackgroundLayout(0)
        binding.orderHistoryLayout.setOnClickListener(this@HomeActivityKitchen)
        binding.orderLayout.setOnClickListener(this@HomeActivityKitchen)
        binding.chatLayout.setOnClickListener(this@HomeActivityKitchen)
        binding.notificationLayout.setOnClickListener(this@HomeActivityKitchen)

        try {
            lottie_main_bell.setAnimation(R.raw.bell)
            lottie_main_bell.repeatCount = LottieDrawable.INFINITE
            lottie_main_bell.playAnimation()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        _messageHome.observe(this) {
            lottie_main_bell.setAnimation(R.raw.bell)
            lottie_main_bell.repeatCount = LottieDrawable.INFINITE
            lottie_main_bell.playAnimation()
            if (!it.equals("blank")) {
                lottie_main_bell.visibility = VISIBLE
                binding.chatImageView.visibility = GONE
            } else {
                lottie_main_bell.visibility = GONE
                binding.chatImageView.visibility = VISIBLE
            }
        }
//        notifyMessage()
    }

//    private fun setAnim() {
//
//        lottie_main_bell.visibility = GONE
//        lottie_main_bell.setAnimation(R.raw.bell)
//
//        MessageViewModel.anim.observe(this, Observer {
//            Log.v("tab_value", tab_value.toString())
//            if (it && tab_value != 1) {
//                chatImageView.visibility = GONE
//                lottie_main_bell.setAnimation(R.raw.bell)
//                lottie_main_bell.visibility = VISIBLE
//                lottie_main_bell.setAnimation(R.raw.bell)
//                lottie_main_bell.repeatCount = LottieDrawable.INFINITE
//                lottie_main_bell.playAnimation()
//            } else {
//                lottie_main_bell.setAnimation(R.raw.bell)
//                chatImageView.visibility = VISIBLE
//                lottie_main_bell.visibility = GONE
//            }
//        })
//    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.orderLayout -> {
                setBackgroundLayout(0)
                replaceFragment(FragmentOrderListKitchen.newInstance())
            }
            R.id.chatLayout -> {
                setBackgroundLayout(1)
                replaceFragment(FragmentMessages.newInstance())
                // val dialogChat = DialogChatKitchen(this@HomeActivityKitchen)
                //  dialogChat.show()
            }
            R.id.orderHistoryLayout -> {
                setBackgroundLayout(2)
                replaceFragment(FragmentOrderHistory.newInstance())
            }
            R.id.notificationLayout -> {
                setBackgroundLayout(3)
//                Toast.makeText(this@HomeActivityKitchen, "Working on", Toast.LENGTH_SHORT).show()
                replaceFragment(FragmentNotifications.newInstance())
            }
        }
    }

    /*setBackgroundLayout*/
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setBackgroundLayout(value: Int) {
        when (value) {
            0 -> {
                tab_value = 0
                orderHistoryLayout.setBackgroundColor(resources.getColor(R.color.black))
                orderHistoryTextView.setTextColor(resources.getColor(R.color.white))
                orderHistoryArrow.setImageResource(R.drawable.next_arrow_white)
                orderHistoryImageView.setImageResource(R.drawable.history_white)

                chatLayout.setBackgroundColor(resources.getColor(R.color.black))
                chatTextView.setTextColor(resources.getColor(R.color.white))
                chatArrow.setImageResource(R.drawable.next_arrow_white)
                chatImageView.setImageResource(R.drawable.ic_chat)

                orderLayout.background = resources.getDrawable(R.drawable.round_background_white)
                orderTextView.setTextColor(resources.getColor(R.color.black))
                orderArrow.setImageResource(R.drawable.arrow_black)
                orderImageView.setImageResource(R.drawable.order_black)

                notificationLayout.setBackgroundColor(resources.getColor(R.color.black))
                notificationTextView.setTextColor(resources.getColor(R.color.white))
                notificaiotnArrow.setImageResource(R.drawable.next_arrow_white)
                notificationImageView.setImageResource(R.drawable.ic_notification_icon_white)
            }
            1 -> {
                tab_value = 1
                orderHistoryLayout.setBackgroundColor(resources.getColor(R.color.black))
                orderHistoryTextView.setTextColor(resources.getColor(R.color.white))
                orderHistoryArrow.setImageResource(R.drawable.next_arrow_white)
                orderHistoryImageView.setImageResource(R.drawable.history_white)

                orderLayout.setBackgroundColor(resources.getColor(R.color.black))
                orderTextView.setTextColor(resources.getColor(R.color.white))
                orderArrow.setImageResource(R.drawable.next_arrow_white)
                orderImageView.setImageResource(R.drawable.order)

                chatLayout.background = resources.getDrawable(R.drawable.round_background_white)
                chatTextView.setTextColor(resources.getColor(R.color.black))
                chatArrow.setImageResource(R.drawable.arrow_black)
                chatImageView.setImageResource(R.drawable.ic_chat_black)


                notificationLayout.setBackgroundColor(resources.getColor(R.color.black))
                notificationTextView.setTextColor(resources.getColor(R.color.white))
                notificaiotnArrow.setImageResource(R.drawable.next_arrow_white)
                notificationImageView.setImageResource(R.drawable.ic_notification_icon_white)


                lottie_main_bell.visibility = GONE
                chatImageView.visibility = VISIBLE


            }
            2 -> {
                tab_value = 2
                orderLayout.setBackgroundColor(resources.getColor(R.color.black))
                orderTextView.setTextColor(resources.getColor(R.color.white))
                orderArrow.setImageResource(R.drawable.next_arrow_white)
                orderImageView.setImageResource(R.drawable.order)

                chatLayout.setBackgroundColor(resources.getColor(R.color.black))
                chatTextView.setTextColor(resources.getColor(R.color.white))
                chatArrow.setImageResource(R.drawable.next_arrow_white)
                chatImageView.setImageResource(R.drawable.ic_chat)


                orderHistoryLayout.background =
                    resources.getDrawable(R.drawable.round_background_white)
                orderHistoryTextView.setTextColor(resources.getColor(R.color.black))
                orderHistoryArrow.setImageResource(R.drawable.arrow_black)
                orderHistoryImageView.setImageResource(R.drawable.history_black)


                notificationLayout.setBackgroundColor(resources.getColor(R.color.black))
                notificationTextView.setTextColor(resources.getColor(R.color.white))
                notificaiotnArrow.setImageResource(R.drawable.next_arrow_white)
                notificationImageView.setImageResource(R.drawable.ic_notification_icon_white)
            }
            3 -> {
                tab_value = 3
                orderLayout.setBackgroundColor(resources.getColor(R.color.black))
                orderTextView.setTextColor(resources.getColor(R.color.white))
                orderArrow.setImageResource(R.drawable.next_arrow_white)
                orderImageView.setImageResource(R.drawable.order)

                chatLayout.setBackgroundColor(resources.getColor(R.color.black))
                chatTextView.setTextColor(resources.getColor(R.color.white))
                chatArrow.setImageResource(R.drawable.next_arrow_white)
                chatImageView.setImageResource(R.drawable.ic_chat)

                orderHistoryLayout.setBackgroundColor(resources.getColor(R.color.black))
                orderHistoryTextView.setTextColor(resources.getColor(R.color.white))
                orderHistoryArrow.setImageResource(R.drawable.next_arrow_white)
                orderHistoryImageView.setImageResource(R.drawable.history_white)


                notificationLayout.background =
                    resources.getDrawable(R.drawable.round_background_white)
                notificationTextView.setTextColor(resources.getColor(R.color.black))
                notificaiotnArrow.setImageResource(R.drawable.arrow_black)
                notificationImageView.setImageResource(R.drawable.ic_notification_icon_black)


            }
        }
    }

    override fun onResume() {
        super.onResume()
        hideKeyboard()
        setBanner()
    }

    override fun onStart() {
        super.onStart()
        SharedPreference.setIsKitchen(this@HomeActivityKitchen, true)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.v("--------------------", "-------------- ")

    }

    private fun setBanner() {

        setBannerV()
        banner.observe(this, Observer {
            try {
                Picasso.get().load(it).into(freeimage)
            } catch (ex: Exception) {
                freeimage.setImageResource(R.drawable.na)
            }
        })
//        CoroutineScope(Dispatchers.IO).launch {
//            val commonAPI = CommonApi()
//            commonAPI.getBanner(this@HomeActivityKitchen, this@HomeActivityKitchen)
//        }
    }

    private fun setBannerV() {
//        val urlLine: String = APIConstant().getApiBaseUrl(APIConstant.BANNER)
        val commonApi = CommonApi()
        try {
            commonApi.getBanner(this@HomeActivityKitchen, object : EventHandler {
                override fun onSuccess() {
                    super.onSuccess()
//                    val _banner = getBanner(rss.toString())
                    banner.value =  APIConstant.BANNER_URL
                }
            })

        } catch (ex: Exception) {
//            setBannerV()
        }

    }
//    private fun setBannerV() {
//        val urlLine: String = APIConstant().getApiBaseUrl(APIConstant.BANNER)
//        CoroutineScope(Dispatchers.IO).launch {
//            val rss = NetworkUtility.APIrequest(urlLine)
//            withContext(Dispatchers.Main) {
////                val commonApi = CommonApi()
//                try {
//                    val _banner = getBanner(rss.toString())
//                    banner.value = _banner
//                } catch (ex: Exception) {
//                    setBannerV()
//                }
//
//                // call to UI thread
//                //isLoadingData = false
//                //showProgress.value = false
//            }
//        }
//    }

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

    override fun onSuccess() {
        super.onSuccess()
        val imageView = findViewById<ImageView>(R.id.freeimage)
        try {
            Picasso.get().load(APIConstant.BANNER_URL).into(imageView)
        } catch (ex: Exception) {
            imageView.setImageResource(R.drawable.na)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val manager = supportFragmentManager
//        var prevFragment = manager.findFragmentByTag(fragment::class.java.simpleName)
//        if (prevFragment == null) // if none were found, create it
//            prevFragment = fragment
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container, fragment, fragment::class.java.simpleName)
//        transaction.addToBackStack(null)
        transaction.commit()
    }

//    private fun notifyMessage() {
//        val checkUnReadArray = ArrayList<Boolean>()
//
//        /*---------------------------------------------*/
//        FirebaseDatabase.getInstance().reference.child(APIConstant.CHATS_ROOM_NEW)
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if (snapshot.hasChildren()) {
//                        for (sp in snapshot.children) {
//                            for (_sp in sp.children) {
//                                //  val read = (sp.value as HashMap<String, Boolean>)["read"]
//                                for (s in _sp.children) {
//                                    val message = s.getValue(Message::class.java)!!
//                                    // returnChatFalse( _sp,    checkUnReadArray)
//                                    message.childrenCount = _sp.childrenCount.toString()
//                                    if (message.senderId != "00000000000000"
//                                        && message.childrenCount!!.toInt() > 0
//                                    )
//                                        MessageViewModel.setAnim(true)
//                                }
//                            }
//
//                        }
//                        if (checkUnReadArray.size > 0)
//                            MessageViewModel.setAnim(true)
//                    } else {
////                        mDataBinding.nochattextview.visibility = View.VISIBLE
//                    }
//
//                }
//
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//            })
//        /*---------------------------------------------*/
//    }

//    private fun returnChatFalse(_sp: DataSnapshot?, checkUnReadArray: ArrayList<Boolean>) {
//        for (s in _sp!!.children) {
//            val message = s.getValue(Message::class.java)!!
//            if (!message.read) {
//                message.childrenCount = message.childrenCount!!.toInt().plus(1).toString()
//                checkUnReadArray.add(false)
//            }
//        }
//    }


}
