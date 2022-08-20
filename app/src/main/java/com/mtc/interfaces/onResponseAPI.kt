package com.mtc.interfaces

import com.mtc.kitchen.OrderHistory
import com.mtc.kitchen.OrderListItem

interface onResponseAPI {

    fun onOrderHistory(data: ArrayList<OrderHistory.Result>) {}
    fun onSuccess(arrayList: ArrayList<com.mtc.order.OrderItem>){}
    fun onSuccessKitchen(arrayList: ArrayList<com.mtc.kitchen.OrderItem>){}
    fun onSuccessKitchenOrderListItem(arrayList: ArrayList<OrderListItem.Result> /* = java.util.ArrayList<com.mtc.order.OrderItem> */){}
    fun onFailure(){}
    fun onSuccessKitchenOrderListItemRow(order: OrderListItem.Result){}
}