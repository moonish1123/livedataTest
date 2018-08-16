package com.example.bricekang.livedatatest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bricekang.livedatatest.CustomLiveData.CustomViewModelActivity
import com.example.bricekang.livedatatest.FeedModel.SimpleViewModelActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        simpleLiveDataViewModel.setOnClickListener {
            launchActivity<SimpleViewModelActivity> {
                putExtra("desc", "simpleviewmodel")
            }
        }

        customLiveDataViewModel.setOnClickListener {
            launchActivity<CustomViewModelActivity> {
                putExtra("desc", "customviewmodel")
            }
        }
    }
}


