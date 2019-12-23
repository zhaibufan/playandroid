package com.study.zhai.playandroid.utils;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;

import com.study.zhai.playandroid.MyApplication;

public class JoinWhiteListUtils {

    /**
     * 跳转到指定应用的首页
     */
    public static void showActivity(@NonNull String packageName) {
        Intent intent = MyApplication.getInstance().getPackageManager().getLaunchIntentForPackage(packageName);
        MyApplication.getInstance().startActivity(intent);
    }

    /**
     * 跳转到指定应用的指定页面
     */
    public static void showActivity(String packageName, @NonNull String activityDir) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, activityDir));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.getInstance().startActivity(intent);
    }

    public static void goSetting() {
        if (isHuawei()) {
            goHuaweiSetting();
        } else if (isXiaomi()) {
            goXiaomiSetting();
        }
    }

    public static boolean isHuawei() {
        if (Build.BRAND == null) {
            return false;
        } else {
            return Build.BRAND.toLowerCase().equals("huawei") || Build.BRAND.toLowerCase().equals("honor");
        }
    }

    public static void goHuaweiSetting() {
        try {
            showActivity("com.huawei.systemmanager",
                    "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
        } catch (Exception e) {
            showActivity("com.huawei.systemmanager",
                    "com.huawei.systemmanager.optimize.bootstart.BootStartActivity");
        }
    }


    public static boolean isXiaomi() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("xiaomi");
    }

    public static void goXiaomiSetting() {
        showActivity("com.miui.securitycenter",
                "com.miui.permcenter.autostart.AutoStartManagementActivity");
    }
}
