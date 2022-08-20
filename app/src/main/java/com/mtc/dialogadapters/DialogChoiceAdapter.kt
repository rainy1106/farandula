package com.mtc.dialogadapters

import android.content.Context
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.mtc.BR
import com.mtc.R
import com.mtc.extension.dataBind
import com.mtc.models.OrderChoice
import com.mtc.order.OrderListViewModel
import kotlinx.android.synthetic.main.dialog_choice_row.view.*

class DialogChoiceAdapter(
    private var context: Context, private val arrayListOrderChoice: ArrayList<OrderChoice>
) :
    RecyclerView.Adapter<DialogChoiceAdapter.ItemViewHolder>() {

    var count: Int = 0

    class ItemViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(image: OrderChoice) {
            binding.setVariable(BR.viewModel, image)
        }
    }

    // region - Override function
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            dataBind(
                parent,
                R.layout.dialog_choice_row
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val order = arrayListOrderChoice[position]
        holder.bind(order)




        holder.itemView.checkboxID.setOnCheckedChangeListener { compoundButton, b ->

            if (b) {
                if (count < 2) {
                    count = count.plus(1)
                    order.isSelected = b
                    holder.itemView.checkboxID.isChecked = order.isSelected
                    OrderListViewModel.extraItems.add(order.name)
                } else {
                    holder.itemView.checkboxID.isChecked = false
                    Toast.makeText(
                        context,
                        context.getString(R.string.you_cannot_select_more_than_two),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                count = count.minus(1)
                order.isSelected = b
                holder.itemView.checkboxID.isChecked = order.isSelected
                OrderListViewModel.extraItems.remove(order.name)
            }

        }
    }

    override fun getItemCount(): Int = arrayListOrderChoice.size
}