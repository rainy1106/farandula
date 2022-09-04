package com.mtc.home

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ExpandableListAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.FirebaseDatabase
import com.mtc.R
import com.mtc.api.APIConstant
import com.mtc.databinding.ActivityHomeBinding
import com.mtc.dialog.DialogThankYou
import com.mtc.general.BaseActivity
import com.mtc.general.BaseViewModel.Companion.reportHomeButton
import com.mtc.general.SharedPreference
import com.mtc.general.initViewModel
import com.mtc.interfaces.EventHandler
import com.mtc.kitchen.OrdersViewModel
import com.mtc.network.ConnectionLiveData
import com.mtc.network.ConnectionModel
import com.mtc.order.FragmentMyOrderList
import com.mtc.order.FragmentOrderList
import com.mtc.order.OrderListViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(), View.OnClickListener,
    EventHandler {
    private var connectionMode: ConnectionModel? = null
    private var adapter: ExpandableListAdapter? = null
    private var titleList: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDataBinding.viewModel = getViewModel()
        orderListButton.setOnClickListener(this)
        replaceFragment(FragmentOrderList.newInstance())
        observers()
    }


    override fun onResume() {
        super.onResume()
        ConnectionLiveData(this).observe {
            connectionMode = it
            restart(it)
        }

    }


    override fun onStart() {
        super.onStart()
        SharedPreference.setIsKitchen(this@HomeActivity, false)
    }

    private fun restart(connection: ConnectionModel) {
        connection.also {
            mViewModel.setNetworkState(it)
            if (!connection.isConnected) {
                mViewModel.showToast(getString(R.string.no_internet_connection), this)
            } else {
                setCategories()
                setBanner()
//                setLogin()
            }
        }
    }

