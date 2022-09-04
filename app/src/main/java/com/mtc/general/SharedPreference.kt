package com.mtc.general

import android.content.Context
import android.content.SharedPreferences
import com.mtc.api.APIConstant
import com.mtc.kitchen.VerifyCode

object SharedPreference {

    private val sharedPrefFile = APIConstant.SHARED_FILE
    private fun getPref(context: Context): SharedPreferences.Editor? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        return sharedPreferences.edit()
    }

    fun setRestaurantId(context: Context, restaurantId: String) {
        val editor = getPref(context)
        if (editor != null) {
            editor.putString(APIConstant.RESTAURANT_ID, restaurantId)
            editor.apply()
            editor.commit()
        }

    }

    fun setCatId(context: Context, catId: String) {
        val editor = getPref(context)
        if (editor != null) {
            editor.putString(APIConstant.CAT_ID, catId)
            editor.apply()
            editor.commit()
        }

    }

    fun setRestaurantName(context: Context, restaurantName: String) {
        val editor = getPref(context)
        if (editor != null) {
            editor.putString(APIConstant.RESTAURANT_NAME, restaurantName)
            editor.apply()
            editor.commit()
        }
    }

    fun getRestaurantName(context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        return sharedPreferences.getString(APIConstant.RESTAURANT_NAME, "")
    }

    fun getRestaurantId(context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        return sharedPreferences.getString(APIConstant.RESTAURANT_ID, "1")
    }

    fun getCatId(context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        return sharedPreferences.getString(APIConstant.CAT_ID, "1")
    }

    fun setOneTimeLogin(context: Context, isUserIn: Boolean) {
        val editor = getPref(context)
        if (editor != null) {
            editor.putBoolean(APIConstant.USER_IN, isUserIn)
            editor.apply()
            editor.commit()
        }
    }

    fun getUserStatus(context: Context): Boolean {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(APIConstant.USER_IN, false)
    }

    /*Seat*/
    fun setSeatId(context: Context, seatId: String) {
        val editor = getPref(context)
        if (editor != null) {
            editor.putString(APIConstant.SEAT_ID, seatId)
            editor.apply()
            editor.commit()
        }

    }

    fun getSeatId(context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        return sharedPreferences.getString(APIConstant.SEAT_ID, "1")
    }

    /**/


    /*table*/
    fun setTableId(context: Context, seatId: String) {
        val editor = getPref(context)
        if (editor != null) {
            editor.putString(APIConstant.TABLE_ID, seatId)
            editor.apply()
            editor.commit()
        }

    }

    fun getTableId(context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        return sharedPreferences.getString(APIConstant.TABLE_ID, "")
    }

    fun setTableName(context: Context, restaurantName: String) {
        val editor = getPref(context)
        if (editor != null) {
            editor.putString(APIConstant.TABLE_NAME, restaurantName)
            editor.apply()
            editor.commit()
        }
    }

    fun getTableName(context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        return sharedPreferences.getString(APIConstant.TABLE_NAME, "")
    }

    fun setDate(context: Context, date: String) {
        val editor = getPref(context)
        if (editor != null) {
            editor.putString(APIConstant.DATE, date)
            editor.apply()
            editor.commit()
        }
    }

    fun getDate(context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        return sharedPreferences.getString(APIConstant.DATE, "")
    }


    fun setSeatName(context: Context, seat: String) {
        val editor = getPref(context)
        if (editor != null) {
            editor.putString(APIConstant.SEAT_NAME, seat)
            editor.apply()
            editor.commit()
        }
    }

    fun getSeatName(context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        return sharedPreferences.getString(APIConstant.SEAT_NAME, "")
    }

    /*tale*/
    fun clear(context: Context) {
        val editor = getPref(context)
        editor?.clear()
        editor?.apply()
        editor?.commit()
    }

    fun setRestaurantKitchen(context: Context, data: VerifyCode.Result) {
        val editor = getPref(context)
        if (editor != null) {
            editor.putString(APIConstant.KITCHEN_RESTAURANT, data.restaurant_id)
            editor.apply()
            editor.commit()
        }
    }

    fun getRestaurantKitchen(context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        return sharedPreferences.getString(APIConstant.KITCHEN_RESTAURANT, "")
    }

//    fun setOrderId(context: Context, orderId: String) {
//        val editor = getPref(context)
//        if (editor != null) {
//            editor.putString(APIConstant.ORDER_ID, orderId)
//            editor.apply()
//            editor.commit()
//        }
//    }

//    fun getOrderId(context: Context): String? {
//        val sharedPreferences: SharedPreferences =
//            context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
//        return sharedPreferences.getString(APIConstant.ORDER_ID, "")
//    }

    fun setIsKitchen(context: Context, isKitchen: Boolean) {
        val editor = getPref(context)
        if (editor != null) {
            editor.putBoolean(APIConstant.ISKITCHEN, isKitchen)
            editor.apply()
            editor.commit()
        }
    }

    fun isKitchen(context: Context): Boolean? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(APIConstant.ISKITCHEN, false)
    }

//    fun setUserFCMToken(mContext: Context, fcmToken: String?) {
//        val editor = getPref(mContext)
//        if (editor != null) {
//            editor.putString(APIConstant.FCMUSER, fcmToken)
//            editor.apply()
//            editor.commit()
//        }
//    }
//
//    fun getFCMUserToken(mContext: Context): String? {
//        val sharedPreferences: SharedPreferences =
//            mContext.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
//        return sharedPreferences.getString(APIConstant.FCMUSER, "")
//    }
}