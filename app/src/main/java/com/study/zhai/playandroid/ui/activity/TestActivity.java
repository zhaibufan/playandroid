package com.study.zhai.playandroid.ui.activity;

import android.os.Handler;
import android.util.Log;

import com.study.zhai.playandroid.R;
import com.study.zhai.playandroid.base.BaseResultActivity;
import com.study.zhai.playandroid.contract.HomeListContract;
import com.study.zhai.playandroid.presenter.ArticleListPre;

public class TestActivity extends BaseResultActivity implements HomeListContract.View {

    private static final String TAG = "MainActivity";
    private Handler mHandler = new Handler();
    private ArticleListPre pre;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {
        Log.d(TAG, "initData");
        pre = new ArticleListPre();
        pre.attachView(this);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pre.getArticleList(4);
            }
        }, 2000);
    }

    @Override
    public void getDemoResultOK(String result) {
        showNormal();
        Log.d(TAG, "result = " +result);
    }

    @Override
    public void getDemoResultErr(String info) {
        showError(info);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
