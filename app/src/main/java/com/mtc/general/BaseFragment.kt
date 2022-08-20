package com.mtc.general

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.mtc.R


/**
 * @author GWL
 * @Created on 02/8/19.
 */
abstract class BaseFragment<B : ViewDataBinding, V : BaseViewModel> : Fragment() {


    // region - Public properties
    lateinit var mDataBinding: B
    lateinit var mViewModel: V
    val permissionList = listOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    val progressLoader by lazy { ProgressDialog(requireActivity()) }
    // endregion

    // region - Lifecycle functions
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return mDataBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = getViewModel()
    }

    fun replaceFragment(fragment: Fragment) {
        val manager = requireActivity().supportFragmentManager
//        var prevFragment = manager.findFragmentByTag(fragment::class.java.simpleName)
//        if (prevFragment == null) // if none were found, create it
//            prevFragment = fragment
        val transaction = manager.beginTransaction()
        transaction.add(R.id.container, fragment, fragment::class.java.simpleName)
        transaction.addToBackStack(fragment::class.java.simpleName)
        transaction.commit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDataBinding.setVariable(getBindingVariable(), mViewModel)
        mDataBinding.lifecycleOwner = this
        mDataBinding.executePendingBindings()
    }

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
//        overridePendingTransitionEnter()
    }
    // endregion

    // region - Abstract functions
    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract fun getBindingVariable(): Int

    /**
     * @return layout resource id
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract fun getViewModel(): V
    // endregion

    // region - Public functions
    fun <T> LiveData<T>.observe(performTask: (it: T) -> Unit) {
        this.observe(viewLifecycleOwner) {
            performTask(it)
        }
    }

    //    fun requestPermission() {
    // Here, thisActivity is the current activity
//        if (ContextCompat.checkSelfPermission(requireContext(), permissionList[0]) != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                ActivityCompat.requestPermissions(requireActivity(), permissionList.toTypedArray(), VideoListFragment.PERMISSION_REQ_CODE)
//                 Show an explanation to the user *asynchronously* -- don't block
//            } else {
//                 No explanation needed, we can request the permission.
//                ActivityCompat.requestPermissions(requireActivity(), permissionList.toTypedArray(), VideoListFragment.PERMISSION_REQ_CODE)
//            }
//        } else {
//             Permission has already been granted
//        }
//    }
//
    fun isInternetConnected(): Boolean {
        val connectivityManager =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permissionList[0]
        ) != PackageManager.PERMISSION_GRANTED
    }


//    fun showLoader(message: String = getString(R.string.loading)) {
//        try {
//            requireActivity().runOnUiThread {
//                progressLoader.apply {
//                    setProgressStyle(ProgressDialog.STYLE_SPINNER) // Progress Dialog Style Spinner
//                    setMessage(message)
//                    setCancelable(false)
//                    show() // Display Progress Dialog
//                }
//            }
//        } catch (ex: Exception) {
//            ex.printStackTrace()
//        }
//    }

//    fun hideLoader() {
//        try {
//            requireActivity().runOnUiThread {
//                if (progressLoader.isShowing)
//                    progressLoader.hide()
//            }
//        } catch (ex: Exception) {
//            ex.printStackTrace()
//        }
//
//    }

    fun Fragment?.runOnUiThread(action: () -> Unit) {
        this ?: return
        if (!isAdded) return // Fragment not attached to an Activity
        activity?.runOnUiThread(action)
    }
//    fun showLoader() {
//        progressDialog =
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) //Check if Android API Level is greater than or equal to 30
//            {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) //This is optional. This will enable Android's Autotheming for the entire App
//                ProgressDialog(
//                    requireActivity(),
//                    ProgressDialog.THEME_FOLLOW_SYSTEM
//                ) // Enables AutoTheming for the ProgressDialog instance.
//            } else //Autotheming not compatible
//            {
//                ProgressDialog(
//                    requireActivity(),
//                    ProgressDialog.THEME_DARK
//                ) // or any other constructors mentioned above
//            }
//        progressDialog.setMessage(getString(R.string.loading))
//        progressDialog.theme = ProgressDialog.THEME_DARK
//        progressDialog.mode = ProgressDialog.MODE_DETERMINATE
//        progressDialog.setTitle(getString(R.string.app_name))
////        progressDialog.progress = 65
////        progressDialog.secondaryProgress = 80
////        progressDialog.setOnCancelListener(object : DialogInterface.OnCancelListener {
////            override fun onCancel(p0: DialogInterface?) {
////                TODO("Not yet implemented")
////            }
////        })
////        progressDialog.setNegativeButton("Dismiss", "Indeterminate") { v: View? ->
////            Toast.makeText(this, "Custom OnClickListener for Indeterminate", Toast.LENGTH_LONG)
////                .show()
////            progressDialog.dismiss()
////        }
//        progressDialog.isCancelable = false
//        //progressDialog.showProgressTextAsFraction(true)
//        progressDialog.show()
//    }

//    fun hide() {
//        progressDialog.dismiss()
//    }
    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
//    private fun overridePendingTransitionEnter() {
//        requireActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
//    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
//    private fun overridePendingTransitionExit() {
//        requireActivity().overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
//    }
    // endregion
}