//    private fun setLogin() {
//        val urlLine = APIConstant.ApiBaseUrl + "seat_login?seat_id=" +
//                "${SharedPreference.getSeatId(this@HomeActivity)}&device_type=Android&register_id=${
//                    SplashViewModel.FCM
//                }"
//        CoroutineScope(Dispatchers.IO).launch {
//            val rss = NetworkUtility.APIrequest(urlLine)
//        }
//    }//---


    private fun replaceFragment(fragment: Fragment) {
        val manager = supportFragmentManager
//        var prevFragment = manager.findFragmentByTag(fragment::class.java.simpleName)
//        if (prevFragment == null) // if none were found, create it
//            prevFragment = fragment
        val transaction = manager.beginTransaction()
        transaction.add(R.id.container, fragment, fragment::class.java.simpleName)
        //transaction.addToBackStack(null)
        transaction.commit()
    }


    override fun getBindingVariable(): Int {
        TODO("Not yet implemented")
    }

    override fun getLayoutId(): Int = R.layout.activity_home

    override fun getViewModel(): HomeViewModel {
        return initViewModel {
            HomeViewModel()
        }
    }

    private fun observers() {

//        updateHomeButton.observe {
//            orderListTextView.text = getString(R.string.your_order_is)
//            orderDisplayListSelected.addAll(OrderListViewModel.orderListSelected)
//            OrderListViewModel.orderListSelected.clear()
//        }
//        setCategories()


        OrdersViewModel.showDialog.observe {
            if (it){
                try {
                    FirebaseDatabase.getInstance().reference.child(APIConstant.CHATS_ROOM_NEW)
                        .child(SharedPreference.getTableId(this@HomeActivity)!!)
                        .ref.removeValue { error, ref -> Log.v("Ref", ref.toString()) }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                val dialog = DialogThankYou(this@HomeActivity)
                dialog.show()
            }
        }


        OrdersViewModel.userUpdates.observe {
            if (OrderListViewModel.orderListSelected.size != 0 && it.toString().isNotBlank())
                showAlert(it)
        }

        mViewModel.categoryList.observe(this) {
            Log.e("ResSize", "" + it.size + "::" + it)
            setExpandableView(it)
        }
        mViewModel.banner.observe(this) {
            try {
                Picasso.get().load(it).into(freeimage)
            } catch (ex: Exception) {
                freeimage.setImageResource(R.drawable.na)
            }
            try {
                Picasso.get().load(it).into(freeimage1)
            } catch (ex: Exception) {
                freeimage1.setImageResource(R.drawable.na)
            }
        }
        reportHomeButton.observe {
            if (it != "0")
                (getString(R.string.your_order_is) + " : $ $it").also {
                    orderListTextView.text = it
                }
            else orderListTextView.text = getString(R.string.your_order_is)
        }

    }

    private fun showAlert(string: String) {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .create()
        val view = layoutInflater.inflate(R.layout.customview_layout, null)
        val userUpdateTitle = view.findViewById<TextView>(R.id.userUpdateTitle)
        userUpdateTitle.text = string
        val button = view.findViewById<Button>(R.id.dialogDismiss_button)
        val buttonC = view.findViewById<Button>(R.id.dialogDismissC_button)
        builder.setView(view)
        button.setOnClickListener {
            builder.dismiss()
        }
        buttonC.setOnClickListener {
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            //SharedPreference.clear(this)
            OrderListViewModel.orderListSelected.clear()
            orderListTextView.text = getString(R.string.your_order_is)
            finish()
        }

    }


//    override fun onSuccessCategory(arrayList: ArrayList<Category>) {
//        super.onSuccessCategory(arrayList)
//        setExpandableView(arrayList)
//    }


    override fun onClick(view: View?) {
        if (OrderListViewModel.orderListSelected.let { OrderListViewModel.orderListSelected.size > 0 }) {
            replaceFragment(FragmentMyOrderList.newInstance())
        } else mViewModel.showToast(getString(R.string.please_select_orders), this@HomeActivity)
    }


    fun setCategories() {
        mViewModel.setCategories()
//        mViewModel.setCategories(this@HomeActivity)
    }

    private fun setBanner() {
//        val commonAPI = CommonApi()
//        commonAPI.getBanner(this@HomeActivity, this)
        mViewModel.setBanner()

    }

    private fun setExpandableView(arrayList: ArrayList<Category>) {
        var previousItem = -1
        if (expandableListView != null) {
            val listData = APIConstant.categoriesList
            titleList = ArrayList(listData.keys)
            adapter = CustomExpandableListAdapter(this, titleList as ArrayList<String>, listData)
            expandableListView!!.setAdapter(adapter)
            expandableListView!!.setOnGroupExpandListener { groupPosition ->
                val groupValue = (titleList as ArrayList<String>)[groupPosition]
                arrayList.forEach {
                    if (groupValue == it.category_name) {
                        SharedPreference.setCatId(this@HomeActivity, it.category_id)
                    }
                }
                replaceFragment(FragmentOrderList.newInstance())
                if (listData.values.isNotEmpty()) {
                    if (groupPosition != previousItem)
                        expandableListView.collapseGroup(previousItem)
                    previousItem = groupPosition
                } else {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.no_sub_categories_available),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
            expandableListView!!.setOnGroupCollapseListener { groupPosition ->

//                val groupValue = (titleList as ArrayList<String>)[groupPosition]
//                arrayList.forEach {
//                    if (groupValue == it.category_name) {
//                        SharedPreference.setCatId(this@HomeActivity, it.category_id)
//                    }
//                }
//                replaceFragment(FragmentOrderList.newInstance())
            }
            expandableListView!!.setOnChildClickListener { expandableListView, view, groupPosition, childPosition, _ ->
                println(
                    listData[(titleList as ArrayList<String>)[groupPosition]]!!.get(
                        childPosition
                    ).category_id
                )
                SharedPreference.setCatId(
                    this, listData[(titleList as ArrayList<String>)[groupPosition]]!!.get(
                        childPosition
                    ).category_id
                )
                replaceFragment(FragmentOrderList.newInstance())

                false
            }
        }
    }
}