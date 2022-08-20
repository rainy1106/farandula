package com.mtc.api

import com.mtc.home.SubCategory

open class APIConstant {

    companion object {

        const val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"
        val ISKITCHEN: String = "isKitchen"
        val FCMUSER: String = "fcmUser"
        val KITCHEN_RESTAURANT: String = "kitchen_restaurant"
        val DATE: String = "date"
        val MESSAGE_ROOM: String = "messages"
        val CHATS_ROOM: String = "chats"
        val CHATS_ROOM_NEW: String = "chats_new"
        val TABLE_ID: String = "table_id"
        val SEAT_ID: String = "seat_id"
        val USER_IN: String = "user_in"
        val RESTAURANT_NAME: String = "restaurant_name"
        val TABLE_NAME: String = "table_name"
        val SEAT_NAME: String = "seat_name"
        val RESTAURANT_ID: String = "restaurant_id"
        val CAT_ID: String = "category_id"
        val ORDER_ID: String = "order_id"
        val SHARED_FILE = "Farandula_Pref"
        const val BASE_IMAGE_URL = "https://datafarandula.com/"
        const val ApiBaseUrl = "https://datafarandula.com/index.php/api/"

        //const val BASE_IMAGE_URL = "https://multitouchscreentables.com/"
        //const val ApiBaseUrl = "https://multitouchscreentables.com/index.php/api/"
        //const val BASE_IMAGE_URL = "https://codezens.com/farandula/"
        //const val ApiBaseUrl = "https://codezens.com/farandula/index.php/api/"
        const val PRODUCT_LIST = "get_products?restaurant_id=1&category_id=3"
        const val TABLE_LIST = "get_tables?restaurant_id=1"
        const val GET_CATEGORIES = "get_categories?restaurant_id=1"
        const val GET_SUB_CATEGORIES = "get_sub_categories?category_id=4"
        const val GET_TABLES = "get_tables?restaurant_id="

        // const val GET_SEATS_IN_TABLE = "get_seats?restaurant_id=1&table_id=1"
        const val GET_RESTAURANTS = "get_restaurants"
        var ADD_TO_CART = "add_to_cart?seat_id=1&product_id=1&quantity=1&price=7"
        const val GET_CART = "get_cart?seat_id=1"
        const val CLEAR_CART = "clear_cart?seat_id=1"
        const val REMOVE_ONE_ITEM_FROM_CART = "add_to_cart?seat_id=1&product_id=1&quantity=0"
        const val USER_LOGIN = ""

        //        var PLACE_AN_ORDER = ""
        var categoriesList = HashMap<String, ArrayList<SubCategory>>()

        /*KITCHEN*/
        const val VERIFY_CODE = "login?restaurant_code="
        var BANNER = "get_banners"
        var BANNER_URL = ""
        var GET_ORDERS = "get_order?"
        val UPDATE_ORDER = "update_order?"
        val DOWNLOAD_ORDER = "download_order?"
    }


    open fun getApiBaseUrl(PATH_URL: String): String {
        return ApiBaseUrl + PATH_URL
    }
}