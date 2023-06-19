package com.mtc.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mtc.R
import com.mtc.api.APIConstant
import com.mtc.general.SharedPreference
import com.mtc.interfaces.EventHandler
import com.mtc.utils.SimpleDividerItemDecoration
import kotlinx.android.synthetic.main.dialog_restabse_row.view.*
import kotlinx.android.synthetic.main.dialog_selection_res_tab_sea.*


class DialogSelectionRes(
    activity: Activity?,
    selectionClass: ArrayList<SelectionClass>,
    var mEventHandler: EventHandler
) :
    Dialog(activity!!) {

    var selectionClassList = selectionClass


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_selection_res_tab_sea)
        setWindow()

        recyclerViewMyDialogResTabSe.adapter =
            MyDialogResTabSeAdapter(selectionClassList, mEventHandler)
        recyclerViewMyDialogResTabSe.visibility = View.VISIBLE
        //noTextView.visibility = View.GONE
        recyclerViewMyDialogResTabSe.adapter?.let {
            recyclerViewMyDialogResTabSe.adapter?.notifyDataSetChanged()
        }
        val lineDivider = ContextCompat.getDrawable(context, R.drawable.line_divider)
        recyclerViewMyDialogResTabSe.addItemDecoration(
            SimpleDividerItemDecoration(
                context, lineDivider!!
            )
        )
        val layoutManager = LinearLayoutManager(context)
        recyclerViewMyDialogResTabSe.layoutManager = layoutManager

    }

    private fun setWindow() {
        val displayMetrics = DisplayMetrics()
        window?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(window?.attributes)
        val displayWidth = displayMetrics.widthPixels
        val displayHeight = displayMetrics.heightPixels
        val dialogWindowWidth = (displayWidth * 0.8f).toInt()
        val dialogWindowHeight = (displayHeight * 0.8f).toInt()

        lp.width = dialogWindowWidth
        lp.height = dialogWindowHeight
        lp.gravity = Gravity.NO_GRAVITY
        window?.attributes = lp

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    class MyDialogResTabSeAdapter(
        private var selectionList: ArrayList<SelectionClass>,
        private var mEventHandler: EventHandler
    ) :
        RecyclerView.Adapter<MyDialogResTabSeAdapter.ItemViewHolder>() {

        // Holds the views for adding it to image and text
        class ItemViewHolder(itemView: View, val mEventHandler: EventHandler) :
            RecyclerView.ViewHolder(itemView) {
            val textView: TextView = itemView.findViewById(R.id.dialog_textview_row)


            fun bindView(selectionClass: SelectionClass) {
                textView.text = selectionClass.restaurant_name
                itemView.linearLayoutFree.setOnClickListener {
                    SharedPreference.setRestaurantId(itemView.context, selectionClass.restaurant_id)
                    SharedPreference.setRestaurantName(itemView.context, selectionClass.restaurant_name)
                    SharedPreference.setKitchenTax(itemView.context,selectionClass.tax_percent.toFloat())
                    SharedPreference.setKitchenAddress(itemView.context,selectionClass.restaurant_address)
                    APIConstant.tax_percent = selectionClass.tax_percent
                    APIConstant.restaurant_address_cons = selectionClass.restaurant_address
                    mEventHandler.onSuccess("table")
                }
            }


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.dialog_restabse_row, parent, false)
            return ItemViewHolder(view, mEventHandler)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val selectionClass = selectionList[position]
            holder.bindView(selectionClass)
        }

        override fun getItemCount(): Int = selectionList.size
    }

}