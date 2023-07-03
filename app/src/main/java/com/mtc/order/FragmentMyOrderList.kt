package com.mtc.order

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.mtc.BR
import com.mtc.R
import com.mtc.databinding.FragmentMyorderlistBinding
import com.mtc.dialog.DialogPlaceOrder
import com.mtc.general.BaseFragment
import com.mtc.general.BaseViewModel
import com.mtc.general.SharedPreference
import com.mtc.general.initViewModel
import com.mtc.interfaces.EventHandler
import com.mtc.payment.FragmentPayment
import com.mtc.utils.SimpleDividerItemDecoration
import kotlinx.android.synthetic.main.fragment_myorderlist.*
import org.json.JSONObject

class FragmentMyOrderList : BaseFragment<FragmentMyorderlistBinding, MyOrderListViewModel>(),
    View.OnClickListener, EventHandler {


    companion object {
        fun newInstance() = FragmentMyOrderList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDataBinding.viewModel = getViewModel()
        confirm_order_button.setOnClickListener(this)
        onClickAddMoreList.setOnClickListener(this)
        onClickAddGeneral.setOnClickListener(this)
        setupRecyclerView()
        observers()
    }

    private fun observers() {
        mViewModel.totalCost.observe {
            paymentInfoTotalCost.text = it
            FragmentPayment.placeOrder.grandTotal = it.replace("$", "")
        }


        mViewModel.animationOne.observe {

            if (it == 1) {
                placeOrder()
                try {
                    val dialogPlaceOrder = DialogPlaceOrder(requireActivity(),
                        1,
                        mViewModel._animationOne,
                        object : FragmentConfirmOrder.ReturnToFragment {
                            override fun returnToFragment() {

                            }
                        })
                    dialogPlaceOrder.show()
                } catch (ex: Exception) {
                    mViewModel._animationOne.value = 2
                }
            } else {
                try {
                    val dialogPlaceOrder = DialogPlaceOrder(requireActivity(),
                        2,
                        mViewModel._animationOne,
                        object : FragmentConfirmOrder.ReturnToFragment {
                            override fun returnToFragment() {
                                // placeOrder()
                            }
                        })
                    dialogPlaceOrder.show()
                } catch (ex: Exception) {

                }
            }
        }
    }


    private fun placeOrder() {

        mViewModel.placeOrder(requireContext(), object : EventHandler {
            override fun onSuccess(result: JSONObject) {
                super.onSuccess(result)
                confirm_order_button.isClickable = true
                FragmentPayment.order_id = result.getString("order_id")
                mViewModel.updateOrderList()
                replaceFragment(FragmentConfirmOrder.newInstance())
            }

            override fun onFailure(toString: String) {
                super.onFailure(toString)
                confirm_order_button.isClickable = true
                Toast.makeText(requireContext(), toString, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecyclerView() {

//        if (mViewModel.isInternetConnected(requireContext())) {
        // mList = OrderListViewModel.orderListSelected //mViewModel.getMyOrderList()
        if (OrderListViewModel.orderListSelected.size > 0) {
            val adapter = MyOrderListAdapter(object : EventHandler {
                override fun onRemove(/*orderItem: OrderItem*/) {
//                        mList.remove(orderItem)
//                        OrderListViewModel.orderListSelected = mList
                    setPaymentInfo()
                    if (OrderListViewModel.orderListSelected.size == 0) {
                        replaceFragment(FragmentOrderList.newInstance())
                    }
                }
            })
            mDataBinding.recyclerViewMyOrderList.setHasFixedSize(true)
            mDataBinding.recyclerViewMyOrderList.visibility = View.VISIBLE
            mDataBinding.noTextView.visibility = View.GONE
            mDataBinding.recyclerViewMyOrderList.adapter = adapter
            val layoutManager = LinearLayoutManager(requireContext())
            mDataBinding.recyclerViewMyOrderList.layoutManager = layoutManager
            recyclerViewMyOrderList.scheduleLayoutAnimation()
            setPaymentInfo()
        } else {
            recyclerViewMyOrderList.visibility = View.GONE
            noTextView.visibility = View.VISIBLE
            activity?.supportFragmentManager?.popBackStack()
        }
//        } else {
//            mViewModel.showToast(getString(R.string.no_internet_connection), requireContext())
//        }


    }


    private fun setPaymentInfo() {
        val adapter = PaymentInformationAdapter()
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerViewPaymentInformation.layoutManager = layoutManager
        recyclerViewPaymentInformation.adapter = adapter
        val lineDivider = ContextCompat.getDrawable(requireContext(), R.drawable.line_divider)
        recyclerViewPaymentInformation.addItemDecoration(
            SimpleDividerItemDecoration(
                requireContext(), lineDivider!!
            )
        )
        recyclerViewPaymentInformation.scheduleLayoutAnimation()
        mViewModel.updateTotalCost()
        BaseViewModel.reportHomeButton()
    }


    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_myorderlist

    override fun getViewModel(): MyOrderListViewModel {
        return initViewModel {
            MyOrderListViewModel()
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.confirm_order_button -> {
                if (OrderListViewModel.orderListSelected.size > 0) {
                    confirm_order_button.isClickable = false
                    mViewModel._animationOne.value = 1
                } else Toast.makeText(
                    requireContext(), getString(R.string.please_select_orders), Toast.LENGTH_SHORT
                ).show()
            }
            R.id.onClickAddMoreList -> {
                replaceFragment(FragmentOrderList.newInstance())
            }
            R.id.onClickAddGeneral -> {
                val dialog = Dialog(requireContext())
                dialog.setContentView(R.layout.dialog_layout)
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
                dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                dialog.setCancelable(false)
                val editTextGeneral = dialog.findViewById<EditText>(R.id.editTextGeneral)
                val submit = dialog.findViewById<Button>(R.id.submit)
                val freeImage = dialog.findViewById<ImageView>(R.id.freeImage)

                submit.setOnClickListener {
                    OrderListViewModel.generalNote = editTextGeneral.text.toString()
                    dialog.dismiss()
                }
                freeImage.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.show()
            }
        }
    }

    private fun callAddToCartAPI() {
        mViewModel.callAddToCartAPI(requireContext(), this)
    }


//    interface OnAdapterUpdate {
//        fun onUpdate(orderItem: OrderItem)
//    }
}