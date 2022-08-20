package com.mtc.models

class UpdateReponse {


    data class Response(
        var status: Int,
        var message: String,
        var result: Result
    )


    data class Result(
        var order_id: String,
        var restaurant_id: String,
        var table_id: String,
        var seat_id: String,
        var status: String,
        var date: String,
        var datetime: String,
        var extra_items: String,
        var instractions: String,
        var sub_total: String,
        var discount: String,
        var txn_amount: String,
        var txn_id: String,
        var payment_mode: String,
        var payment_by: String,
        var payment_status: String,
        var promocode: String,
    )

}


