package com.mtc.order

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.mtc.api.BaseRepository
import com.mtc.general.BaseViewModel
import com.mtc.general.SyncLiveData

class OrderListViewModel(
    var baseRepository: BaseRepository
) : BaseViewModel() {

    companion object {
        var instractions: String = ""
        var extraItems = ArrayList<String>()
        var orderListSelected = ArrayList<OrderItem>()
        var generalNote = ""
    }

    //  private var networkState: ConnectionModel? = null
//    val mListF: LiveData<Boolean> get() = _mListF
//    var _mListF: MutableLiveData<Boolean> = MutableLiveData()

    val mList: LiveData<ArrayList<OrderItem>> get() = _mList
    var _mList: MutableLiveData<ArrayList<OrderItem>> = MutableLiveData()

    init {
        syncLiveDataObserver = Observer { isSynced ->
            this.isSynced = isSynced
            if (isSynced) reportHomeButton()
        }
        SyncLiveData.observeForever(syncLiveDataObserver)
    }


    fun getProductList(context: Context) {
        baseRepository.getProductList(context, _mList)
    }

//    fun setNetworkState(connectionModel: ConnectionModel) {
//        networkState = connectionModel
//
//    }


}