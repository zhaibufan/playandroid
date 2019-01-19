package com.study.zhai.playandroid;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.study.zhai.playandroid.base.BaseActivity;
import com.study.zhai.playandroid.log.LogUtils;
import com.study.zhai.playandroid.ui.activity.CustomCameraActivity;
import com.study.zhai.playandroid.ui.activity.DownloadActivity;
import com.study.zhai.playandroid.ui.activity.PagerTestActivity;
import com.study.zhai.playandroid.ui.activity.ProgressBarActivity;
import com.study.zhai.playandroid.ui.activity.PropertyAnimationEntryActivity;
import com.study.zhai.playandroid.ui.activity.SettingPhotoActivity;
import com.study.zhai.playandroid.ui.activity.TestActivity;
import com.study.zhai.playandroid.utils.NetUtils;
import com.study.zhai.playandroid.utils.StatusBarUtils;

import java.util.List;

import me.jessyan.autosize.AutoSizeConfig;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private static final String TAG = "MainActivity";
    private String[] permission = {Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int APPLY_PERMISSION_CODE = 200;

    @Override
    public int getLayoutId() {
        AutoSizeConfig.getInstance().setBaseOnWidth(false);
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        StatusBarUtils.setStatusViewColor(this, this.getResources().getColor(R.color.white_255));
        StatusBarUtils.statusBarLightMode(this);
    }


    @Override
    public void initData() {
        requestPermission();
    }

    public void mvpTest(View view) {
        startActivity(new Intent(this, TestActivity.class));
    }

    public void download(View v) {
        startActivity(new Intent(this, DownloadActivity.class));
    }

    public void settingPhoto(View v) {
        startActivity(new Intent(this, SettingPhotoActivity.class));
    }

    public void camera(View v) {
        startActivity(new Intent(this, CustomCameraActivity.class));
    }

    public void progressBar(View v) {
        startActivity(new Intent(this, ProgressBarActivity.class));
    }

    public void viewPager(View view) {
        startActivity(new Intent(this, PagerTestActivity.class));
    }

    public void animal(View view) {
        startActivity(new Intent(this, PropertyAnimationEntryActivity.class));
    }

    private void requestPermission() {
        if (!EasyPermissions.hasPermissions(this, permission)) {
            EasyPermissions.requestPermissions(this, "您有重要权限未开启，可能影响使用，建议开启", APPLY_PERMISSION_CODE, permission);
        } else {
            LogUtils.e(TAG, "以获取了权限");
        }
    }

    // 把执行操作给easyPermissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    // 同意权限
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    }

    // 拒绝权限
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.e(TAG, "onPermissionsDenied");
        if (EasyPermissions.somePermissionDenied(this, permission[0])) {
            showAppSettingsDialog("相机");
        }
        if (EasyPermissions.somePermissionDenied(this, permission[1])) {
            showAppSettingsDialog("定位");
        }
        if (EasyPermissions.somePermissionDenied(this, permission[2])) {
            showAppSettingsDialog("存储");
        }
    }

    // 如果用户禁止权限跳转至系统设置页面
    private void showAppSettingsDialog(String rationale) {
        new AppSettingsDialog
                .Builder(this)
                .setRationale("此功能需要" + rationale + "权限，否则无法正常使用，建议打开设置")
                .setPositiveButton("是")
                .setNegativeButton("否")
                .build()
                .show();
    }

    @Override
    public void onNetChange(int netStatus) {
        super.onNetChange(netStatus);
        if (netStatus == NetUtils.NETWORK_WIFI) {
            LogUtils.d("当前网络 wifi");
        }
    }
}
