package com.study.zhai.playandroid.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.study.zhai.playandroid.MyApplication;
import com.study.zhai.playandroid.R;
import com.study.zhai.playandroid.base.BaseResultActivity;
import com.study.zhai.playandroid.contract.DownloadContract;
import com.study.zhai.playandroid.presenter.DownloadFilePre;
import com.study.zhai.playandroid.util.DecodeBitmapUtils;
import com.study.zhai.playandroid.util.DpToPxUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DownloadActivity extends BaseResultActivity implements DownloadContract.View {

    @BindView(R.id.iv_image)
    ImageView ivImage;

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

    public void downloadFile(View view) {
        pre.downloadFile(PICTURE_URL);
    }

    @Override
    public void initData() {
        pre = new DownloadFilePre();
        pre.attachView(this);
    }

    @Override
    public void onStartDown() {
        Log.d(TAG, "onStartDown");
    }

    @Override
    public void onProgress(int currentLength) {
        Log.d(TAG, "onProgress currentLength = " + currentLength);
    }

    @Override
    public void onFinish(final String localPath) {
        Log.d(TAG, "onFinish");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 通过BitmapFactory解析图片
//                Bitmap bitmap = DecodeBitmapUtils.compressBySize(localPath, DpToPxUtils.dp2px(DownloadActivity.this, 100), DpToPxUtils.dp2px(DownloadActivity.this, 100));
//                ivImage.setImageBitmap(bitmap);
                // 通过Glide加载图片
                Glide.with(DownloadActivity.this).load(localPath).into(ivImage);
            }
        });
    }

    @Override
    public void onFailure(String erroInfo) {
        Log.d(TAG, "onFailure --- " + erroInfo);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pre.detachView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
