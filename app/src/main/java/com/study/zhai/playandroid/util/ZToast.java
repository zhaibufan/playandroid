package com.study.zhai.playandroid.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;


/**
 * Created by Pei on 2016-12-20.
 */
public class ZToast {

    private static final boolean DEBUG = true;
    private static Handler mainHandler = new Handler(Looper.getMainLooper());
    private static ZToast toastUtil = new ZToast();
    private Toast toast = null;
    private UIToastRunable toastRunable;

    private ZToast() {
        toastRunable = new UIToastRunable();
    }

    public void init(Context context) {
        this.toast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT);
    }

    public static ZToast getInstance() {
        return toastUtil;
    }

    public static void showToast(Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public void showToast(String content) {
        if (!DEBUG) {
            return;
        }
        toastRunable.setMsg(content);
        mainHandler.post(toastRunable);
    }

    public void showToastNotHide(String content) {
        toastRunable.setMsg(content);
        mainHandler.post(toastRunable);
    }

    class UIToastRunable implements Runnable {

        private String msg = null;

        public UIToastRunable() {
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            if (toast != null) {
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                View view = toast.getView();
                view.setVisibility(View.VISIBLE);
                toast.setText(msg);
                toast.show();
            }
        }
    }
}
