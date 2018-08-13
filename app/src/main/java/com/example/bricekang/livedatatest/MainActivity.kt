package com.example.bricekang.livedatatest

import android.app.Application
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {
    private lateinit var model: NameViewModel
    private lateinit var view: NameView
    private val viewFactory: ViewModelProvider.Factory = object: ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(NameViewModel::class.java!!)) {
                NameViewModel(this@MainActivity) as T
            } else {
                modelClass.getConstructor(Application::class.java).newInstance(this@MainActivity)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model = ViewModelProvider(this, viewFactory).get(NameViewModel::class.java)
        view = NameView(this, model)

        initUI()
    }

    fun initUI() {
        button.setOnClickListener {
            model.mCurrentName.value = "changed"
            with(it) {
                visibility = View.GONE
                isEnabled = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        view.deinit()
    }

    fun test() {
        var a: String? = null
        var b: String? = null

        a ?: ""
    }
}

class NameView(parent: AppCompatActivity, viewModel: NameViewModel) {
    private val model: NameViewModel = viewModel
    private val refParent: WeakReference<AppCompatActivity> = WeakReference(parent)

    var textView: TextView

    init {
        textView = parent.textView
    }

    private val nameChangeObserver = Observer<String> {
        it?.let {
            textView.text = it
        }
    }

    init {
        refParent.get()?.let {
            model.mCurrentName.observe(it, nameChangeObserver)
        }

        textView.text = model.mCurrentName.value
    }

    fun deinit() {
        model.mCurrentName.removeObserver(nameChangeObserver)
    }
}
