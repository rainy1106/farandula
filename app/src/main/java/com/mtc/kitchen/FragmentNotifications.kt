package com.mtc.kitchen

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.mtc.BR
import com.mtc.R
import com.mtc.api.APIConstant
import com.mtc.api.BaseRepository
import com.mtc.databinding.FragmentNotificationsBinding
import com.mtc.general.BaseFragment
import com.mtc.general.SharedPreference
import com.mtc.general.initViewModel
import com.mtc.kitchen.adapters.NotificationKitchenAdapter
import com.mtc.models.Notifications
import com.mtc.network.ConnectionLiveData
import com.mtc.network.ConnectionModel
import com.mtc.utils.NetworkUtility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class FragmentNotifications : BaseFragment<FragmentNotificationsBinding, OrdersViewModel>() {

    private var connectionMode: ConnectionModel? = null
    private var baseRespository = BaseRepository()

    companion object {
        fun newInstance() = FragmentNotifications()
    }


    override fun onStart() {
        super.onStart()
        ConnectionLiveData(requireContext()).observe {
            connectionMode = it
            restart(it)
        }
    }

    private fun restart(connection: ConnectionModel) {
        connection.also {
            mViewModel.setNetworkState(it)
            if (!connection.isConnected) {
                mViewModel.showToast(getString(R.string.no_internet_connection), requireContext())
            } else {
                callNotifications()
            }
        }
    }

    private fun callNotifications() {
        if (mViewModel.isInternetConnected(requireContext())) {
            try {
                mViewModel.getNotificationListKitchen(requireContext())
            } catch (ex: Exception) {
                Toast.makeText(requireContext(), "Network Error, Please try again..", Toast.LENGTH_SHORT)
                    .show()
               // mViewModel.getNotificationListKitchen(requireContext())
            }
        } else {
            mViewModel.showToast(getString(R.string.no_internet_connection), requireContext())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

    }

    private fun initViews() {

        mViewModel._mNotificationList.observe {
            if (it.isEmpty().not()) {
                setupRecyclerView(it)
            } else {
                mDataBinding.shimmerLayout.visibility = View.GONE
                mDataBinding.shimmerLayout.stopShimmer()
                mDataBinding.recyclerViewNotificationList.visibility = View.GONE
                mDataBinding.noTextView.visibility = View.VISIBLE
            }
        }
        //--
    }


    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_notifications

    override fun getViewModel(): OrdersViewModel {
        return initViewModel {
            OrdersViewModel(baseRespository)
        }
    }

    private fun setupRecyclerView(mNotificationList: ArrayList<Notifications.ResultNoti>) {

        mDataBinding.shimmerLayout.visibility = View.GONE
        mDataBinding.shimmerLayout.stopShimmer()

        val adapter = NotificationKitchenAdapter(mNotificationList)
        mDataBinding.recyclerViewNotificationList.adapter = adapter
        if (mNotificationList.isEmpty().not()) {
            mDataBinding.recyclerViewNotificationList.visibility = View.VISIBLE
            mDataBinding.noTextView.visibility = View.GONE

        } else {
            mDataBinding.recyclerViewNotificationList.visibility = View.GONE
            mDataBinding.noTextView.visibility = View.VISIBLE
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        mDataBinding.recyclerViewNotificationList.layoutManager = layoutManager
        adapter.setOnItemClick(object : NotificationKitchenAdapter.OnItemClick {
            override fun onItemClick(position: Int) {
                val order_id = mNotificationList[position].order_id
                if (order_id != "0")
                    getOrders(order_id)
                else
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.order_id_not_found),
                        Toast.LENGTH_SHORT
                    ).show()

            }
        })
    }

    private var mContext: Context? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    fun getOrders(order_id: String) {
        val restaurantId =
            mContext?.let { SharedPreference.getRestaurantKitchen(it) }
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())
        val urlPath ="get_order?order_id=${order_id}"//restaurant_id=${restaurantId}&date=${currentDate}&
        val urlLine: String = APIConstant().getApiBaseUrl(urlPath)
        CoroutineScope(Dispatchers.IO).launch {
            val rss = NetworkUtility.APIrequest(urlLine)
            withContext(Dispatchers.Main) {
                try {
                    val arrayList = getOrderJson(rss.toString())
                    val fr = FragmentOrderDetails.newInstance()
                    fr.setList(arrayList[0])
                    replaceFragment(fr)
                } catch (ex: Exception) {
                    Toast.makeText(mContext, "Network Error, Please try again..", Toast.LENGTH_SHORT)
                        .show()
                    //getOrders(order_id)
                }
            }
        }

    }//

    fun getOrderJson(response: String): java.util.ArrayList<OrderListItem.Result> {
        val orderJson = java.util.ArrayList<OrderListItem.Result>()
        val res = JSONObject(response).getBoolean("status")
        if (res) {
            val gson = Gson()
            val orderListItem =
                gson.fromJson(response, OrderListItem.Response::class.java)
            return if (orderListItem.status) {
                orderListItem.data.result
            } else orderJson
        } else {
            return orderJson
            // onResponseAPI.onFailure()
        }
        Log.v("Response is: ", response.toString())
    }
}