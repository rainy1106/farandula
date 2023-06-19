package com.mtc.order

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mtc.BR
import com.mtc.R
import com.mtc.api.APIConstant
import com.mtc.databinding.FragmentConfirmOrderBinding
import com.mtc.dialog.DialogChatUser
import com.mtc.general.BaseFragment
import com.mtc.general.SharedPreference
import com.mtc.general.initViewModel
import com.mtc.home.HomeActivity
import com.mtc.kitchen.OrdersViewModel
import com.mtc.print.starprint.starprntsdk.Communication
import com.mtc.print.starprint.starprntsdk.MainActivity
import com.mtc.utils.SimpleDividerItemDecoration
import kotlinx.android.synthetic.main.fragment_confirm_order.*
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.system.exitProcess

class FragmentConfirmOrder :
    BaseFragment<FragmentConfirmOrderBinding, ConfirmOrderListViewModel>(), View.OnClickListener {
    private var value: String? = ""
    private var mList: ArrayList<OrderItem> = ArrayList()

    companion object {
        fun newInstance() = FragmentConfirmOrder()
        var oldCount = 0
    }

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_confirm_order

    override fun getViewModel(): ConfirmOrderListViewModel {
        return initViewModel {
            ConfirmOrderListViewModel()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDataBinding.viewModel = getViewModel()
//       mList = mViewModel.getMyOrderList()
        setPaymentInfo()
        costObserver()
        onClickAddMoreList.setOnClickListener(this)
        onClickPlaceOrder.setOnClickListener(this)
        sendInstructionButton.setOnClickListener(this)
        finish.setOnClickListener(this)
        printUser.setOnClickListener(this)
        mDataBinding.onClickPlaceOrder.isEnabled = false
        mDataBinding.onClickAddMoreList.isEnabled = true
        userOrderImages.setImageResource(R.drawable.chefimages)
        OrdersViewModel.userUpdates.postValue("")
        //bellOnClick.setOnClickListener(this)

        showKitchenMessageToUser()

//        kitchenMessageLayoutId.setOnClickListener {
//            showKitchenMessageToUser()
//            showAlert()
//        }
    }

    private fun setPaymentInfo() {

        val adapter = PaymentInformationAdapter(/*OrderListViewModel.orderListSelected*/)
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerViewConfirmPaymentInformation.layoutManager = layoutManager
        val lineDivider = ContextCompat.getDrawable(requireContext(), R.drawable.line_divider)
        recyclerViewConfirmPaymentInformation.addItemDecoration(
            SimpleDividerItemDecoration(
                requireContext(),
                lineDivider!!
            )
        )
        recyclerViewConfirmPaymentInformation.adapter = adapter
        recyclerViewConfirmPaymentInformation.scheduleLayoutAnimation()
        mViewModel.updateTotalCost()
    }


    @SuppressLint("SetTextI18n")
    fun costObserver() {
        mViewModel.totalCost.observe {
            totalCost.text = it
            totalCostT.text = "$ ${roundOffDecimal(it.replace("$", "").trim().toDouble())}"
        }
        OrdersViewModel.userUpdates.observe {
            mDataBinding.onClickPlaceOrder.isEnabled = it.equals("ORDER IS READY")
            mDataBinding.onClickAddMoreList.isEnabled = !mDataBinding.onClickPlaceOrder.isEnabled
            if (mDataBinding.onClickPlaceOrder.isEnabled)
                userOrderImages.setImageResource(R.drawable.ready)
            else
                userOrderImages.setImageResource(R.drawable.chefimages)
        }
    }
    private fun roundOffDecimal(number: Double): Double {
        val withTax = (number * SharedPreference.getKitchenTax(requireContext())) / 100
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(number).toDouble().plus(withTax)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.onClickAddMoreList -> {
                replaceFragment(FragmentOrderList.newInstance())
            }
            R.id.printUser -> {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                Communication.CommunicationResult.count = 1;
            }
            R.id.finish -> {
                val intent = Intent(context, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                exitProcess(0)
            }
            R.id.onClickPlaceOrder -> {
                // replaceFragment(FragmentPayment.newInstance())
                //  mViewModel._animationOne.value = 1
            }
            R.id.sendInstructionButton -> {
                //go to chat page
                mViewModel.goToChat(requireActivity())
            }
            R.id.bellOnClick -> {
//                var dialog = DialogNotifications(requireActivity())
//                dialog.show()
            }
        }
    }


    fun showKitchenMessageToUser() {
        val dialog = DialogChatUser(requireActivity())
//        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.bounce);
//        animation.repeatCount = Animation.INFINITE
//        animation.repeatMode = Animation.RESTART
        FirebaseDatabase.getInstance().reference.child(APIConstant.CHATS_ROOM_NEW)
            .child(SharedPreference.getTableId(requireContext())!!)
            .child(SharedPreference.getSeatId(requireContext())!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        if (snapshot.hasChildren())
                            if (!dialog.isShowing)
                                dialog.show()
                    } catch (ex: Exception) {
                        //
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })


    }//------------

//    private fun showAlert() {
//        val builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
//            .create()
//        val view = layoutInflater.inflate(R.layout.customview_layout, null)
//        val userUpdateTitle = view.findViewById<TextView>(R.id.userUpdateTitle)
//        userUpdateTitle.text = value
//        val button = view.findViewById<Button>(R.id.dialogDismiss_button)
//        val buttonC = view.findViewById<Button>(R.id.dialogDismissC_button)
//        builder.setView(view)
//        buttonC.setOnClickListener {
//            builder.dismiss()
//        }
//        button.setOnClickListener {
//            builder.dismiss()
//            val dialogChat = DialogChatUser(requireActivity())
//            dialogChat.show()
//        }
//        builder.setCanceledOnTouchOutside(false)
//        builder.show()
//
//
//    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (DialogChatUser.senderId
            == ""
        )
            signInAnonymously()
    }

    private fun signInAnonymously() {
        var auth: FirebaseAuth = FirebaseAuth.getInstance()
        // [START signing_anonymously]
        auth.signInAnonymously()
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    DialogChatUser.senderId = user?.uid.toString()
                    // setAdapter()
                } else {
                    Log.d("TAG", "signInAnonymously:failure", task.exception)
                    Toast.makeText(
                        context, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        // [END signin_anonymously]
    }

    interface ReturnToFragment {
        fun returnToFragment() {}
    }
}