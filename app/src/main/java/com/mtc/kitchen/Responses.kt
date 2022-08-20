//package com.mtc.kitchen
//
//import android.util.Log
//import com.google.gson.annotations.SerializedName
//
//class Responses(
//
//    @SerializedName("status")
//    val status: Boolean,
//
//    @SerializedName("message")
//    val message: String,
//
//    @SerializedName("data")
//    val data: Data
//
//) {
//    override fun toString(): String {
//        Log.v("toString", "Responses(status=$status, message='$message', data=$data)")
//        return "Responses(status=$status, message='$message', data=$data)"
//    }
//}
//
//class Data(
//    @SerializedName("result")
//    val result: ArrayList<OrderItem>
//)
//
