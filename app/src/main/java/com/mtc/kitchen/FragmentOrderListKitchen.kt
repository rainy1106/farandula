package com.mtc.kitchen

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.mtc.BR
import com.mtc.R
import com.mtc.api.BaseRepository
import com.mtc.databinding.FragmentOrdersBinding
import com.mtc.general.BaseFragment
import com.mtc.general.initViewModel
import com.mtc.interfaces.onResponseAPI
import com.mtc.network.ConnectionLiveData
import com.mtc.network.ConnectionModel
import com.mtc.utils.OrderType
import kotlinx.android.synthetic.main.fragment_orders.*
import org.json.JSONArray

class FragmentOrderListKitchen : BaseFragment<FragmentOrdersBinding, OrdersViewModel>(),
    onResponseAPI {

    private var connectionMode: ConnectionModel? = null
    var baseRespository = BaseRepository()

    companion object {
        fun newInstance() = FragmentOrderListKitchen()
        var orderItem = OrderItem(
            "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
            JSONArray()
        )
    }


    private fun initOb() {

        mViewModel.visibleShimmer(shimmerLayoutK, shimmerLayoutK1)


        mDataBinding.newOrders.setOnClickListener {
            showData.text = getString(R.string.showing_new_orders)
            mViewModel.type.value = OrderType.ACCEPTED.type
            loadOrders(/*OrderType.ACCEPTED.type*/)
        }

//        mDataBinding.upComingOrders.setOnClickListener {
//            showData.text = getString(R.string.showing_upcoming_orders)
//            mViewModel.type.value = OrderType.UPCOMINGORDER.type
//            loadOrders(/*OrderType.UPCOMINGORDER.type*/)
//        }
//        mDataBinding.readyOrders.setOnClickListener {
//            showData.text = getString(R.string.showing_ready_orders)
//            mViewModel.type.value = OrderType.READY.type
//            loadOrders(/*OrderType.READY.type*/)
//        }
        mDataBinding.toPrint.setOnClickListener {
            showData.text = getString(R.string.showing_to_be_print_items)
            mViewModel.type.value = "PAID"//OrderType.PAID.type
            loadOrders(/*OrderType.READY.type*/)
        }

        OrdersViewModel.newOrder.observe {
            showData.text = getString(R.string.showing_upcoming_orders)
            mViewModel.type.value = it
            loadOrders(/*it*/)
        }

//        mViewModel.readyCount.observe {
//            readyOrdersB.badgeValue = it.toInt()//newOrdersB.badgeValue.plus(it.toInt())
//        }
        mViewModel.newOrderCount.observe {
            newOrdersB.badgeValue = it.toInt()//newOrdersB.badgeValue.plus(it.toInt())
        }
//        mViewModel.upComingCount.observe {
//            upComingOrdersB.badgeValue = it.toInt() //upComingOrdersB.badgeValue.plus(it.toInt())
//        }
        mViewModel.toBePrint.observe {
            toPrintB.badgeValue = it.toInt() //upComingOrdersB.badgeValue.plus(it.toInt())
        }
/*--------------------------------------*/
//        mViewModel.showProgress.observe {
//            if (it == true) {
//                progressBar.setAnimation(R.raw.food_loading)
//                progressBar.repeatCount = LottieDrawable.INFINITE
//                progressBar.playAnimation()
//                progressBar.visibility = View.VISIBLE
//            } else {
//                progressBar.visibility = View.GONE
//                progressBar.pauseAnimation()
//            }
//        }


        mViewModel.updateBadgeCount.observe {
            updateBadgeValue(it)
        }

        /*-----------------------------------------------*/
        mViewModel.productList.observe { mList ->

            if (mList.size > 0) {

                val _tempMListA =
                    mList.filter { it.status == mViewModel.type.value }
                            as ArrayList<OrderListItem.Result>
                val tempAdded = ArrayList<OrderListItem.Result>()
                tempAdded.addAll(_tempMListA)


                if (mViewModel.type.value.equals(OrderType.READY.type)) {
                    val _tempMListP =
                        mList.filter { it.status == OrderType.ONLYPAID.type }
                                as ArrayList<OrderListItem.Result>
                    tempAdded.addAll(_tempMListP)
                }

                if (tempAdded.isEmpty().not()) {
                    //-----------------
                    val adapter = OrdersAdapter(tempAdded, object : onResponseAPI {
                        override fun onSuccessKitchenOrderListItemRow(order: OrderListItem.Result) {
                            super.onSuccessKitchenOrderListItemRow(order)
                            if (order.cart.isEmpty().not()) {
                                val fr = FragmentOrderDetails.newInstance()
                                fr.setList(order)
                                replaceFragment(fr)
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "No Details Available",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
                    mDataBinding.recyclerViewOrder.adapter = adapter
                    //adapter.onDashboardClickListener = mViewModel
                    val layoutManager = GridLayoutManager(requireContext(), 3)
                    mDataBinding.recyclerViewOrder.layoutManager = layoutManager
                    recyclerViewOrder.scheduleLayoutAnimation()
                    mDataBinding.recyclerViewOrder.setHasFixedSize(false)
                    mDataBinding.recyclerViewOrder.visibility = View.VISIBLE
                    mDataBinding.noTextView.visibility = View.GONE
                    //  mViewModel.showProgress.value = false
                    //-----------------
                } else {
                    mDataBinding.recyclerViewOrder.visibility = View.GONE
                    //  mViewModel.showProgress.value = false
                    mDataBinding.noTextView.visibility = View.VISIBLE

                }
                mViewModel.inVisibleShimmer(shimmerLayoutK, shimmerLayoutK1)
            } else {
                mViewModel.inVisibleShimmer(shimmerLayoutK, shimmerLayoutK1)
                mDataBinding.recyclerViewOrder.visibility = View.GONE
                mDataBinding.noTextView.visibility = View.VISIBLE
                //mViewModel.showProgress.value = false
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newOrdersB.badgeValue = 0
        upComingOrdersB.badgeValue = 0
        showData.text = getString(R.string.showing_upcoming_orders)
        initOb()
    }

    private fun restart(connection: ConnectionModel) {
        connection.also {
            mViewModel.setNetworkState(it)
            if (!connection.isConnected) {
                mViewModel.showToast(getString(R.string.no_internet_connection), requireContext())
            } else {
                mViewModel.type.value = OrderType.ACCEPTED.type
                loadOrders()
                // showData.text = getString(R.string.showing_upcoming_orders)
                //loadOrders(OrderType.UPCOMINGORDER.type)
            }
        }
    }

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int {
        return R.layout.fragment_orders
    }

    override fun onResume() {
        super.onResume()
        ConnectionLiveData(requireContext()).observe {
            connectionMode = it
            restart(it)
        }
    }


    private fun loadOrders() {


        mViewModel.visibleShimmer(shimmerLayoutK, shimmerLayoutK1)
        mViewModel.getOrders(requireContext())
        if (noTextView.isVisible) {
            noTextView.visibility = View.GONE
        }
        mDataBinding.recyclerViewOrder.visibility = View.GONE

//        if (mViewModel.isInternetConnected(requireContext())) {
//            showLoader(getString(R.string.loading_orders))

//        val progressBar = ProgressDialog(requireContext())
//        requireActivity().runOnUiThread {
//            progressBar.apply {
//                setProgressStyle(ProgressDialog.STYLE_SPINNER) // Progress Dialog Style Spinner
//                setMessage(getString(R.string.loading_orders))
//                setCancelable(false)
//                show() // Display Progress Dialog
//            }
//        }
//
//        mViewModel.getOrders(requireContext())
//            object : onResponseAPI {
//                override fun onSuccess(arrayList: ArrayList<com.mtc.order.OrderItem>) {
//                    TODO("Not yet implemented")
//                }
//
//                @SuppressLint("NotifyDataSetChanged")
//                override fun onSuccessKitchen(mList: ArrayList<com.mtc.kitchen.OrderItem>) {
//
//                }
//
//                override fun onSuccessKitchenOrderListItem(mList: ArrayList<OrderListItem.Result>) {
//                    if (mList.size > 0) {
//
//                        updateBadgeValue(mList)
//
//                        val adapter = OrdersAdapter(mList.filter { it.status == type }
//                                as ArrayList<OrderListItem.Result>, object : onResponseAPI {
//                            override fun onSuccessKitchenOrderListItemRow(order: OrderListItem.Result) {
//                                super.onSuccessKitchenOrderListItemRow(order)
//                                if (order.cart.isEmpty().not()) {
//                                    val fr = FragmentOrderDetails.newInstance()
//                                    fr.setList(order)
//                                    replaceFragment(fr)
//                                } else {
//                                    Toast.makeText(
//                                        requireContext(),
//                                        "No Details Available",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                }
//
//                            }
//
//                            override fun onSuccessKitchenOrderListItem(arrayList: ArrayList<OrderListItem.Result>) {
//
//                            }
//
//                            /*clicked*/
//                            override fun onSuccess(arrayList: ArrayList<com.mtc.order.OrderItem>) {
//                                // replaceFragment(FragmentOrderDetails.newInstance())
//                            }
//
//                            override fun onSuccessKitchen(arrayList: ArrayList<OrderItem>) {
//
//                            }
//
//
//                            override fun onFailure() {
//                                TODO("Not yet implemented")
//                            }
//
//                        })
//                        mDataBinding.recyclerViewOrder.setHasFixedSize(false)
//                        mDataBinding.recyclerViewOrder.visibility = View.VISIBLE
//                        mDataBinding.noTextView.visibility = View.GONE
//                        mDataBinding.recyclerViewOrder.adapter = adapter
//                        adapter.notifyDataSetChanged()
//                        //adapter.onDashboardClickListener = mViewModel
//                        val layoutManager = GridLayoutManager(requireContext(), 3)
//                        mDataBinding.recyclerViewOrder.layoutManager = layoutManager
//                        recyclerViewOrder.scheduleLayoutAnimation()
//                    } else {
//                        mDataBinding.recyclerViewOrder.visibility = View.GONE
//                        mDataBinding.noTextView.visibility = View.VISIBLE
//                    }
//                    progressBar.dismiss()
        //hideLoader()
//                }
//
//
//                override fun onFailure() {
//                    progressBar.dismiss()
//                    //hideLoader()
//                    mDataBinding.recyclerViewOrder.visibility = View.GONE
//                    mDataBinding.noTextView.visibility = View.VISIBLE
//                }
//
//            })

//        } else {
//            mViewModel.showToast(getString(R.string.no_internet_connection), requireContext())
//        }
    }

    private fun updateBadgeValue(mList: ArrayList<OrderListItem.Result>) {
        var countU: Int = 0 //accepted
        var countN: Int = 0 // new
        var countR: Int = 0 // new
        var countP: Int = 0 //paid
        mList.forEach {
//            if (it.getOrderStatus() == OrderType.UPCOMINGORDER.name)
//                countU = countU.plus(1)
             if (it.getOrderStatus() == OrderType.ACCEPTED.name)
                countN = countN.plus(1)
//            else if (it.getOrderStatus() == OrderType.READY.name || it.getOrderStatus() == "PAID")
//                countR = countR.plus(1)
//            else if (it.getOrderStatus() == "PAID")
//                countP = countP.plus(1)
        }
        //  upComingOrdersB.badgeValue = countU
        mViewModel.updateNewOrderCount(
            countN.toString(),
            countU.toString(),
            countR.toString(),
            countP.toString()
        )

    }


    override fun getViewModel(): OrdersViewModel {
        return initViewModel {
            OrdersViewModel(baseRespository)
        }
    }


}