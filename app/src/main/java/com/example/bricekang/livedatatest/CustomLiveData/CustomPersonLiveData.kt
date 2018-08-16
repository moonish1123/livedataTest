package com.example.bricekang.livedatatest.CustomLiveData

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.bricekang.livedatatest.CustomLiveData.model.PersonModel
import com.google.firebase.firestore.FirebaseFirestore

class CustomPersonLiveData: LiveData<List<PersonModel>>() {
    var db = FirebaseFirestore.getInstance()

    init {

    }

    override fun onInactive() {
        super.onInactive()
    }

    override fun onActive() {
        super.onActive()
    }

    fun getData() {
        var job = FirebaseFirestore.getInstance().collection("test_1").get()
        job.addOnCompleteListener {
            if (it.isSuccessful) {
                var list = mutableListOf<PersonModel>()
                it.result.forEach {
                    Log.d("firebase", "$it")
                    list.add(PersonModel(it["name"] as String, it.getLong("age")?.toInt() ?: 0, it["sex"] as Boolean))
                }
                value = list
            }
        }
    }

    fun putData(model: PersonModel) {
        FirebaseFirestore.getInstance().collection("test_1")
                .add(model)
                .addOnSuccessListener {
                    Log.d("firebase", "DocumentSnapshot added with ID: ${it.id} ")
                    getData()
                }
                .addOnFailureListener {
                    Log.e("firebase", "Error adding document ${it} ")
                }
    }
}
