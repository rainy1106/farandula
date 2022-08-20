package com.mtc.general

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.mtc.BR

abstract class BaseActivity<B : ViewDataBinding, V : BaseViewModel> : AppCompatActivity() {

    // region - Public properties
    lateinit var mDataBinding: B
    lateinit var mViewModel: V

    // region - Lifecycle functions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
        observeData()
    }

    // region - Private functions
    private fun performDataBinding() {
        mDataBinding = DataBindingUtil.setContentView<B>(this, getLayoutId())
        this.mViewModel = getViewModel()
        mDataBinding.setVariable(BR.viewModel, mViewModel)
        mDataBinding.executePendingBindings()
    }
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

    @SuppressLint("ResourceAsColor")
    private fun observeData() {
//        syncDao.getAllSyncList().observe(this, Observer {
//            if (it.isNotEmpty()) {
//                SyncLiveData.postValue(false)
//                GlobalScope.launch { SyncManager.sync(this@BaseActivity) }
//            } else SyncLiveData.postValue(true)
//        })
//        LoginManager.getInstance(this).setUserStatus(LoginManager.getInstance(this).getUserStatus())
//        observeUserStatus()
    }

    // region - Public functions
    fun <T> LiveData<T>.observe(performTask: (it: T) -> Unit) {
        this.observe(this@BaseActivity, Observer {
            performTask(it)
        })
    }
}