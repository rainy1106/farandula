package com.mtc.order

import com.mtc.general.BaseViewModel

data class OrderItem(
    val product_id: String,
    val restaurant_id: String,
    val category_id: String,
    val product_name: String,
    var price: String,
    val description: String,
    var image: String,
    val status: String,
    val entrydt: String,
    var isAdded: Boolean,
    var isEnable: Boolean = true,
    var quatity: String,
    var extra_itemsO: String,
    var extra_itemsA: String,
    var instruction: String
) {

    /*.........getQuantity()............*/
    fun getQuantity(): String {
        return quatity + " X" + price.replace("$", "")
    }

    /*......calculatePrice............*/
    fun calculatePrice(): String {
        return BaseViewModel().roundOffDecimal(
            quatity.toInt().times(price.replace("$", "").toDouble())
        ).toString()
    }

    fun getQuantityText(): String {
        return quatity
    }

    fun getExtraItems(): List<String> {
        return if (extra_itemsO.trim().isNotEmpty())
            extra_itemsO.trim().split(",")
        else emptyList()
    }

}

