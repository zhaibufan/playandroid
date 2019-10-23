package com.study.zhai.playandroid.lifecycle

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.study.zhai.playandroid.R

/**
 * Lifecycle使用
 */
class LifecycleActivity : AppCompatActivity() {

    private val observer = ObserverActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val testPresenter = LifecyclePresenter(this)
        lifecycle.addObserver(testPresenter)

        val myCompoment = MyCompoment(lifecycle)

        //添加监听
        lifecycle.addObserver(observer)
    }


    override fun onPause() {
        super.onPause()
        //移除监听
        lifecycle.removeObserver(observer)
    }
}