package com.study.zhai.playandroid.ui.activity

import com.study.zhai.playandroid.R
import com.study.zhai.playandroid.base.BaseActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * @author zhaixiaofan
 * @date 2019/10/17 8:51 PM
 */
class KotlinTestActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_kotlin_test

    override fun initView() {
        runBlocking {

        }

        GlobalScope.launch {

        }
    }

    override fun initData() {
    }

}