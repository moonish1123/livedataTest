package com.example.bricekang.livedatatest

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.ref.WeakReference


class NameViewModel(owner: LifecycleOwner): ViewModel() {
    val refOwner: WeakReference<LifecycleOwner> = WeakReference(owner)
    val mCurrentName: MutableLiveData<String>

    init {
        mCurrentName = MutableLiveData()
        mCurrentName.value = "init"
    }

    override fun onCleared() {
        super.onCleared()

        refOwner.get()?.let {
            mCurrentName.removeObservers(it)
        }
    }
}

