package com.example.bricekang.livedatatest.FeedModel

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bricekang.livedatatest.FeedModel.model.BabyAction
import com.example.bricekang.livedatatest.FeedModel.model.FeedBabyAction
import com.example.bricekang.livedatatest.FeedModel.viewmodel.BabyActionViewModel
import com.example.bricekang.livedatatest.NameViewModel
import com.example.bricekang.livedatatest.R
import com.example.bricekang.livedatatest.TestAppCompatActivity
import kotlinx.android.synthetic.main.activity_simple_viewmodel.*
import java.lang.ref.WeakReference

class SimpleViewModelActivity : TestAppCompatActivity() {
    private lateinit var viewModel: NameViewModel
    private lateinit var babyActionViewModel: BabyActionViewModel

    private lateinit var view: NameView
    private lateinit var babyActionView: BabyActionView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_viewmodel)

        viewModel = ViewModelProvider(this, viewFactory).get(NameViewModel::class.java)
        view = NameView(this, viewModel)

        babyActionViewModel = ViewModelProvider(this, viewFactory).get(BabyActionViewModel::class.java)
        babyActionView = BabyActionView(this, babyActionViewModel)

        initUI()
    }

    fun initUI() {
        button.setOnClickListener {
            if (!viewModel.mCurrentName.value.equals("button clicked")) {
                viewModel.mCurrentName.value = "button clicked"
                (it as Button).text = "reset"
            } else {
                viewModel.mCurrentName.value = intent.getStringExtra("desc") ?: "initialized"
                (it as Button).text = "button"
            }

            Log.d("feed", "insert")
            babyActionViewModel.insertBabyAction(FeedBabyAction(160))
            babyActionViewModel.insertBabyAction(FeedBabyAction(160))
            babyActionViewModel.insertBabyAction(FeedBabyAction(160))
            Log.d("feed", "update")
            babyActionViewModel.updateBabyAction(Math.max(0, (babyActionViewModel.actionList.value?.size ?: 0) - 1) , FeedBabyAction(60))
            /*Log.d("feed", "remove")
            babyActionViewModel.removeBabyAction(2)*/
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        view.deinit()
        babyActionView.deinit()
    }
}

class BabyActionView(parent: AppCompatActivity, viewModel: BabyActionViewModel) {
    private val model: BabyActionViewModel = viewModel
    private val refParent: WeakReference<AppCompatActivity> = WeakReference(parent)

    private val actionListChangeObserver = Observer<List<BabyAction>> {
        Log.d("feed", "actionListChangeObserver")

        it?.forEach {
            if (it is FeedBabyAction) {
                Log.d("feed", "${it.actionType} - ${it.actionDate} - ${it.amount}")
            } else {
                Log.d("feed", "${it.actionType} - ${it.actionDate}")
            }
        }
    }

    init {
        refParent.get()?.let {
            model.actionList.observe(it, actionListChangeObserver)
        }
    }

    fun deinit() {
        model.actionList.removeObserver(actionListChangeObserver)
    }
}

class NameView(parent: AppCompatActivity, viewModel: NameViewModel) {
    private val model: NameViewModel = viewModel

    private val refParent: WeakReference<AppCompatActivity> = WeakReference(parent)
    private val textView: TextView = parent.text

    //순서가 중요함 바뀌면 안된다 (init 과 static 영역 ??)
    private val nameChangeObserver = Observer<String> {
        Log.d("feed", "nameChangeObserver $it")

        it?.let {
            textView.text = it

            with(textView) {
                background = run {
                    if (it.equals("origin"))
                        Color.CYAN.toDrawable()
                    else
                        Color.LTGRAY.toDrawable()
                }
            }
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