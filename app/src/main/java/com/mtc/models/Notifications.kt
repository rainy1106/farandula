package com.mtc.models

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

//{"result":[{"notification_id":"1","notification_title":"ORDER UPDATED","notification":"Customer has updated Order",
//    "send_to":"0,1","send_by":"1","send_by_name":"Seat A",
//    "image":"https:\/\/codezens.com\/farandula\/img\/company\/logo.png","status":"1",
//    "datetime":"2022-08-01 15:35:13",
//    "entrydt":"2022-08-01 10:05:13","time_ago":"Hace 3 minutoss"}],"message":"success","status":1}
class Notifications {

    data class Response(
        var status: Int,
        var message: String,
        var result: ArrayList<ResultNoti>
    )

    data class ResultNoti(
        var notification_id: String,
        var notification_title: String,
        var notification: String,
        var send_to: String,
        var send_by: String,
        var send_by_name: String,
        var image: String,
        var status: String,
        var datetime: String,
        var entrydt: String,
        var time_ago: String,
        var order_id: String
    ) {

        @SuppressLint("SimpleDateFormat")
        fun getTime(): String? {
            val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val outputFormat: DateFormat = SimpleDateFormat("KK:mm a")
            return inputFormat.parse(datetime)?.let { outputFormat.format(it) }
        }

        fun getTDate(): String? {
            return try {
                val simpleDateFormat =
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(datetime)
                SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(simpleDateFormat)
            } catch (ex: Exception) {
                datetime
            }
        }
    }
}