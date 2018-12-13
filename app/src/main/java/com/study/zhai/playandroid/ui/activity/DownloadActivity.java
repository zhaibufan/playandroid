package com.study.zhai.playandroid.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.study.zhai.playandroid.R;
import com.study.zhai.playandroid.base.BaseActivity;
import com.study.zhai.playandroid.contract.DownloadContract;
import com.study.zhai.playandroid.presenter.DownloadFilePre;

public class DownloadActivity extends BaseActivity implements DownloadContract.View {

    private static final String TAG = "DownloadActivity";
    private DownloadFilePre pre;
    private static final String PICTURE_URL = "http://small-bronze.oss-cn-shanghai.aliyuncs.com/" +
            "image/video/cover/2018/3/8/8BBC6C00DF78476C98AD9CA482DEF635.jpg";
    private static final String VIDEO_URL = "http://7xstkb.com1.z0.glb.clouddn.com/agen_apple.mp4";

    @Override
    public int getLayoutId() {
        return R.layout.activity_download;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        pre = new DownloadFilePre();
        pre.attachView(this);

        pre.downloadFile(VIDEO_URL);
    }

    @Override
    public void onStartDown() {
        Log.d(TAG, "onStartDown");
    }

    @Override
    public void onProgress(int currentLength) {
        Log.d(TAG, "onProgress currentLength = " +currentLength);
    }

    @Override
    public void onFinish(String localPath) {
        Log.d(TAG, "onFinish");
    }

    @Override
    public void onFailure(String erroInfo) {
        Log.d(TAG, "onFailure");
    }

    @Override
    public void showNormal() {

    }

    @Override
    public void showError(String err) {
        Log.d(TAG, "showError");
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
        pre.detachView();
    }
}
