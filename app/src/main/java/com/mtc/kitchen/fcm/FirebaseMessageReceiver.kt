package com.mtc.kitchen.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mtc.R
import com.mtc.general.SharedPreference
import com.mtc.home.HomeActivity
import com.mtc.kitchen.ActivityEnterCode
import com.mtc.kitchen.FragmentMessages
import com.mtc.kitchen.HomeActivityKitchen
import com.mtc.kitchen.OrdersViewModel
import com.mtc.payment.FragmentPayment
import com.mtc.splash.RegisterActivity
import com.mtc.utils.OrderType
import org.json.JSONObject
import java.io.File


class FirebaseMessageReceiver : FirebaseMessagingService() {


    private var key: String = ""

    // Override onMessageReceived() method to extract the
    // title and
    // body from the message passed in FCM
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Second case when notification payload is
        // received.
        if (remoteMessage.toIntent().extras != null) {
            key = JSONObject(
                remoteMessage.toIntent().extras?.get("message").toString()
            ).getString("key")
            val message = JSONObject(
                remoteMessage.toIntent().extras?.get("message").toString()
            ).getString("message")

            Log.v("message", message)
            Log.v("key", key)
            Log.v("FIREMESSAGE", remoteMessage.toIntent().extras?.get("message").toString())
            if (key == "NEW ORDER") {
                OrdersViewModel.newOrder.postValue(OrderType.NEWORDER.type/*true*/)
                showNotificationNew("Farandula", message)
            }

            if (key == OrderType.UPCOMINGORDER.type)
                OrdersViewModel.newOrder.postValue(OrderType.UPCOMINGORDER.type/*true*/)

            if (key == OrderType.READY.type) {
                OrdersViewModel.newOrder.postValue(OrderType.UPCOMINGORDER.type/*true*/)
            }

            if (key == OrderType.PAID.type) {
                OrdersViewModel.newOrder.postValue(OrderType.UPCOMINGORDER.type/*true*/)
            }


            if (key == "CLEAR") {

                SharedPreference.setCatId(applicationContext, "1")
                FragmentPayment.order_id = ""
                val intent = Intent(applicationContext, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

                OrdersViewModel.showDialog.postValue(true)
            }

            if (key == "MESSAGE") {
                val sendBy = JSONObject(
                    remoteMessage.toIntent().extras?.get("message").toString()
                ).getString("send_by")
                val table_id = JSONObject(
                    remoteMessage.toIntent().extras?.get("message").toString()
                ).getString("table_id")
                FragmentMessages.new_message.postValue(
                    JSONObject(
                        remoteMessage.toIntent().extras?.get(
                            "message"
                        ).toString()
                    )
                )
                HomeActivityKitchen._messageHome.postValue(sendBy)
            }


            // user
            if (key == "ORDER HAS ACCEPTED") {
                OrdersViewModel.newOrder.postValue(OrderType.ACCEPTED.type/*true*/)
                OrdersViewModel.userUpdates.postValue("ORDER HAS ACCEPTED")
                showNotificationNew("Farandula", message)
            }
            if (key == "ORDER IS READY") {
                OrdersViewModel.userUpdates.postValue("ORDER IS READY")
            }
//            if (key == "ORDER UPDATED") {
//                OrdersViewModel.newOrder.postValue(OrderType.UPCOMINGORDER.type/*true*/)
//            }
            if (key == "LOGOUT" && SharedPreference.isKitchen(this) == true) {
//                SharedPreference.clear(applicationContext)
//                clearApplicationData()
                val intent = Intent(applicationContext, ActivityEnterCode::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            if (key == "LOGOUT" && SharedPreference.isKitchen(this) != true) {
//                SharedPreference.clear(applicationContext)
//                clearApplicationData()
                val intent = Intent(applicationContext, RegisterActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }

        }
    }

    private fun clearApplicationData() {
        val cache: File = cacheDir
        val appDir = File(cache.parent)
        if (appDir.exists()) {
            val children: Array<String> = appDir.list()
            for (s in children) {
                if (s != "lib") {
                    deleteDir(File(appDir, s))
                    Log.i(
                        "EEEEEERRRRRRROOOOOOORRRR",
                        "**************** File /data/data/APP_PACKAGE/$s DELETED *******************"
                    )
                }
            }
        }
    }

    private fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        return dir!!.delete()
    }

    // Method to get the custom Design for the display of
    // notification.
    private fun getCustomDesign(
        title: String?,
        message: String?
    ): RemoteViews {
        val remoteViews = RemoteViews(
            applicationContext.packageName,
            R.layout.notification
        )
        remoteViews.setTextViewText(R.id.title, title)
        remoteViews.setTextViewText(R.id.message, message)
        remoteViews.setImageViewResource(
            R.id.icon,
            R.mipmap.ic_launcher
        )
        return remoteViews
    }

    // [START on_new_token]
    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
//    override fun onNewToken(token: String) {
//        Log.d(TAG, "Refreshed token: $token")
//
//        // If you want to send messages to this application instance or
//        // manage this apps subscriptions on the server side, send the
//        // FCM registration token to your app server.
//        sendRegistrationToServer(token)
//    }
    // [END on_new_token]

//    private fun scheduleJob() {
//        // [START dispatch_job]
//        val work = OneTimeWorkRequest.Builder(MyWorker::class.java)
//            .build()
//        WorkManager.getInstance(this)
//            .beginWith(work)
//            .enqueue()
//        // [END dispatch_job]
//    }

    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

//    private fun sendRegistrationToServer(token: String?) {
//        // TODO: Implement this method to send token to your app server.
//        Log.d(TAG, "sendRegistrationTokenToServer($token)")
//    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }

//    internal class MyWorker(appContext: Context, workerParams: WorkerParameters) :
//        Worker(appContext, workerParams) {
//        override fun doWork(): Result {
//            // TODO(developer): add long running task here.
//            return Result.success()
//        }
//    }

    // [END on_new_token]


    fun showNotificationNew(title: String?, message: String?) {

        val mMediaPlayer = MediaPlayer.create(this, R.raw.notification)
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mMediaPlayer.isLooping = false
        mMediaPlayer.start()
        val channel_id = "notification_channel"

        val vibrationPattern = longArrayOf(2000, 2000, 2000, 2000)
        val soundUri = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + packageName + "/raw/notification.mp3"
        )
//        val soundUri =
//            Uri.parse("android.resource://com.mtc.farandula/raw/notification");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val attr = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            val channelName: CharSequence = channel_id
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel =
                NotificationChannel(channel_id, channelName, importance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.setSound(soundUri, attr)
            notificationChannel.vibrationPattern = vibrationPattern
            notificationManager.createNotificationChannel(notificationChannel)
            notificationManager.notify(0, builder(title, message))
        }


    }

    fun builder(title: String?, message: String?): Notification {
        // Assign channel ID
        // RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        // Pass the intent to switch to the MainActivity
        val intent: Intent = if (SharedPreference.isKitchen(this) == true) {
            Intent(this, HomeActivityKitchen::class.java)
        } else {
            Intent(this, HomeActivity::class.java)
        }
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        // Pass the intent to PendingIntent to start the
        // next Activity
        var pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_MUTABLE
        )
        val channel_id = "notification_channel"
        val vibrationPattern = longArrayOf(2000, 2000, 2000, 2000)
        val soundUri = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + packageName + "/raw/notification.mp3"
        )
        if (key == "LOGOUT") {
            pendingIntent = null
        }
        with(NotificationCompat.Builder(applicationContext, channel_id)) {
            setContentTitle(title)
            setContentText(message)
            setSmallIcon(com.mtc.R.mipmap.ic_launcher)
            setAutoCancel(true)
            setVibrate(vibrationPattern)
            setSound(soundUri)
            setContentIntent(pendingIntent)
            return build()
        }
    }

    // Method to display the notifications
    fun showNotification(title: String?, message: String?) {
        // Pass the intent to switch to the MainActivity
        val intent: Intent = if (SharedPreference.isKitchen(this) == true) {
            Intent(this, HomeActivityKitchen::class.java)
        } else {
            Intent(this, HomeActivity::class.java)
        }


        // Assign channel ID
        val channel_id = "notification_channel"
        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
        // the activities present in the activity stack,
        // on the top of the Activity that is to be launched
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // Pass the intent to PendingIntent to start the
        // next Activity
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_MUTABLE
        )
        //Define sound URI
        val soundUri =
            Uri.parse("android.resource://my.package.name/raw/notification")// RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(
            applicationContext,
            channel_id
        )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentText(message)
            .setSubText(message)
            .setAutoCancel(true)
            .setLights(Color.parseColor("#ffb400"), 50, 10)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setSound(soundUri)
            .setVibrate(
                longArrayOf(
                    1000, 1000, 1000,
                    1000, 1000
                )
            )
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        // A customized design for the notification can be
        // set only for Android versions 4.1 and above. Thus
        // condition for the same is checked here.
        builder = builder.setContent(getCustomDesign(title, message))
        // Create an object of NotificationManager class to
        // notify the
        // user of events that happen in the background.
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Check if the Android Version is greater than Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channel_id, "web_app", NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(
                notificationChannel
            )
        }
        notificationManager.notify(0, builder.build())
    }
}