package com.study.zhai.playandroid;

import android.app.Application;

import com.study.zhai.playandroid.log.LogUtils;

import me.jessyan.autosize.AutoSizeConfig;

/**
 * application
 */

public class MyApplication extends Application {

    private static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        LogUtils.init(true);
        // AndroidAutoSize默认是以宽度为基准就行适配的，改方法是设置以高度为基准
        AutoSizeConfig.getInstance().setBaseOnWidth(false);
    }



    public static synchronized MyApplication getInstance() {
        return myApplication;
    }
}
