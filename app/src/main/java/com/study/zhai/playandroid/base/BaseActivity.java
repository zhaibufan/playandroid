package com.study.zhai.playandroid.base;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.study.zhai.playandroid.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder bind;
    protected Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        bind = ButterKnife.bind(this);
        activity = this;
        initView();
        initData();
    }

    public abstract int getLayoutId();
    public abstract void initView();
    public abstract void initData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}