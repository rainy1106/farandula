package com.mtc.kitchen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.mtc.BR
import com.mtc.R
import com.mtc.databinding.FragmentOrderHistoryBinding
import com.mtc.general.BaseFragment
import com.mtc.general.SharedPreference
import com.mtc.general.initViewModel
import com.mtc.utils.PDFClass
import com.mtc.utils.PDFClass.Companion.PERMISSION_CODE
import kotlinx.android.synthetic.main.fragment_order_history.*

class FragmentOrderHistory : BaseFragment<FragmentOrderHistoryBinding, OrderHistoryViewModel>(),
    View.OnClickListener {

    private var pdfList = ArrayList<OrderHistory.Result>()
//    private var connectionMode: ConnectionModel? = null

    companion object {
        fun newInstance() = FragmentOrderHistory()
    }


    //    private fun restart(connection: ConnectionModel) {
//        connection.also {
//            mViewModel.setNetworkState(it)
//            if (!connection.isConnected) {
//                mViewModel.showToast(getString(R.string.no_internet_connection), requireContext())
//            } else {
//                CoroutineScope(Dispatchers.IO).launch {
//                    setupRecyclerView()
//                }
//            }
//        }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        dateLayout.setOnClickListener(this)
        exportIdLayout.setOnClickListener(this)

    }

    private fun initViews() {
        mViewModel.setDefaultDate()
        mViewModel.dateSelected.observe {
            dateId.text = it
            SharedPreference.setDate(requireContext(), it)
            setupRecyclerView()
        }

    }

    interface OnComplete {
        fun onComplete()
    }

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_order_history

    override fun getViewModel(): OrderHistoryViewModel {
        return initViewModel {
            OrderHistoryViewModel()
        }
    }

    private fun setupRecyclerView() {

        mViewModel.getOrderHistory(requireContext())
        mViewModel.historyList.observe { data ->
            pdfList = data
            if (data.size > 0) {
                val adapter = OrderHistoryAdapter(data)
                mDataBinding.recyclerViewOrderHistory.visibility = View.VISIBLE
                mDataBinding.noTextView.visibility = View.GONE
                mDataBinding.recyclerViewOrderHistory.adapter = adapter
                recyclerViewOrderHistory.adapter?.let {
                    recyclerViewOrderHistory.adapter?.notifyDataSetChanged()
                }
                //adapter.onDashboardClickListener = mViewModel
                val layoutManager = LinearLayoutManager(requireContext())
                mDataBinding.recyclerViewOrderHistory.layoutManager = layoutManager
                recyclerViewOrderHistory.scheduleLayoutAnimation()
            } else {
                mDataBinding.recyclerViewOrderHistory.visibility = View.GONE
                mDataBinding.noTextView.visibility = View.VISIBLE
            }
        }

//        if (mViewModel.isInternetConnected(requireContext())) {
        //  showLoader(getString(R.string.ladoing_hisoty))
//            val progressBar = ProgressDialog(requireContext())
//            requireActivity().runOnUiThread {
//                progressBar.apply {
//                    setProgressStyle(ProgressDialog.STYLE_SPINNER) // Progress Dialog Style Spinner
//                    setMessage(getString(R.string.ladoing_hisoty))
//                    setCancelable(false)
//                    show() // Display Progress Dialog
//                }
//            }
//            mViewModel.getOrderHistory(requireContext(),
//                object : onResponseAPI {
//                    override fun onOrderHistory(data: ArrayList<OrderHistory.Result>) {
//                        super.onOrderHistory(data)
//                        pdfList = data
//                        if (data.size > 0) {
//                            val adapter = OrderHistoryAdapter(data)
//                            mDataBinding.recyclerViewOrderHistory.visibility = View.VISIBLE
//                            mDataBinding.noTextView.visibility = View.GONE
//                            mDataBinding.recyclerViewOrderHistory.adapter = adapter
//                            recyclerViewOrderHistory.adapter?.let {
//                                recyclerViewOrderHistory.adapter?.notifyDataSetChanged()
//                            }
//                            //adapter.onDashboardClickListener = mViewModel
//                            val layoutManager = LinearLayoutManager(requireContext())
//                            mDataBinding.recyclerViewOrderHistory.layoutManager = layoutManager
//                            // recyclerViewOrderHistory.scheduleLayoutAnimation()
//                        } else {
//                            mDataBinding.recyclerViewOrderHistory.visibility = View.GONE
//                            mDataBinding.noTextView.visibility = View.VISIBLE
//                        }
//
//                        //   hideLoader()
//                    }
//
//                    override fun onSuccess(mList: ArrayList<com.mtc.order.OrderItem>) {
//
//                    }
//
//                    override fun onSuccessKitchen(mList: ArrayList<OrderItem>) {
//
//                    }
//
//                    override fun onSuccessKitchenOrderListItem(arrayList: ArrayList<OrderListItem.Result>) {
//                        TODO("Not yet implemented")
//                    }
//
//
//                    override fun onFailure() {
//                        // hideLoader()
////                        progressBar.dismiss()
//                        mDataBinding.recyclerViewOrderHistory.visibility = View.GONE
//                        mDataBinding.noTextView.visibility = View.VISIBLE
//                    }
//
//                })

//        } else {
//            mViewModel.showToast(getString(R.string.no_internet_connection), requireContext())
//        }
    }

    //pdf


    // on below line we are creating a function to request permission.
    fun requestPermission() {
        // on below line we are requesting read and write to
        // storage permission for our application.
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), PDFClass.PERMISSION_CODE
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        // on below line we are checking if the
        // request code is equal to permission code.
        if (requestCode == PERMISSION_CODE) {

            // on below line we are checking if result size is > 0
            if (grantResults.size > 0) {

                // on below line we are checking
                // if both the permissions are granted.
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]
                    == PackageManager.PERMISSION_GRANTED
                ) {

                    // if permissions are granted we are displaying a toast message.
                    Toast.makeText(requireContext(), "Permission Granted..", Toast.LENGTH_SHORT)
                        .show()

                } else {

                    // if permissions are not granted we are
                    // displaying a toast message as permission denied.
                    Toast.makeText(requireContext(), "Permission Denied..", Toast.LENGTH_SHORT)
                        .show()
                    //requireActivity().finish()
                }
            }
        }
    }

    override fun onClick(p0: View?) {


        when (p0?.id) {
            R.id.exportIdLayout -> {

                if (pdfList.size > 0) {


                    // on below line we are checking permission
                    if (PDFClass(requireContext()).checkPermissions()) {

                        mViewModel.dowloadPdf(requireActivity())


                        // if permission is granted we are displaying a toast message.
//                        PDFClass(requireContext()).generatePDF(pdfList, object : OnComplete {
//                            override fun onComplete() {
//
//                                val builder = AlertDialog.Builder(requireContext())
//                                builder.setTitle("Pdf Download")
//                                builder.setMessage("Pdf downloaded successfully in download folder")
//                                //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))
//                                builder.setPositiveButton(android.R.string.ok) { dialog, which ->
//                                    dialog.dismiss()
//                                }
//
//                                builder.show()
//
//                            }
//                        })

                    } else {
                        // if the permission is not granted
                        // we are calling request permission method.
                        requestPermission()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.no_order_history_for_pdf),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            R.id.dateLayout -> {
                mViewModel.showDateSelector(requireContext())
            }
        }
    }


}