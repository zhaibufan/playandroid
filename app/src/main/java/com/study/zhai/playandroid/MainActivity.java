package com.study.zhai.playandroid;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.study.zhai.playandroid.base.BaseActivity;
import com.study.zhai.playandroid.ui.activity.DownloadActivity;
import com.study.zhai.playandroid.util.ToastUtil;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import java.util.List;

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
        requestPermission();
    }

    public void download(View v) {
        startActivity(new Intent(this, DownloadActivity.class));

    }


    private void requestPermission() {
        AndPermission.with(this)
                .permission(Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                // 准备方法，和 okhttp 的拦截器一样，在请求权限之前先运行改方法，已经拥有权限不会触发该方法
                .rationale(new Rationale() {
                    @Override
                    public void showRationale(Context context, List<String> permissions, RequestExecutor executor) {
                        // 此处可以选择显示提示弹窗
                        executor.execute();
                    }
                })
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        ToastUtil.show(activity,"用户给权限啦");
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        if (AndPermission.hasAlwaysDeniedPermission(activity, permissions)) {
                            // 打开权限设置页
                            AndPermission.permissionSetting(activity).execute();
                            return;
                        }
                        ToastUtil.show(activity,"用户拒绝权限");
                    }
                }).start();
    }

}
