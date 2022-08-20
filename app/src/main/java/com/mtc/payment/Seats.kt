package com.mtc.payment

//{"status":true,"message":"","data":{"result":[{"seat_id":"1","restaurant_id":"1",
//    "table_id":"1","seat_name":"Seat A","status":"1","entrydt":"2022-05-20 04:53:43"},
//    {"seat_id":"2","restaurant_id":"1","table_id":"1","seat_name":"Seat B","status":"1","entrydt":"2022-05-20 04:54:34"},
//    {"seat_id":"3","restaurant_id":"1","table_id":"1","seat_name":"Seat C","status":"1","entrydt":"2022-05-20 14:54:31"},
//    {"seat_id":"4","restaurant_id":"1","table_id":"1","seat_name":"Seat D","status":"1","entrydt":"2022-05-20 14:54:52"}]}}
class Seats {


    var sel_value: Int = 0
    var seat_id: String? = null
    var restaurant_id: String? = null
    var table_id: String? = null
    var seat_name: String? = null
    var status: String? = null
    var entrydt: String? = null
    var isSelected: Boolean? = null
    var cost: String? = null
//    var booleanArray = ArrayList<BooleanArray>()
//    var isEnable: Boolean? = null

    constructor()
    constructor(
        seat_id: String,
        restaurant_id: String,
        table_id: String,
        seat_name: String,
        status: String,
        entrydt: String,
        isSelected: Boolean,
        cost: String//,        isEnable: Boolean
    ) {
        this.seat_id = seat_id
        this.restaurant_id = restaurant_id
        this.table_id = restaurant_id
        this.seat_name = seat_name
        this.status = status
        this.entrydt = entrydt
        this.isSelected = isSelected
        this.cost = cost
//        this.booleanArray = booleanArray
        //  this.isEnable = isEnable
    }
}

//class BooleanArray {
//    var seat_id: String = ""
//    var seat_boolean: Boolean = false
//
//    constructor()
//    constructor(seat_id: String, seat_boolean: Boolean) {
//        this.seat_id = seat_id
//        this.seat_boolean = seat_boolean
//    }
//}