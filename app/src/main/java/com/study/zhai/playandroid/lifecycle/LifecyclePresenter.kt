package com.study.zhai.playandroid.lifecycle

import android.content.Context
import android.util.Log

class LifecyclePresenter(context: Context) : IPresenter{

    override fun onCreate() {
        Log.d("LifecyclePresenter", "onCreate")
    }

    override fun onStart() {
        Log.d("LifecyclePresenter", "onStart")
    }

    override fun onResume() {
        Log.d("LifecyclePresenter", "onResume")
    }

    override fun onStop() {
        Log.d("LifecyclePresenter", "onStop")
    }

    override fun onDestroy() {
        Log.d("LifecyclePresenter", "onDestroy")
    }
}