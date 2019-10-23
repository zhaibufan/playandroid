package com.study.zhai.playandroid.lifecycle

import android.annotation.SuppressLint
import android.arch.lifecycle.GenericLifecycleObserver
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner

@SuppressLint("RestrictedApi")
interface IPresenter : GenericLifecycleObserver {

    fun onCreate()
    fun onStart()
    fun onResume()
    fun onStop()
    fun onDestroy()

    override fun onStateChanged(p0: LifecycleOwner?, p1: Lifecycle.Event?) {
        when (p1) {
            Lifecycle.Event.ON_CREATE -> onCreate()
            Lifecycle.Event.ON_START -> onStart()
            Lifecycle.Event.ON_RESUME -> onResume()
            Lifecycle.Event.ON_PAUSE -> onStop()
            Lifecycle.Event.ON_STOP -> onStop()
            Lifecycle.Event.ON_DESTROY -> onDestroy()
        }
    }
}