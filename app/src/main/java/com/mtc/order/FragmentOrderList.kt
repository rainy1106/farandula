package com.mtc.order

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.mtc.BR
import com.mtc.R
import com.mtc.api.BaseRepository
import com.mtc.databinding.FragmentOrderListBinding
import com.mtc.general.BaseFragment
import com.mtc.general.initViewModel
import kotlinx.android.synthetic.main.fragment_order_list.*

class FragmentOrderList : BaseFragment<FragmentOrderListBinding, OrderListViewModel>() {

    //    private var connectionMode: ConnectionModel? = null
    private val baseRepository = BaseRepository()

    companion object {
        fun newInstance() = FragmentOrderList()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDataBinding.viewModel = getViewModel()
        initOb()
    }

    private fun initOb() {

        shimmerLayout.startShimmer()
//        if (mViewModel.isInternetConnected(requireContext())) {
        try {
            //showLoader(getString(R.string.loading))
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        mViewModel.getProductList(requireContext())
//        } else {
//            mViewModel.showToast(getString(R.string.no_internet_connection), requireContext())
//        }


//        mViewModel.mListF.observe {
//            // hideLoader()
//            mDataBinding.recyclerViewOrderList.visibility = View.GONE
//            shimmerLayout.visibility = View.GONE
//            shimmerLayout.stopShimmer()
//            mDataBinding.noTextView.visibility = View.VISIBLE
//        }

        mViewModel.mList.observe {
            if (it.isNotEmpty())
                setRecyclerView(it)
            else {
                mDataBinding.recyclerViewOrderList.visibility = View.GONE
                shimmerLayout.visibility = View.GONE
                shimmerLayout.stopShimmer()
                mDataBinding.noTextView.visibility = View.VISIBLE
            }
        }

    }

    //    private fun restart(connection: ConnectionModel) O


    @SuppressLint("NotifyDataSetChanged")
    private fun setRecyclerView(mList: ArrayList<OrderItem>) {

        if (mList.size > 0) {

            if (OrderListViewModel.orderListSelected.size > 0) {

                OrderListViewModel.orderListSelected.forEach { selectedOrder ->
                    mList.forEach { mListObj ->
                        if (mListObj.product_id == selectedOrder.product_id)
                            if (selectedOrder.isAdded)
                                mListObj.isAdded = true
                    }
                }
            }//

            val adapter = OrderListAdapter(mList)
            mDataBinding.recyclerViewOrderList.setHasFixedSize(true)
            mDataBinding.recyclerViewOrderList.visibility = View.VISIBLE
            mDataBinding.noTextView.visibility = View.GONE
            mDataBinding.recyclerViewOrderList.adapter = adapter
            adapter.let {
                it.notifyDataSetChanged()
            }
            //adapter.onDashboardClickListener = mViewModel
            val layoutManager = GridLayoutManager(requireContext(), 2)
            mDataBinding.recyclerViewOrderList.layoutManager = layoutManager
            /// recyclerViewOrderList.scheduleLayoutAnimation();
        } else {
            mDataBinding.recyclerViewOrderList.visibility = View.GONE
            mDataBinding.noTextView.visibility = View.VISIBLE
        }
        //hideLoader()
        shimmerLayout.visibility = View.GONE
        shimmerLayout.stopShimmer()

    }

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_order_list

    override fun getViewModel(): OrderListViewModel {
        return initViewModel {
            OrderListViewModel(baseRepository)
        }
    }

    override fun onDetach() {
        super.onDetach()
        try {
            System.gc();
            System.runFinalization();
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }
}