package com.example.bricekang.livedatatest.CustomLiveData

import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bricekang.livedatatest.CustomLiveData.model.PersonModel
import com.example.bricekang.livedatatest.R
import com.example.bricekang.livedatatest.TestAppCompatActivity
import com.example.bricekang.livedatatest.Util.random
import kotlinx.android.synthetic.main.activity_custom_livedata_viewmodel.*
import java.lang.ref.WeakReference

class CustomViewModelActivity : TestAppCompatActivity() {
    lateinit var viewModel: CustomLiveViewModel
    lateinit var view: CustomLiveDataView

    val testNameSet = listOf("riela", "blair", "june", "alex", "jammy", "irene", "aaron", "dino")
    var index = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_livedata_viewmodel)

        viewModel = ViewModelProvider(this, viewFactory).get(CustomLiveViewModel::class.java)
        view = CustomLiveDataView(this, viewModel)

        add_button.setOnClickListener {
            viewModel.getNameProfileModel().putData(PersonModel(testNameSet[(0..(testNameSet.size - 1)).random()], (10..50).random(), false))
            index++
        }

        delete.setOnClickListener {
            viewModel.getNameProfileModel().delteData()
        }

        //init
        viewModel.getNameProfileModel().getData()
    }
}

class CustomLiveDataView(parent: CustomViewModelActivity, viewModel: CustomLiveViewModel) {
    private val model: CustomLiveViewModel = viewModel

    private val refParent: WeakReference<CustomViewModelActivity> = WeakReference(parent)
    private val textView: TextView = parent.text

    //순서가 중요함 바뀌면 안된다 (init 과 static 영역 ??)
    private val nameChangeObserver = Observer<List<PersonModel>> {
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
    }

    init {
        refParent.get()?.let {
            model.getNameProfileModel().observe(it, nameChangeObserver)
        }
    }

    fun deinit() {
        model.getNameProfileModel().removeObserver(nameChangeObserver)
    }
}