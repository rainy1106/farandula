package com.mtc.kitchen

import com.google.gson.annotations.SerializedName
import org.json.JSONArray

class OrderItem(
    @SerializedName("order_id")
    val order_id: String,
    @SerializedName("seat_id")
    val seat_id: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("datetime")
    val datetime: String,
    @SerializedName("extra_items")
    val extra_items: String,
    @SerializedName("instractions")
    val instractions: String,
    @SerializedName("sub_total")
    var sub_total: String,
    @SerializedName("discount")
    val discount: String,
    @SerializedName("txn_amount")
    val txn_amount: String,
    @SerializedName("txn_id")
    val txn_id: String,
    @SerializedName("payment_mode")
    val payment_mode: String,
    @SerializedName("payment_status")
    val payment_status: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("promocode")
    val promocode: String,
    @SerializedName("seat_name")
    val seat_name: String,
    @SerializedName("entrydt")
    val entrydt: String,
    @SerializedName("table_name")
    val table_name: String,
    @SerializedName("reviewed")
    val reviewed: String,
    @SerializedName("time_ago")
    val time_ago: String,
    @SerializedName("cart")
    val cart: JSONArray,
) {

    fun getSubtotal(): String {
        return "$\t$sub_total"
    }


    fun getTableName(): String {
        val table = table_name.replace("Table", "").trim()
        return "Order From :\t$table"
    }

    fun getSubTableName(): String {
        val seat = seat_name.replace("Seat", "").trim()
        return "Sub Table :\t$seat"
    }

    fun getSubTableNameOnly(): String {
        return seat_name.replace("Seat", "").trim()
    }
}
