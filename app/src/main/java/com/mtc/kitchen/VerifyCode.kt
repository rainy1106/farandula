package com.mtc.kitchen

//{"status":true,"message":"","data":{"result":
//    {"restaurant_id":"1","restaurant_code":"FP1","restaurant_name":"Farandula Pizza & Restaurant I",
//    "restaurant_image":"uploads\/restaurant\/logonegropng.png",
//    "status":"1","entrydt":"2022-05-20 14:40:42"}}}
class VerifyCode {


    data class Response(
        var status: Boolean,
        var message: String,
        var data: Data
    )

    data class Data(
        var result: Result
    )

    data class Result(
        var restaurant_id: String,
        var restaurant_code: String,
        var restaurant_name: String,
        var restaurant_image: String,
        var restaurant_address: String,
        var status: String,
        var entrydt: String,
        var tax_percent: String
    )

}


