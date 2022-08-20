package com.mtc.home

data class Category(
    val category_id: String,
    val restaurant_id: String,
    val category_name: String,
    val category_image: String,
    var status: String,
    val entrydt: String/*,
    val subCategoryList: ArrayList<SubCategory>*/
)
