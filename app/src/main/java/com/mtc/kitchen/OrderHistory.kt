package com.mtc.kitchen

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.SimpleDateFormat

class OrderHistory {


    data class OrderHistoryRoot(
        val status: Boolean,
        val message: String,
        var data: Data
    )

    data class Cart(
        var order_cart_id: String,
        var order_id: String,
        var seat_id: String,
        var product_id: String,
        var quantity: String,
        var price: String,
        var status: String,
        var entrydt: String,
        var amount: String,
        var product_name: String,
        var image: String
    )

    data class Result(
        var order_id: String,
        var restaurant_id: String,
        var table_id: String,
        var seat_id: String,
        var date: String,
        var datetime: String,
        var extra_items: String,
        var instractions: String,
        var sub_total: String,
        var discount: String,
        var txn_amount: String,
        var txn_id: String,
        var payment_status: String,
        var status: String,
        var promocode: String,
        var entrydt: String,
        var table_name: String,
        var seat_name: String,
        var reviewed: String,
        var time_ago: String,
        var paid_amount: String,
        var cart: java.util.ArrayList<Cart>
    ) {

        fun getSubTotal(): String {
            return "$\t$paid_amount"
        }


        fun tableName(): String {
            return "Order From : \t$table_name"
        }

        fun seatName(): String {
            return "Sub Table : \t$seat_name"
        }

        fun getQty(): String {
            val s1 = cart.size.toString()//Random.nextInt(0, 5).toString()
            val s2 = "\tItem"
            return s1.plus(s2)
        }

        @SuppressLint("SimpleDateFormat")
        fun getTime(): String? {
            val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val outputFormat: DateFormat = SimpleDateFormat("KK:mm a")
            return inputFormat.parse(datetime)?.let { outputFormat.format(it) }
        }

        fun getLStatus(): String {
            return "Status : $status"
        }
    }

    data class Data(
        var result: ArrayList<Result>
    )
}