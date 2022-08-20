package com.mtc.kitchen

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.mtc.R
import com.mtc.api.APIConstant
import com.mtc.dialog.DialogDate
import com.mtc.general.BaseViewModel
import com.mtc.general.SharedPreference
import com.mtc.utils.NetworkUtility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class OrderHistoryViewModel : BaseViewModel() {


    private val referenceID: Long = 0
    private var downloadManager: DownloadManager? = null
    private val PERMISSION_REQUEST_CODE = 1

    //   private var networkState: ConnectionModel? = null

    private var _onComplete = MutableLiveData<String>()
    val onComplete: LiveData<String> get() = _onComplete

    private var _dateSelected = MutableLiveData<String>()
    val dateSelected: LiveData<String> get() = _dateSelected


    var historyList = MutableLiveData<ArrayList<OrderHistory.Result>>()

//    fun setNetworkState(connectionModel: ConnectionModel) {
//        networkState = connectionModel
//    }

    fun getOrderHistory(context: Context/*, onResponseAPI: onResponseAPI*/) {
        val urlLine =
            APIConstant.ApiBaseUrl + APIConstant.GET_ORDERS +
                    "restaurant_id=${SharedPreference.getRestaurantKitchen(context)}" +
                    "&date=${SharedPreference.getDate(context)}"
        CoroutineScope(Dispatchers.IO).launch {
            val rss = NetworkUtility.APIrequest(urlLine)
            withContext(Dispatchers.Main) {
                try {
                    val arrayList = getHistoryJson(rss.toString())
                    historyList.value = arrayList
                } catch (ex: Exception) {
                    Toast.makeText(context, "Network Error, Please try again..", Toast.LENGTH_SHORT)
                        .show()
                   // getOrderHistory(context)
                }
                // call to UI thread
                //isLoadingData = false
                //showProgress.value = false
                // usersList.value?.addAll(parseJsonString(rss.toString()))
            }
        }


        /*--------------------------------------*/

//        val commonApi = CommonApi()
//        commonApi.getOrderHistory(context, onResponseAPI)


    }

    private fun getHistoryJson(response: String): ArrayList<OrderHistory.Result> {
        val arrayList = ArrayList<OrderHistory.Result>()
        val _res = JSONObject(response).getBoolean("status")
        if (_res) {
            val gson = Gson()
            val orderHistory =
                gson.fromJson(response, OrderHistory.OrderHistoryRoot::class.java)
            if (orderHistory.status)
                return orderHistory.data.result
            else return arrayList//onResponseAPI.onFailure()
        } else {
            return arrayList//    onResponseAPI.onFailure()
        }
    }


    fun showDateSelector(context: Context) {
        val dialogDate = DialogDate(context, _dateSelected)
        dialogDate.show()
    }

    @SuppressLint("SimpleDateFormat")
    fun setDefaultDate() {
        val cal = Calendar.getInstance()
        _dateSelected.value = formatDate(cal.time)
    }

    fun formatDate(dateString: Date): String {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(dateString)
    }

    fun dowloadPdf(context: Activity) {

        CoroutineScope(Dispatchers.IO).launch {
            val urlLine = APIConstant.ApiBaseUrl +
                    APIConstant.DOWNLOAD_ORDER +
                    "restaurant_id=${SharedPreference.getRestaurantKitchen(context)}" +
                    "&date=${SharedPreference.getDate(context)}"
            val image_uri =
                Uri.parse(urlLine)
            context.registerReceiver(
                receiver,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            )
            var referenceID = DownloadImage(image_uri, context)
        }
    }

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val action = intent.action
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
                if (context != null) {
                    showAlert(context)
                }
                val ImageDownloadQuery = DownloadManager.Query()
                //set the query filter to our previously Enqueued download
                ImageDownloadQuery.setFilterById(referenceID)
                //Query the download manager about downloads that have been requested.
                val cursor: Cursor? = downloadManager?.query(ImageDownloadQuery)
                if (cursor?.moveToFirst() == true) {
                    Toast.makeText(context, DownloadStatus(cursor, context), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun DownloadStatus(cursor: Cursor, context: Context?): String? {

        //column for download  status
        val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
        val status = cursor.getInt(columnIndex)
        //column for reason code if the download failed or paused
        val columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON)
        val reason = cursor.getInt(columnReason)
        var statusText = ""
        var reasonText = ""

        when (status) {
            DownloadManager.STATUS_FAILED -> {
                statusText = "STATUS_FAILED"
                when (reason) {
                    DownloadManager.ERROR_CANNOT_RESUME -> reasonText = "ERROR_CANNOT_RESUME"
                    DownloadManager.ERROR_DEVICE_NOT_FOUND -> reasonText = "ERROR_DEVICE_NOT_FOUND"
                    DownloadManager.ERROR_FILE_ALREADY_EXISTS -> reasonText =
                        "ERROR_FILE_ALREADY_EXISTS"
                    DownloadManager.ERROR_FILE_ERROR -> reasonText = "ERROR_FILE_ERROR"
                    DownloadManager.ERROR_HTTP_DATA_ERROR -> reasonText = "ERROR_HTTP_DATA_ERROR"
                    DownloadManager.ERROR_INSUFFICIENT_SPACE -> reasonText =
                        "ERROR_INSUFFICIENT_SPACE"
                    DownloadManager.ERROR_TOO_MANY_REDIRECTS -> reasonText =
                        "ERROR_TOO_MANY_REDIRECTS"
                    DownloadManager.ERROR_UNHANDLED_HTTP_CODE -> reasonText =
                        "ERROR_UNHANDLED_HTTP_CODE"
                    DownloadManager.ERROR_UNKNOWN -> reasonText = "ERROR_UNKNOWN"
                }
            }
            DownloadManager.STATUS_PAUSED -> {
                statusText = "STATUS_PAUSED"
                when (reason) {
                    DownloadManager.PAUSED_QUEUED_FOR_WIFI -> reasonText = "PAUSED_QUEUED_FOR_WIFI"
                    DownloadManager.PAUSED_UNKNOWN -> reasonText = "PAUSED_UNKNOWN"
                    DownloadManager.PAUSED_WAITING_FOR_NETWORK -> reasonText =
                        "PAUSED_WAITING_FOR_NETWORK"
                    DownloadManager.PAUSED_WAITING_TO_RETRY -> reasonText =
                        "PAUSED_WAITING_TO_RETRY"
                }
            }
            DownloadManager.STATUS_PENDING -> {
                statusText = "STATUS_PENDING"
            }
            DownloadManager.STATUS_SUCCESSFUL -> {
                statusText = "Order history saved Successfully"
                //reasonText = "Filename:\n" + filename;
//                Toast.makeText(
//                    context,
//                    "Download Status:\n$statusText\n$reasonText",
//                    Toast.LENGTH_SHORT
//                ).show()
            }
        }
        return statusText + reasonText
    }

    private fun showAlert(context: Context) {
        val builder1: AlertDialog.Builder = AlertDialog.Builder(context)
//        builder1.setMessage()
        builder1.setTitle("Order history downloaded successfully.")
        builder1.setCancelable(true)

        builder1.setPositiveButton(
            "OK"
        ) { dialog, id -> dialog.dismiss() }

        builder1.setNegativeButton(
            context.getString(R.string.cancle)
        ) { dialog, id -> dialog.dismiss() }

        val alert11: AlertDialog = builder1.create()
        alert11.show()
    }

    private fun DownloadImage(uri: Uri, requireAct: Activity): Long {

        val direct = File(requireAct.getExternalFilesDir(null), "/Farandula_Pdfs")

        if (!direct.exists()) {
            direct.mkdirs()
        }

        val mgr = requireAct.getSystemService(DOWNLOAD_SERVICE) as DownloadManager?

        val downloadUri = uri
        val request = DownloadManager.Request(
            downloadUri
        )
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(sdf.parse(SharedPreference.getDate(requireAct)))
            .replace("-", "") + "_OrderHistory.pdf"
        val filename ="/Farandula_Pdfs/"+currentDate
        request.setAllowedNetworkTypes(
            DownloadManager.Request.NETWORK_WIFI or
                    DownloadManager.Request.NETWORK_MOBILE
        )
            .setAllowedOverRoaming(false).setTitle("Order History pdf...") //Download Manager Title
            .setDescription("Downloading...") //Download Manager description
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
               filename  //Your User define(Non Standard Directory name)/File Name
            )
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        return mgr?.enqueue(request)!!


//        val downloadReference: Long
//        downloadManager = requireAct.getSystemService(DOWNLOAD_SERVICE) as DownloadManager?
//        val request = DownloadManager.Request(uri)
//        //Setting title of request
//        request.setTitle("Pdf Download")
//        //Setting description of request
//        request.setDescription("Downloading.")
//        val sdf = SimpleDateFormat("yyyy-MM-dd")
//        val currentDate = sdf.format(sdf.parse(SharedPreference.getDate(requireAct)))
//            .replace("-", "") + "OrderHistory.pdf"
//        val filename = "/Download/Farandula_Orders/" + currentDate
//        val stringFilePath = Environment.getExternalStorageDirectory().path + filename
/*//        val file = File(stringFilePath);
//        request.setDestinationInExternalPublicDir(
//            requireAct.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
//                .toString() + "/Farandula_Orders/",
//            filename
//        )*/
//        request.setDestinationInExternalPublicDir(stringFilePath, filename)
//        request.allowScanningByMediaScanner()
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//        downloadReference = downloadManager?.enqueue(request)!!
//        return downloadReference
    }
}