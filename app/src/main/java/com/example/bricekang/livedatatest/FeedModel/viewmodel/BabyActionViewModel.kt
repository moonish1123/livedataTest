package com.example.bricekang.livedatatest.FeedModel.viewmodel

import androidx.core.ktx.R.id.async
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bricekang.livedatatest.FeedModel.model.BabyAction
import com.example.bricekang.livedatatest.FeedModel.model.FeedBabyAction
import java.lang.ref.WeakReference
import java.util.logging.Handler

class BabyActionViewModel(owner: LifecycleOwner): ViewModel() {

    var actionList = MutableLiveData<MutableList<BabyAction>>()
    var refOwner = WeakReference<LifecycleOwner>(owner)

    init {
        actionList.value = mutableListOf()
    }

    fun insertBabyAction(data: BabyAction) {
        actionList.value?.let {
            it.add(data)
            actionList.postValue(it)
        }
    }

    fun updateBabyAction(index: Int, data: BabyAction) {
        actionList.value?.get(index)?.let {
            if (it is FeedBabyAction && data is FeedBabyAction) {
                it.amount = data.amount
            }

            it.actionDate = data.actionDate

            android.os.Handler().post {
                actionList.postValue(actionList.value)
            }
        }
    }

    fun removeBabyAction(index: Int) {
        actionList.value?.removeAt(index)

        actionList.postValue(actionList.value)
    }

    override fun onCleared() {
        super.onCleared()

        refOwner.get()?.let {
            actionList.removeObservers(it)
        }
    }
}
