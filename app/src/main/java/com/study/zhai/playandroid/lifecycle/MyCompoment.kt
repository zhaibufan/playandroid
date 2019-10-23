package com.study.zhai.playandroid.lifecycle

import android.arch.lifecycle.GenericLifecycleObserver
import android.arch.lifecycle.Lifecycle
import android.util.Log

class MyCompoment(val lifecycle : Lifecycle) {

    init {
        lifecycle.addObserver(GenericLifecycleObserver { p0, p1 ->
            Log.d("MyCompoment", p1.name)
        })
    }

}