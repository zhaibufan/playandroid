package com.study.zhai.playandroid.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.study.zhai.playandroid.broadcast.NetBroadcastReceiver;
import com.study.zhai.playandroid.utils.NetUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity implements NetBroadcastReceiver.NetChangeListener{

    private Unbinder bind;
    protected Activity activity;
    private NetBroadcastReceiver mNetChangeReceiver;
    private int currentNetWorkStatus = NetUtils.NETWORK_NONE; //当前网络的状态
    private AlertDialog dialog; //没有网络的dialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        bind = ButterKnife.bind(this);
        activity = this;
        registerNetChangeReceiver();
        initView();
        initData();
    }

    public abstract int getLayoutId();
    public abstract void initView();
    public abstract void initData();

    private void registerNetChangeReceiver() {
        if (mNetChangeReceiver == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //实例化IntentFilter对象
                IntentFilter filter = new IntentFilter();
                filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                mNetChangeReceiver = new NetBroadcastReceiver(this);
                //注册广播接收
                registerReceiver(mNetChangeReceiver, filter);
            }
        }
    }

    /**
     * 初始化时判断有没有网络
     */
    public void checkNet() {
        currentNetWorkStatus = NetUtils.getNetWorkState(this);
        if (currentNetWorkStatus == NetUtils.NETWORK_NONE) {
            showNetDialog();
        }
    }

    /**
     * 网络变化的回调
     *
     * @param netStatus
     */
    @Override
    public void onNetChange(int netStatus) {
        currentNetWorkStatus = netStatus;
        if (netStatus == NetUtils.NETWORK_NONE) {
            // 没有网络
            showNetDialog();
        } else {
            cancelAlertDialog();
        }
    }

    /**
     * 没有网络的弹窗
     */
    public void showNetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("网络异常");
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 跳到设置网络页面
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelAlertDialog();
            }
        });
        dialog = builder.create();
        dialog.show();
        //设置大小 必须在show之后设置才起作用
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);
    }

    /**
     * 删除没有网络的弹窗
     */
    private void cancelAlertDialog() {
        if (dialog != null) {
            dialog.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
        if (mNetChangeReceiver != null) {
            unregisterReceiver(mNetChangeReceiver);
        }
    }
}