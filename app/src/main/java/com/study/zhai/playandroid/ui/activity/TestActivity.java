package com.study.zhai.playandroid.ui.activity;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.study.zhai.playandroid.MyApplication;
import com.study.zhai.playandroid.R;
import com.study.zhai.playandroid.base.BaseResultActivity;
import com.study.zhai.playandroid.contract.HomeListContract;
import com.study.zhai.playandroid.presenter.ArticleListPre;
import com.study.zhai.playandroid.util.ZToast;

public class TestActivity extends BaseResultActivity implements HomeListContract.View {

    private static final String TAG = "TestActivity";
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

    public void test(View view) {
        ZToast.showToast(MyApplication.getInstance(), "click");
    }

    @Override
    public void initData() {
        Log.d(TAG, "initData");
        showLoading();
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
        Log.d(TAG, "result = " +result);
        showNormal();
    }

    @Override
    public void getDemoResultErr(String info) {
        showError(info);
    }

    @Override
    public void reload() {
        super.reload();
        pre.getArticleList(5);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
