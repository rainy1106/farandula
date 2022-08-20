package com.mtc.interfaces

import com.mtc.dialog.SelectionClass
import com.mtc.home.Category
import com.mtc.order.OrderItem
import com.mtc.payment.Seats
import org.json.JSONObject

interface EventHandler {


    fun onClickPaymentType(text: CharSequence) {}
    fun onSuccess(message: String) {}
    fun onSuccess(arrayList: ArrayList<OrderItem>) { /* default implementation */
    }

    fun onFailure() { /* default implementation */
    }

    fun onRemove(/*orderItem: OrderItem*/) { /* default implementation */
    }

    fun onSuccessCategory(arrayList: ArrayList<Category>) { /* default implementation */
    }

    fun onSuccessSelectionClass(
        arrayList: ArrayList<SelectionClass>,
        type: String
    ) { /* default implementation */
    }

    fun onSuccessSeats(arrayList: ArrayList<Seats>) {/* default implementation */
    }

    fun onSelected(b: Seats) {    /* default implementation */
    }

    fun onDeSelected(b: Seats) {    /* default implementation */
    }

    /*KITCHE*/
    fun onSuccess() {}
    fun onFailure(toString: String) {}
    fun onComplete() {}
    fun onSuccess(result: JSONObject) {}
}