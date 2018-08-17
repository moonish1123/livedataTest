package com.example.bricekang.livedatatest.CustomLiveDataWithRx

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import java.lang.ref.WeakReference

class CustomLiveViewModel(owner: LifecycleOwner): ViewModel() {
    val refOwner: WeakReference<LifecycleOwner> = WeakReference(owner)
    lateinit var personRepository: PersonRepository

    init {
        personRepository = PersonRepository()

        Log.d("firebase", "CustomLiveViewModel constructor")
    }

    fun getNameProfileModel(): PersonRepository {
        return personRepository
    }

    override fun onCleared() {
        super.onCleared()

        refOwner.get()?.let {
            //??
        }
    }
}