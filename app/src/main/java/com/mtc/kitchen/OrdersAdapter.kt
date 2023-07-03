package com.mtc.kitchen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mtc.R
import com.mtc.api.APIConstant
import com.mtc.interfaces.onResponseAPI
import com.mtc.utils.RoundedCornersTransformation
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.order_row_kitchen.view.*
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

class OrdersAdapter(
    private var orderList: ArrayList<OrderListItem.Result>,
    private var onResponse: onResponseAPI
) : RecyclerView.Adapter<ViewOHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewOHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.order_row_kitchen, parent, false)
        return ViewOHolder(view)
    }

    override fun onBindViewHolder(holder: ViewOHolder, position: Int) {

        val order = orderList[position]
        holder.bind(order, onResponse)
    }


    override fun getItemCount(): Int {
        return orderList.size
    }

}

class ViewOHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {


    @SuppressLint("SetTextI18n")
    fun bind(order: OrderListItem.Result, onResponse: onResponseAPI) {


        itemView.moreDetailsId.setOnClickListener {
            onResponse.onSuccessKitchenOrderListItemRow(order)
        }
        itemView.linearLayoutDetailsRow.setOnClickListener {
            onResponse.onSuccessKitchenOrderListItemRow(order)
        }

        itemView.tablenamekitchen.text = order.getTableName()
        itemView.subtablenamekitchen.text = order.getSubTableName()
        itemView.datekitchen.text = order.getTDate()
        itemView.dateTimeKitchen.text = order.getTime()
        itemView.cardx2.text = "X2 extra\t"
        itemView.textViewTotalKitchen.text = "$ " + setTotal(order.cart)
        itemView.subtablenameOnly.text = order.getSubTableNameOnly()
        itemView.generalNoteKitchenText.text = order.getGeneralNote()


        val card = order.cart
        if (card.size == 0) {
            itemView.linearLayout1.visibility = View.INVISIBLE
            itemView.linearLayout2.visibility = View.INVISIBLE
        } else if (card.size == 1) {
            itemView.linearLayout1.visibility = View.VISIBLE
            itemView.linearLayout2.visibility = View.INVISIBLE
            itemView.textViewPro1.text = card[0].product_name
            itemView.textViewProPrice1.text =
                "$ " + card[0].price + "  \t\t\t\t  " + "Qty:" + card[0].quantity
            try {
                Picasso.get().load(APIConstant.BASE_IMAGE_URL + card[0].image)
                    .transform(RoundedCornersTransformation())
                    .error(R.drawable.na)
                    .resize(80, 80)
                    .into(itemView.product_image_1)
            } catch (ex: Exception) {
                itemView.product_image_1.setImageResource(R.drawable.na)
            }
        } else if (card.size == 2) {
            itemView.linearLayout2.visibility = View.VISIBLE
            itemView.linearLayout1.visibility = View.VISIBLE

            itemView.textViewPro1.text = card[0].product_name
            itemView.textViewProPrice1.text =
                "$ " + card[0].price + "  \t\t\t\t\t  " + "Qty:" + card[0].quantity
            try {
                Picasso.get().load(APIConstant.BASE_IMAGE_URL + card[0].image)
                    .transform(RoundedCornersTransformation())
                    .resize(80, 80)
                    .error(R.drawable.na)
                    .into(itemView.product_image_1)
            } catch (ex: Exception) {
                itemView.product_image_1.setImageResource(R.drawable.na)
            }


            itemView.textViewPro2.text = card[1].product_name
            itemView.textViewProPrice2.text =
                "$ " + card[1].price + "  \t\t\t\t\t " + "Qty:" + card[1].quantity
            try {
                Picasso.get().load(APIConstant.BASE_IMAGE_URL + card[1].image)
                    .transform(RoundedCornersTransformation())
                    .resize(80, 80)
                    .error(R.drawable.na)
                    .into(itemView.product_image_2)
            } catch (ex: Exception) {
                itemView.product_image_2.setImageResource(R.drawable.na)
            }
        } else if (card.size > 2) {
            itemView.linearLayout2.visibility = View.VISIBLE
            itemView.linearLayout1.visibility = View.VISIBLE

            itemView.textViewPro1.text = card[0].product_name
            itemView.textViewProPrice1.text =
                "$ " + card[0].price + "  \t\t\t\t\t  " + "Qty:" + card[0].quantity
            try {
                Picasso.get().load(APIConstant.BASE_IMAGE_URL + card[0].image)
                    .transform(RoundedCornersTransformation())
                    .resize(80, 80)
                    .error(R.drawable.na)
                    .into(itemView.product_image_1)
            } catch (ex: Exception) {
                itemView.product_image_1.setImageResource(R.drawable.na)
            }


            itemView.textViewPro2.text = card[1].product_name
            itemView.textViewProPrice2.text =
                "$ " + card[1].price + "  \t\t\t\t\t " + "Qty:" + card[1].quantity
            try {
                Picasso.get().load(APIConstant.BASE_IMAGE_URL + card[1].image)
                    .transform(RoundedCornersTransformation())
                    .resize(80, 80)
                    .error(R.drawable.na)
                    .into(itemView.product_image_2)
            } catch (ex: Exception) {
                itemView.product_image_2.setImageResource(R.drawable.na)
            }
        }


    }

    // endregion
    fun setTotal(card: ArrayList<OrderListItem.Cart>): String? {
        var total = 0.0
        card.forEach {
            //total = total.plus(it.price.toDouble())
            val tempTotal = it.quantity.toInt().times(it.price.toDouble())
            total = total.plus(tempTotal)
        }
        return roundOffDecimal(total).toString()
    }
    fun roundOffDecimal(number: Double): Double {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(number).toDouble()
    }
}