package com.mtc.order

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.view.*
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mtc.R
import com.mtc.dialogadapters.DialogChoiceAdapter
import com.mtc.extension.dataBind
import com.mtc.general.BaseViewModel
import com.mtc.models.OrderChoice
import com.mtc.utils.SimpleDividerItemDecoration
import kotlinx.android.synthetic.main.dialog_choice.*
import kotlinx.android.synthetic.main.order_list_row.view.*


class OrderListAdapter(private var orderList: MutableList<OrderItem>) :
    RecyclerView.Adapter<ItemViewHolder>() {


    // region - Override function
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(dataBind<ItemViewHolder>(parent, R.layout.order_list_row))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val order = orderList[position]
        holder.bind(order)
//        holder.itemView.linearLayoutAdd.setOnClickListener {
//            when {
//                orderList[position].isAdded -> {
//                    order.isAdded = false
//                    OrderListViewModel.orderListSelected.remove(order)
//                }
//                else -> {
//                    order.isAdded = true
//                    OrderListViewModel.orderListSelected.add(order)
//                }
//            }
//            BaseViewModel.reportHomeButton()
//            notifyItemChanged(position)
//        }
        holder.itemView.onClickPen.setOnClickListener {
            setAlertDialog(holder.itemView, order, position, "pen")
        }

        /*
          this make it to add only one time
          when {
               orderList[position].isAdded -> {
                   holder.itemView.addForOrder.text = holder.itemView.context.getString(R.string.added)
               }
               else -> {
                   holder.itemView.addForOrder.text =
                       holder.itemView.context.getString(R.string.add_for_order)
               }
           }*/


        holder.itemView.linearLayoutAdd.setOnClickListener {
//            if (!(order.isAdded))
            setAlertDialog(holder.itemView, order, position, "image")
//            else {
//                order.isAdded = false
//                for (i in OrderListViewModel.orderListSelected.indices) {
//                    if (order.product_id == OrderListViewModel.orderListSelected[i].product_id)
//                        OrderListViewModel.orderListSelected.removeAt(i)
//                }
//                BaseViewModel.reportHomeButton()
//                notifyItemChanged(position)
//            }
        }

    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    fun setAlertDialog(itemView: View, orderItem: OrderItem, position: Int, type: String) {
        val dialog = Dialog(itemView.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_choice)


        val displayMetrics = DisplayMetrics()
        dialog.window?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        val displayWidth = displayMetrics.widthPixels
        val displayHeight = displayMetrics.heightPixels
        val dialogWindowWidth = (displayWidth * 0.8f).toInt()
        val dialogWindowHeight = WRAP_CONTENT//(displayHeight * 0.9f).toInt()
        lp.width = dialogWindowWidth
        lp.height = dialogWindowHeight
        lp.gravity = Gravity.NO_GRAVITY
        dialog.window?.attributes = lp
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.orderName.text = orderItem.product_name
        dialog.orderDes.text = orderItem.description

        if (orderItem.getExtraItems().isNotEmpty() && type == "image") {
            setVisibility(dialog, View.VISIBLE)
            dialog.recyclerViewDialogChoice.adapter =
                DialogChoiceAdapter(itemView.context, getSamlpeList(orderItem.getExtraItems()))
            dialog.recyclerViewDialogChoice.setHasFixedSize(false)
            val layoutManager = LinearLayoutManager(itemView.context)
            dialog.recyclerViewDialogChoice.layoutManager = layoutManager
            val lineDivider = ContextCompat.getDrawable(itemView.context, R.drawable.line_divider)
            dialog.recyclerViewDialogChoice.addItemDecoration(
                SimpleDividerItemDecoration(itemView.context, lineDivider!!)
            )
        } else {
            setVisibility(dialog, View.GONE)
        }

        dialog.show()
        dialog.choiceCancle.setOnClickListener { dialog.dismiss() }
        dialog.choiceSubmit.setOnClickListener {


//            when {
//                orderList[position].isAdded -> {
//                    orderItem.isAdded = false
//                    OrderListViewModel.orderListSelected.remove(orderItem)
//                }
//                else -> {
//                    orderItem.isAdded = true
//                    OrderListViewModel.orderListSelected.add(orderItem)
//                }
//            }
            orderItem.isAdded = true
            orderItem.extra_itemsA = OrderListViewModel.extraItems.joinToString(separator = "|")
            orderItem.instruction = dialog.instractionEditTextId.text.toString()
            OrderListViewModel.orderListSelected.add(
                OrderItem(
                    orderItem.product_id,
                    orderItem.restaurant_id,
                    orderItem.category_id,
                    orderItem.product_name,
                    orderItem.price,
                    orderItem.description,
                    orderItem.image,
                    orderItem.status,
                    orderItem.entrydt,
                    orderItem.isAdded,
                    orderItem.isEnable,
                    orderItem.quatity,
                    orderItem.extra_itemsO,
                    orderItem.extra_itemsA,
                    orderItem.instruction
                )
            )
            BaseViewModel.reportHomeButton()
            notifyItemChanged(position)

//            OrderListViewModel.instractions = dialog.instractionEditTextId.text.toString()

            OrderListViewModel.extraItems.clear()
            dialog.dismiss()
        }
        dialog.cancelDialog.setOnClickListener {
            OrderListViewModel.instractions = ""
            OrderListViewModel.extraItems.clear()
            dialog.dismiss()
        }
    }


    private fun setVisibility(dialog: Dialog, visible: Int) {
        dialog.orderName.visibility = visible
        dialog.orderDes.visibility = visible
        dialog.sideChoice.visibility = visible
        dialog.recyclerViewDialogChoice.visibility = visible
        dialog.sideChoiceMessage.visibility = visible
    }

    private fun getSamlpeList(mList: List<String>): ArrayList<OrderChoice> {

        val arrayListOrder = ArrayList<OrderChoice>()

        mList.forEach {
            val orderChoice = OrderChoice(it, false)
            arrayListOrder.add(orderChoice)
        }
        return arrayListOrder
    }
    // endregion
}

