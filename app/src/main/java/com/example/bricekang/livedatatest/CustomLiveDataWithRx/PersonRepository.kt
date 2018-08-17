package com.example.bricekang.livedatatest.CustomLiveDataWithRx

import android.util.Log
import com.example.bricekang.livedatatest.CustomLiveData.model.PersonModel
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class PersonRepository() {
    var subject: PublishSubject<List<PersonModel>> = PublishSubject.create()

    fun getData(): Single<List<PersonModel>> {
        return Single.create<List<PersonModel>> { singleEmitter ->
            FirebaseFirestore.getInstance()
                    .collection("test_1")
                    .get().addOnCompleteListener {
                        if (it.isSuccessful) {
                            var list = mutableListOf<PersonModel>()
                            it.result.forEach {
                                Log.d("firebase", "$it")
                                list.add(PersonModel(it["name"] as String, it.getLong("age")?.toInt() ?: 0, it["sex"] as Boolean))
                            }
                            list.sortBy {
                                it.age
                            }
                            singleEmitter.onSuccess(list)
                            subject.onNext(list)
                        }
                    }
        }.observeOn(AndroidSchedulers.mainThread())
    }

    fun putData(model: PersonModel) : Single<Boolean> {
        return Single.just(model).map {
            FirebaseFirestore.getInstance().collection("test_1").add(model).isSuccessful
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}

