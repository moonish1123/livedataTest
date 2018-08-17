package com.example.bricekang.livedatatest.CustomLiveDataWithRx

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.bricekang.livedatatest.CustomLiveData.model.PersonModel
import com.example.bricekang.livedatatest.R
import com.example.bricekang.livedatatest.TestAppCompatActivity
import com.example.bricekang.livedatatest.Util.random
import kotlinx.android.synthetic.main.activity_custom_livedata_viewmodel.*
import java.lang.ref.WeakReference

class CustomViewModelActivityWithRx : TestAppCompatActivity() {
    lateinit var viewModel: CustomLiveViewModel
    lateinit var view: CustomLiveDataView

    val testNameSet = listOf("riela-rx", "blair-rx", "june-rx", "alex-rx", "jammy-rx", "irene-rx", "aaron-rx", "dino-rx")
    var index = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_livedata_viewmodel)

        viewModel = ViewModelProvider(this, viewFactory).get(CustomLiveViewModel::class.java)
        view = CustomLiveDataView(this, viewModel)

        add_button.setOnClickListener {
            viewModel.getNameProfileModel().putData(PersonModel(testNameSet[(0..(testNameSet.size - 1)).random()], (10..50).random(), false)).doOnSubscribe {
                Toast.makeText(this,"put start", Toast.LENGTH_SHORT).show()
            }.doOnSuccess {
                Toast.makeText(this,"put end", Toast.LENGTH_SHORT).show()
            }.subscribe()
            index++
        }

        delete.setOnClickListener {
            viewModel.getNameProfileModel().deleteData().subscribe({

            }, {

            })
        }

        //init
        viewModel.getNameProfileModel().getData().doOnSuccess {
            Toast.makeText(this,"loading start", Toast.LENGTH_SHORT).show()
        }.doOnSuccess {
            Toast.makeText(this,"loading end", Toast.LENGTH_SHORT).show()
        }.subscribe()
    }
}

class CustomLiveDataView(parent: CustomViewModelActivityWithRx, viewModel: CustomLiveViewModel) {
    private val model: CustomLiveViewModel = viewModel

    private val refParent: WeakReference<CustomViewModelActivityWithRx> = WeakReference(parent)
    private val textView: TextView = parent.text

    init {
        refParent.get()?.let {
            model.getNameProfileModel().subject.subscribe({
                it?.let {
                    if (it.size != 0) {
                        textView.text = it.map {
                            "${it.name}-${it.age}"
                        }.reduce { m1, m2 ->
                            m1 + "\n" +  m2
                        }
                    } else {
                        textView.text = "none"
                    }

                }
            }, {
                Log.e("firebase", "error $it")
            })
        }
    }

    fun deinit() {

    }
}