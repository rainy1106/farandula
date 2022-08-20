package com.mtc.payment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mtc.BR
import com.mtc.R
import com.mtc.api.APIConstant
import com.mtc.databinding.FragmentPaymentBinding
import com.mtc.dialog.DialogTip
import com.mtc.general.BaseFragment
import com.mtc.general.BaseViewModel
import com.mtc.general.BaseViewModel.Companion.reportHomeButton
import com.mtc.general.SharedPreference
import com.mtc.general.initViewModel
import com.mtc.interfaces.EventHandler
import kotlinx.android.synthetic.main.fragment_payment.*


class FragmentPayment : BaseFragment<FragmentPaymentBinding, PaymentViewModel>(),
    View.OnClickListener, EventHandler {

    lateinit var adapter: SeatsAdapter

    companion object {
        fun newInstance() = FragmentPayment()
        var _addTip = MutableLiveData<String>()
        var order_id: String = ""
        val placeOrder = PlaceOrder("", "", "")
        var calculatedValue: Double = 0.0
        var costToAdd: String? = null
        var arrayListDuplicate = ArrayList<Seats>()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callGetInSeats()
        callPaymentOptions()
        observers()
        onClickPayYourBill.setOnClickListener(this)
        onClickPlaceOrder.setOnClickListener(this)
        onClickPayTip.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    private fun observers() {

        reportHomeButton.observe {
            grandTotal.text = " $ ${BaseViewModel().roundOffDecimal(it.toDouble())}"
            placeOrder.grandTotal = BaseViewModel().roundOffDecimal(it.toDouble()).toString()
        }
        _addTip.value = "Add Tip"
        _addTip.observe {
            val animation = AnimationUtils.loadAnimation(activity, R.anim.bounce)
            if (it.equals("Remove Tip")) {
                mDataBinding.tipLayout.startAnimation(animation)
                mDataBinding.tipLayout.visibility = View.VISIBLE
                mDataBinding.addTipText.text = "$ ${DialogTip.lastValue}"
            } else {
                mDataBinding.tipLayout.clearAnimation()
                mDataBinding.tipLayout.visibility = View.GONE
                mDataBinding.addTipText.text = "0"
            }
            mDataBinding.onClickPayTip.text = it
        }

        mViewModel._order_id.observe {
            val total = mDataBinding.grandTotal.text.toString().replace("$", "")
            onClickPayYourBill.isClickable = true
            mViewModel.setPaymentType(
                requireContext(),
                resultLauncher,
                mViewModel,
                total,
                object : EventHandler {
                    override fun onComplete() {
                        super.onComplete()
//                        onClickPayYourBill.isClickable = true
                    }
                })

        }

        mViewModel._animationOne.observe {
            val dialogBillProgress = DialogBillProgress(
                requireActivity(), mViewModel._animationOne
            )
            if (it == 1) {
                if (dialogBillProgress.isShowing.not())
                    dialogBillProgress.show()
            } else {
                dialogBillProgress.dismiss()
            }//--

        }
    }

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_payment

    override fun getViewModel(): PaymentViewModel {
        return initViewModel {
            PaymentViewModel()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mViewModel._animationOne.value = 0
    }

    override fun onResume() {
        super.onResume()

    }

    private fun callPaymentOptions() {

        val adapter = PaymentTypeAdapter(mViewModel.getPaymentOptions(requireContext()), this)
        mDataBinding.recyclerViewPaymentOptions.setHasFixedSize(false)
        mDataBinding.recyclerViewPaymentOptions.visibility = View.VISIBLE
        // mDataBinding.noTextView.visibility = View.GONE
        mDataBinding.recyclerViewPaymentOptions.adapter = adapter
        //adapter.onDashboardClickListener = mViewModel
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mDataBinding.recyclerViewPaymentOptions.layoutManager = layoutManager
        //recyclerViewPaymentOptions.scheduleLayoutAnimation()
    }

    private fun callGetInSeats() {

//        if (mViewModel.isInternetConnected(requireContext())) {
//            showLoader(getString(R.string.loading_data))
        try {
            mViewModel.getSeatsInTable(requireContext(), this)
        } catch (ex: Exception) {
            mViewModel.getSeatsInTable(requireContext(), this)
        }
//        } else {
//            mViewModel.showToast(getString(R.string.no_internet_connection), requireContext())
//        }


    }

    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.onClickPayYourBill -> {

                if (placeOrder.grandTotal == "0.0") {
                    Toast.makeText(context, "No seats selected to pay", Toast.LENGTH_SHORT).show()
                    return
                }

                onClickPayYourBill.isClickable = false
                try {
                    mViewModel._animationOne.value = 1
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }

                try {
                    FirebaseDatabase.getInstance().reference.child(APIConstant.CHATS_ROOM_NEW)
                        .child(SharedPreference.getTableId(requireActivity())!!)
                        .ref.removeValue { _, ref -> Log.v("Ref", ref.toString()) }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                try {
                    mViewModel.getPaymentScreen(requireContext(), arrayListDuplicate)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            R.id.onClickPayTip -> {

                if (onClickPayTip.text.equals("Add Tip")) {
                    val dialogTip = DialogTip(requireContext())
                    dialogTip.show()
                } else if (onClickPayTip.text.equals("Remove Tip")) {
//                    _reportHomeButton.value = mViewModel.roundOffDecimal(
//                        placeOrder.grandTotal.toDouble().minus(DialogTip.lastValue)
//                    ).toString()
                    _addTip.value = "Add Tip"
                }
            }
        }
    }


    override fun onSuccessSeats(mList: ArrayList<Seats>) {
        super.onSuccessSeats(mList)

        //_mlist = mList
//        hideLoader()
        mList.forEach {
            if (it.seat_id == SharedPreference.getSeatId(requireContext())) {
                it.cost = placeOrder.grandTotal
                it.isSelected = true
            }
        }
        FirebaseDatabase.getInstance().reference.child(SharedPreference.getTableId(requireContext())!!)
            .child(SharedPreference.getSeatId(requireContext())!!).setValue(mList)
        mViewModel.createBranches(requireContext(), object : EventHandler {
            override fun onSuccessSeats(arrayList: ArrayList<Seats>) {
                super.onSuccessSeats(arrayList)
                try {
                    setAdapter(arrayList)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        })

    }

    fun setAdapter(_mlist: ArrayList<Seats>) {

        arrayListDuplicate = _mlist

        if (_mlist.size > 0) {
            adapter = SeatsAdapter(_mlist, this)
            mDataBinding.recyclerViewSeats.setHasFixedSize(false)
            mDataBinding.recyclerViewSeats.visibility = View.VISIBLE
            mDataBinding.recyclerViewSeats.adapter = adapter
            val layoutManager = GridLayoutManager(requireContext(), 2)
            mDataBinding.recyclerViewSeats.layoutManager = layoutManager
            recyclerViewSeats.scheduleLayoutAnimation()
        } else {
            mDataBinding.recyclerViewSeats.visibility = View.GONE
        }


        placeOrder.grandTotal = "0.0"
        _mlist.forEach {
            val tempTotal = it.cost?.replace("$", " ")?.trim()?.toDouble()
            placeOrder.grandTotal = placeOrder.grandTotal.toDouble().plus(tempTotal!!).toString()
        }

        grandTotal.text = "$ " + mViewModel.calculateTaxValue(placeOrder.grandTotal)
    }

    override fun onSelected(mseat: Seats) {
        super.onSelected(mseat)
//        onClickPayYourBill.text =
//            getString(pay_your_bill) + "$ " + calculatedValue.plus(string.toDouble())
        checkIfExist(mseat)
    }

    private fun checkIfExist(mseat: Seats) {

        FirebaseDatabase.getInstance().reference.child(SharedPreference.getTableId(requireContext())!!)
            .child(mseat.seat_id!!).addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {

//                        val mSeat = snapshot.getValue(Seats::class.java)
//                        _reportHomeButton.value = BaseViewModel().roundOffDecimal(
//                            placeOrder.grandTotal.toDouble()
//                                .plus(mSeat!!.cost!!.toDouble())
//                        ).toString()
                        onSelectSeats(mseat)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "No one selected this seat",
                            Toast.LENGTH_SHORT
                        ).show()
                        mseat.isSelected = false
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


    }


    override fun onDeSelected(mSeat: Seats) {
        super.onDeSelected(mSeat)
        onDeSelectSeats(mSeat)
    }

    override fun onFailure() {
        super.onFailure()
//        hideLoader()
        mDataBinding.recyclerViewSeats.visibility = View.GONE
        //  mDataBinding.noTextView.visibility = View.VISIBLE
    }

    override fun onClickPaymentType(text: CharSequence) {
        super.onClickPaymentType(text)

        when (text) {
            getString(R.string.credit_card) -> {
                placeOrder.paymentType = getString(R.string.credit_card)
                Toast.makeText(requireContext(), "Payment Card", Toast.LENGTH_SHORT).show()
            }
//            getString(R.string.paypal) -> {
//                placeOrder.paymentType = getString(R.string.paypal)
//                Toast.makeText(requireContext(), "choosePaymentPayPal", Toast.LENGTH_SHORT).show()
//            }
            getString(R.string.cash) -> {
                placeOrder.paymentType = getString(R.string.cash)
                Toast.makeText(requireContext(), "Payment Cash", Toast.LENGTH_SHORT).show()
            }
        }

    }


/* firbase code*/


    fun onSelectSeats(selectedSeat: Seats) {

        FirebaseDatabase.getInstance().reference.child(SharedPreference.getTableId(requireContext())!!)
            .child(selectedSeat.seat_id!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (sn in snapshot.children) {
                        val _selectedSeat = sn.getValue(Seats::class.java)
                        if (_selectedSeat?.seat_id == selectedSeat.seat_id)
                            costToAdd = _selectedSeat?.cost // selected ka cost nikala
                    }


                    // respective base seat id me set kro on server
                    FirebaseDatabase.getInstance().reference.child(
                        SharedPreference.getTableId(
                            requireContext()
                        )!!
                    )
                        .child(SharedPreference.getSeatId(requireContext())!!)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {

                                val arrayList = ArrayList<Seats>()
                                for (sp in snapshot.children) {
                                    val _seats = sp.getValue(Seats::class.java)
                                    if (_seats?.seat_id == selectedSeat.seat_id!!) {
                                        _seats.isSelected = true
                                        _seats.cost = costToAdd
                                        sp.ref.setValue(_seats)
                                    }
                                    arrayList.add(_seats!!)

                                }
                                setAdapter(arrayList)
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }
                        })


                    // respective selected seat id me remove kro server pr
                    FirebaseDatabase.getInstance().reference.child(
                        SharedPreference.getTableId(
                            requireContext()
                        )!!
                    )
                        .child(selectedSeat.seat_id!!)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {

                                val arrayList = ArrayList<Seats>()
                                for (sp in snapshot.children) {
                                    val _seats = sp.getValue(Seats::class.java)
                                    if (_seats?.seat_id == selectedSeat.seat_id!!) {
                                        _seats.isSelected = false
                                        _seats.cost = "0.0"
                                        sp.ref.setValue(_seats)
                                    }
                                    // arrayList.add(_seats!!)
                                }
                                // setAdapter(arrayList)
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }
                        })


                    // refresh data
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }


    fun onDeSelectSeats(selectedSeat: Seats) {
//        FirebaseDatabase.getInstance().reference.child(SharedPreference.getTableId(requireContext())!!)
//            .child(mseat.seat_id!!).addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if (snapshot.hasChildren()) {
//                        val mSeat = snapshot.getValue(Seats::class.java)
//                        _reportHomeButton.value = BaseViewModel().roundOffDecimal(
//                            placeOrder.grandTotal.toDouble()
//                                .minus(mSeat!!.cost!!.toDouble())
//                        ).toString()
////                        onSelectSeats(mseat)
//                    } else {
//                        Toast.makeText(
//                            requireContext(),
//                            "No one selected this seat",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//
//            })
/*88888888888888888888888888888888888*/
        FirebaseDatabase.getInstance().reference.child(SharedPreference.getTableId(requireContext())!!)
            .child(SharedPreference.getSeatId(requireContext())!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (sn in snapshot.children) {
                        val _selectedSeat = sn.getValue(Seats::class.java)
                        if (_selectedSeat?.seat_id == selectedSeat.seat_id)
                            costToAdd = _selectedSeat?.cost // selected ka cost nikala
                    }


                    // respective  seat id me set kro on server
                    FirebaseDatabase.getInstance().reference.child(
                        SharedPreference.getTableId(
                            requireContext()
                        )!!
                    )
                        .child(selectedSeat.seat_id!!)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {

                                val arrayList = ArrayList<Seats>()
                                for (sp in snapshot.children) {
                                    val _seats = sp.getValue(Seats::class.java)
                                    if (_seats?.seat_id == selectedSeat.seat_id!!) {
                                        _seats.isSelected = true
                                        _seats.cost = costToAdd
                                        sp.ref.setValue(_seats)
                                    }
//                                    arrayList.add(_seats!!)

                                }
//                                setAdapter(arrayList)
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }
                        })


                    // respective base seat id me remove kro server pr
                    FirebaseDatabase.getInstance().reference.child(
                        SharedPreference.getTableId(
                            requireContext()
                        )!!
                    )
                        .child(SharedPreference.getSeatId(requireContext())!!)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {

                                val arrayList = ArrayList<Seats>()
                                for (sp in snapshot.children) {
                                    val _seats = sp.getValue(Seats::class.java)
                                    if (_seats?.seat_id == selectedSeat.seat_id!!) {
                                        _seats.isSelected = false
                                        _seats.cost = "0.0"
                                        sp.ref.setValue(_seats)
                                    }
                                    // arrayList.add(_seats!!)
                                }
                                // setAdapter(arrayList)
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }
                        })


                    // refresh data
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // parse result and perform action
                Log.v("ActivityResultContracts", "ActivityResultContracts")
            }
        }

}