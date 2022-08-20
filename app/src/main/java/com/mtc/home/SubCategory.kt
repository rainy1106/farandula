package com.mtc.home

data class SubCategory(val sub_category_id: String,
                       val restaurant_id: String,
                       val category_id: String,
                       val sub_category_name: String,
                       var status: String,
                       val entrydt: String,)
