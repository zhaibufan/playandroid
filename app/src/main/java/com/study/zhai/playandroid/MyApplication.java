package com.study.zhai.playandroid;

import android.app.Application;

/**
 * application
 *
 * @packageName: cn.white.ymc.wanandroidmaster.base
 * @fileName: MyApplication
 * @date: 2018/7/19  15:22
 * @author: ymc
 * @QQ:745612618
 */

public class MyApplication extends Application {
    private static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }

    public static synchronized MyApplication getInstance() {
        return myApplication;
    }

}
