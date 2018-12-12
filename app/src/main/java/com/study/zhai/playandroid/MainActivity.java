package com.study.zhai.playandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonObject;
import com.study.zhai.playandroid.api.ApiService;
import com.study.zhai.playandroid.api.ApiStore;
import com.study.zhai.playandroid.base.BaseActivity;
import com.study.zhai.playandroid.contract.HomeListContract;
import com.study.zhai.playandroid.presenter.ArticleListPre;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements HomeListContract.View {

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
        ArticleListPre pre = new ArticleListPre();
        pre.attachView(this);
        pre.getArticleList(4);
        showLoading();
    }

    @Override
    public void getDemoResultOK(String result) {
        Log.d(TAG, "result = " +result);
        cancelLoading();
    }

    @Override
    public void getDemoResultErr(String info) {

    }

    @Override
    public void showError(String err) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void cancelLoading() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void reload() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
