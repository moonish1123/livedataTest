package com.example.bricekang.livedatatest

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {
    private lateinit var model: NameViewModel
    private lateinit var view: NameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model = NameViewModel(this)
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
            refParent.get()?.textView?.text = it
        }
    }

    init {
        refParent.get()?.let {
            model.mCurrentName.observe(it, nameChangeObserver)
        }

    }

    fun deinit() {
        model.mCurrentName.removeObserver(nameChangeObserver)
    }
}
