package com.study.zhai.playandroid;

import android.util.Log;
import android.view.View;

import com.study.zhai.playandroid.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
    @Override
    public void initView() {
    }
    @Override
    public void initData() {
    }

    public void download(View v) {
        Log.d(TAG, "down");

    }

}
