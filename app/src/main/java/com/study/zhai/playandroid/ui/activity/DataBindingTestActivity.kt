package com.study.zhai.playandroid.ui.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.study.zhai.playandroid.R
import com.study.zhai.playandroid.bean.User
import com.study.zhai.playandroid.databinding.ActivityTestDatabindingBinding

class DataBindingTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val binding: ActivityTestDatabindingBinding
//                = DataBindingUtil.setContentView(this, R.layout.activity_test_databinding)
//        binding.user = User("GitCode", 3)

        val binding = DataBindingUtil.setContentView<ActivityTestDatabindingBinding>(this, R.layout.activity_test_databinding)
        binding.user = User("zhai", 18)
    }
}