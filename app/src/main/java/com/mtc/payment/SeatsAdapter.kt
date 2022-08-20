package com.mtc.payment

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mtc.R
import com.mtc.extension.dataBind
import com.mtc.general.SharedPreference
import com.mtc.interfaces.EventHandler
import kotlinx.android.synthetic.main.seats_view_row.view.*


class SeatsAdapter(private val mSeatsList: ArrayList<Seats>,private var mEventHandler: EventHandler) :
    RecyclerView.Adapter<SeatsAdapter.SeatsViewHolder>() {

    var selectedItemPos = -1
    var lastItemSelectedPos = -1

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): SeatsViewHolder {
        return SeatsViewHolder(
            dataBind<SeatsViewHolder>(
                parent,
                R.layout.seats_view_row
            ), mEventHandler
        )
    }

    override fun onBindViewHolder(holder: SeatsViewHolder, position: Int) {
        val mSeat = mSeatsList[position]
        //  holder.bind(mSeat)

        holder.itemView.mySeatATextID.setText(mSeat.seat_name)
        holder.itemView.costID.setText("$ " + mSeat.cost)


//        if (position == selectedItemPos)
//            holder.selectedBg(holder.itemView, mSeat)
//        else
//            holder.defaultBg(holder.itemView, mSeat)

        if (mSeat.isSelected!!) {
            holder.itemView.mySeatATextID.setTextColor(holder.itemView.context.resources.getColor(R.color.white))
            holder.itemView.costID.setTextColor(holder.itemView.context.resources.getColor(R.color.white))
            holder.itemView.seatIdLayout.setBackgroundResource(R.drawable.round_background_black)
        }else{
            holder.itemView.mySeatATextID.setTextColor(holder.itemView.context.resources.getColor(R.color.black))
            holder.itemView.costID.setTextColor(holder.itemView.context.resources.getColor(R.color.black))
            holder.itemView.seatIdLayout.setBackgroundResource(R.drawable.round_background_black_border)
        }

//        checkDisableSeats(holder.itemView, mSeat)

        holder.itemView.setOnClickListener {

            if (holder.itemView.seatIdLayout.isEnabled) {
                if (mSeat.seat_id != SharedPreference.getSeatId(holder.itemView.context)) {
                    mSeat.isSelected = !mSeat.isSelected!!
                    if (!mSeat.isSelected!!)
                        holder.defaultBg(holder.itemView, mSeat)
                    else holder.selectedBg(holder.itemView, mSeat)
                } else {
                    Toast.makeText(holder.itemView.context,"You cannot modify your seat!",Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(holder.itemView.context, "Not Available!",   Toast.LENGTH_SHORT   ) .show()
            }
        }

    }


    override fun getItemCount(): Int = mSeatsList.size

    class SeatsViewHolder(val binding: ViewDataBinding, val mEventHandler: EventHandler) :
        RecyclerView.ViewHolder(binding.root) {


//        fun bind(image: Seats) {
//            binding.setVariable(BR.viewModel, image)
//        }

        fun defaultBg(view: View, mSeat: Seats) {
            view.mySeatATextID.setTextColor(view.context.resources.getColor(R.color.black))
            view.costID.setTextColor(view.context.resources.getColor(R.color.black))
            view.seatIdLayout.setBackgroundResource(R.drawable.round_background_black_border)
            //  if(FragmentPayment.calculatedValue!=0.0)
            mEventHandler.onDeSelected(mSeat/*PaymentViewModel.calculatedValue.minus(view.costID.text.toString().toDouble())*/)
        }

        fun selectedBg(view: View, mSeat: Seats) {
            view.mySeatATextID.setTextColor(view.context.resources.getColor(R.color.white))
            view.costID.setTextColor(view.context.resources.getColor(R.color.white))
            view.seatIdLayout.setBackgroundResource(R.drawable.round_background_black)
            mEventHandler.onSelected(mSeat/*PaymentViewModel.calculatedValue.plus(view.costID.text.toString().toDouble())*/)
        }
    }

    fun checkDisableSeats(view: View, mSeat: Seats) {
        FirebaseDatabase.getInstance().reference.child(SharedPreference.getTableId(view.context)!!)
            .child(mSeat.seat_id!!).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {


                    if (snapshot.hasChildren()) {
                        view.seatIdLayout.isEnabled = true
                    } else {
                        view.mySeatATextID.setTextColor(view.context.resources.getColor(R.color.black))
                        view.costID.setTextColor(view.context.resources.getColor(R.color.black))
                        view.seatIdLayout.setBackgroundResource(R.drawable.round_background_cyan)
                        view.seatIdLayout.isEnabled = false
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("onCancelled", error.message)
                }

            })
    }
}