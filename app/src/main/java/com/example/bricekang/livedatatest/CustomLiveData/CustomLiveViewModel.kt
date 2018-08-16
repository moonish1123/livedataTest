package com.example.bricekang.livedatatest.CustomLiveData

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import java.lang.ref.WeakReference

class CustomLiveViewModel(owner: LifecycleOwner): ViewModel() {
    val refOwner: WeakReference<LifecycleOwner> = WeakReference(owner)
    val mCustomData: CustomPersonLiveData

    init {
        mCustomData = CustomPersonLiveData()

        Log.d("firebase", "CustomLiveViewModel constructor")
    }

    fun getNameProfileModel(): CustomPersonLiveData {
        return mCustomData
    }

    override fun onCleared() {
        super.onCleared()

        refOwner.get()?.let {
            mCustomData.removeObservers(it)
        }
    }


}