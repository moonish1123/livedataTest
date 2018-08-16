package com.example.bricekang.livedatatest

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.ref.WeakReference


class NameViewModel(owner: LifecycleOwner): ViewModel() {
    val refOwner: WeakReference<LifecycleOwner> = WeakReference(owner)
    val mCurrentName: MutableLiveData<String>

    init {
        mCurrentName = MutableLiveData<String>()

        mCurrentName.value = "initialized"

        Log.d("feed", "NameViewModel constructor")
    }

    override fun onCleared() {
        super.onCleared()

        refOwner.get()?.let {
            mCurrentName.removeObservers(it)
        }
    }
}

