package com.mtc.kitchen

import android.annotation.SuppressLint
import com.mtc.utils.OrderType
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class OrderListItem {

    data class Response(
        var status: Boolean,
        var message: String,
        var data: Data
    )


    data class Data(
        var result: ArrayList<Result>
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
        var general_note: String,
        var sub_total: String,
        var discount: String,
        var txn_amount: String,
        var txn_id: String,
        var payment_mode: String,
        var payment_by: String,
        var payment_status: String,
        var status: String,
        var promocode: String,
        var entrydt: String,
        var seat_name: String,
        var table_name: String,
        var reviewed: String,
        var time_ago: String,
        var cart: ArrayList<Cart>
    ) {
        @SuppressLint("SimpleDateFormat")
        fun getTime(): String? {
            val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val outputFormat: DateFormat = SimpleDateFormat("KK:mm a")
            return inputFormat.parse(datetime)?.let { outputFormat.format(it) }
        }


        fun getSubtotal(): String {
            return "$\t$sub_total"
        }


        fun getTableName(): String {
            // val table = table_name.replace("Table", "").trim()
            return "Order From :\t$table_name"
        }

        fun getSubTableName(): String {
            val seat = seat_name.replace("Seat", "").trim()
            return "Sub Table :\t$seat"
        }

        fun getSubTableNameOnly(): String {
            return seat_name.replace("Seat", "").trim()
        }

        fun getGeneralNote(): String {
            return if (general_note.isEmpty().not())
                "General Note : ".plus(general_note)
            else "General Note : No notes available"
        }

        fun getTDate(): String? {
            return try {
                val simpleDateFormat =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date)
                SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(simpleDateFormat)
            } catch (ex: Exception) {
                date
            }
        }

        fun setCard(): String {
            return "X2 Items"
        }

        fun getOrderStatus(): String {
            return when (status) {
                OrderType.UPCOMINGORDER.type -> OrderType.UPCOMINGORDER.name
                OrderType.ACCEPTED.type -> OrderType.ACCEPTED.name
                OrderType.READY.type -> OrderType.READY.name
                OrderType.ONLYPAID.type -> OrderType.ONLYPAID.type
                else -> {
                    ""
                }
            }

        }
    }


    data class Cart(
        var order_cart_id: String,
        var remark: String,
        var extra_items: String,
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
    ) {
        fun getTPrice(): String {
            return "$ ${(price.toDouble() * 100.0).roundToInt() / 100.0}"
        }

        fun getTRemark(): String {
            return "Remark :${remark}"
        }

        fun getTExtra(): String {
            return "Extra Items :${extra_items}"
        }
    }


}