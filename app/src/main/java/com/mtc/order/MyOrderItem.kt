package com.mtc.order

data class MyOrderItem(
    val product_id: String,
    val restaurant_id: String,
    val category_id: String,
    val product_name: String,
    var price: String,
    val description: String,
    var image: String,
    val status: String,
    val entrydt: String
)