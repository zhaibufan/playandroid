package com.study.zhai.playandroid.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.PowerManager;
import android.provider.Settings;

import com.study.zhai.playandroid.R;
import com.study.zhai.playandroid.base.BaseActivity;
import com.study.zhai.playandroid.log.LogUtils;
import com.study.zhai.playandroid.utils.JoinWhiteListUtils;

public class KeepAliveActivity extends BaseActivity {

    private static final String TAG = "KeepAliveActivity";

    @Override
    public int getLayoutId() {
        return R.layout.activity_keep_alive;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        boolean ignoringBatteryOptimizations = isIgnoringBatteryOptimizations();
        LogUtils.d(TAG, "ignoringBatteryOptimizations = " + ignoringBatteryOptimizations);
//        if (!ignoringBatteryOptimizations) {
//            requestIgnoreBatteryOptimizations();
//        }
        JoinWhiteListUtils.goSetting();
    }

    private boolean isIgnoringBatteryOptimizations() {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(getPackageName());
        }
        return isIgnoring;
    }

    public void requestIgnoreBatteryOptimizations() {
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
