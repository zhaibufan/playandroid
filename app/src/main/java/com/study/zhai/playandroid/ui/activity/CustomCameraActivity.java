package com.study.zhai.playandroid.ui.activity;

import android.util.DisplayMetrics;
import android.view.View;

import com.study.zhai.playandroid.R;
import com.study.zhai.playandroid.base.BaseActivity;
import com.study.zhai.playandroid.util.ConstantUtil;
import com.study.zhai.playandroid.util.LogUtils;
import com.study.zhai.playandroid.util.StatusBarUtils;
import com.study.zhai.playandroid.widget.CameraPreviewView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;

public class CustomCameraActivity extends BaseActivity implements CameraPreviewView.CameraCallBack{

    @BindView(R.id.surface_view)
    CameraPreviewView mSurfaceView;

    private static final String TAG = "CustomCameraActivity";

    @Override
    public int getLayoutId() {
        return R.layout.activity_custom_camera;
    }

    @Override
    public void initView() {
        StatusBarUtils.fullScreen(this);
        mSurfaceView.setOnCameraCallBack(this);
        DisplayMetrics outMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getRealMetrics(outMetrics);
        int widthPixel = outMetrics.widthPixels;
        int heightPixel = outMetrics.heightPixels;
        // 重新绘制相机预览的宽高
        mSurfaceView.resize(widthPixel, heightPixel);
    }

    @Override
    public void initData() {
    }

    public void takePicture(View view) {
        mSurfaceView.takePicture(ConstantUtil.CUSTOM_CAMERA, new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg");
    }

    public void switchCamera(View view) {
        mSurfaceView.switchFrontCamera();
    }

    @Override
    public void onSurfaceCreated() {
        LogUtils.e(TAG, "onSurfaceCreated");
    }

    @Override
    public void onSurfaceChanged() {
        LogUtils.e(TAG, "onSurfaceChanged");
    }

    @Override
    public void onSurfaceDestroyed() {
        LogUtils.e(TAG, "onSurfaceDestroyed");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSurfaceView != null) {
            mSurfaceView.destroyCamera();
            mSurfaceView = null;
        }
    }
}
