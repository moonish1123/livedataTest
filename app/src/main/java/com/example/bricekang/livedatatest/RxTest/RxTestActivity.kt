package com.example.bricekang.livedatatest.RxTest

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.bricekang.livedatatest.R
import com.example.bricekang.livedatatest.Util.random
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.toFlowable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_rxtest.*
import java.util.*
import java.util.concurrent.TimeUnit


class RxTestActivity: AppCompatActivity() {

    var subject = BehaviorSubject.createDefault<String>("init")
    val compositeDisposable = CompositeDisposable()
    val repeat = Flowable.just<Int>(1,2,3,4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_rxtest)

        //CombineLast. Test.

        addEvent.setOnClickListener {
            subject.onNext((32..100).random().toChar().toString())
        }

        btnSubject.setOnClickListener {
            desc1.text = ""

            refreshDisposable(desc1) {
                subject.doOnComplete {
                    desc1.append(" complete")
                }.doOnNext {
                    desc1.append(" $it")
                }.subscribe()
            }
        }

        combineLast.setOnClickListener {
            desc1.text = ""

            refreshDisposable(desc1) {
                Flowable.combineLatest(repeat, subject.toFlowable(BackpressureStrategy.BUFFER), BiFunction { i1: Int, s1: String ->
                    "$i1 - $s1"
                }).observeOn(AndroidSchedulers.mainThread()).subscribe ({
                    Log.w("log",Thread.currentThread().toString())
                    desc1.append(" $it")
                },{
                    it.printStackTrace()
                })
            }
        }

        test3.setOnClickListener {
            refreshDisposable(desc1) {
                repeat.map { "map test $it" }.subscribe({
                    desc1.append(it)
                    desc1.append("\n")
                }, {
                    it.printStackTrace()
                })

                repeat.map { "map tolist test $it" }.toList().subscribe({
                    desc1.append(it.toString())
                    desc1.append("\n")
                }, {
                    it.printStackTrace()
                })

                Completable.create {
                    //working hard.

                    it.onComplete()
                }.subscribe({
                    //no arg. just end.
                }, {

                })

                var list = listOf("a" , "b", "c", "d", "e", "f")
                Flowable.fromArray(list).flatMap {
                    it.toFlowable()
                }.doOnNext {
                    desc1.append(it.toString())
                    desc1.append("\n")
                }.subscribe()

                Flowable.fromIterable(list)
                        .flatMap {
                            return@flatMap Flowable.just(it + "-flatmap").delay(Random().nextInt(5).toLong(), TimeUnit.SECONDS)
                        }
                        .toList().observeOn(AndroidSchedulers.mainThread())
                        .doOnSuccess {
                            desc1.append(it.toString())
                            desc1.append("\n")
                        }.subscribe()
                Flowable.fromIterable(list)
                        .switchMap {
                            return@switchMap Flowable.just(it + "-switchMap").delay(Random().nextInt(5).toLong(), TimeUnit.SECONDS)
                        }
                        .toList().observeOn(AndroidSchedulers.mainThread())
                        .doOnSuccess {
                            desc1.append(it.toString())
                            desc1.append("\n")
                        }.subscribe()
                Flowable.fromIterable(list)
                        .concatMap {
                            return@concatMap Flowable.just(it + "-concatMap").delay(Random().nextInt(5).toLong(), TimeUnit.SECONDS)
                        }
                        .toList().observeOn(AndroidSchedulers.mainThread())
                        .doOnSuccess {
                            desc1.append(it.toString())
                            desc1.append("\n")
                        }.subscribe()

                //MVVM event listener..
                return@refreshDisposable Flowable.combineLatest(repeat, subject.toFlowable(BackpressureStrategy.BUFFER), BiFunction { i1: Int, s1: String ->
                    Pair(i1, s1)
                }).switchMap { pair ->
                    Flowable.create<String>({
                        //query
                        it.onNext(pair.toString())
                    }, BackpressureStrategy.BUFFER)
                }.observeOn(AndroidSchedulers.mainThread()).subscribe ({
                    desc1.append(" $it")
                    desc1.append("\n")
                },{
                    it.printStackTrace()
                })
            }
        }
    }

    fun refreshDisposable(view: View, block: () -> Disposable) {
        if (view.tag is Disposable) {
            (view.tag as Disposable).dispose()
            compositeDisposable.remove((view.tag as Disposable))
        }

        var disposal = block()

        view.tag = disposal
        compositeDisposable.add(disposal)
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.dispose()
        compositeDisposable.clear()
    }
}