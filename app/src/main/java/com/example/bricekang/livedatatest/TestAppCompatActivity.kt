package com.example.bricekang.livedatatest

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bricekang.livedatatest.CustomLiveData.CustomLiveViewModel
import com.example.bricekang.livedatatest.FeedModel.viewmodel.BabyActionViewModel

open class TestAppCompatActivity: AppCompatActivity() {
    protected val viewFactory: ViewModelProvider.Factory = object: ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when {
                modelClass.isAssignableFrom(NameViewModel::class.java) -> NameViewModel(this@TestAppCompatActivity) as T
                modelClass.isAssignableFrom(BabyActionViewModel::class.java) -> BabyActionViewModel(this@TestAppCompatActivity) as T
                modelClass.isAssignableFrom(CustomLiveViewModel::class.java) -> CustomLiveViewModel(this@TestAppCompatActivity) as T
                else -> modelClass.getConstructor(Application::class.java).newInstance(this@TestAppCompatActivity)
            }
        }
    }
}

inline fun <reified T : Any> Activity.launchActivity(
        requestCode: Int = -1,
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}) {

    val intent = newIntent<T>(this)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivityForResult(intent, requestCode, options)
    } else {
        startActivityForResult(intent, requestCode)
    }
}

inline fun <reified T : Any> Context.launchActivity(
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}) {

    val intent = newIntent<T>(this)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivity(intent, options)
    } else {
        startActivity(intent)
    }
}

inline fun <reified T : Any> newIntent(context: Context): Intent =
        Intent(context, T::class.java)