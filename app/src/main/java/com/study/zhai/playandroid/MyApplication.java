package com.study.zhai.playandroid;

import android.app.Application;

import me.jessyan.autosize.AutoSizeConfig;

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
        // AndroidAutoSize默认是以宽度为基准就行适配的，改方法是设置以高度为基准
        AutoSizeConfig.getInstance().setBaseOnWidth(false);
    }

    public static synchronized MyApplication getInstance() {
        return myApplication;
    }

}
