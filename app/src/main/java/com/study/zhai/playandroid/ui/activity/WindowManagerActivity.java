package com.study.zhai.playandroid.ui.activity;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.study.zhai.playandroid.R;
import com.study.zhai.playandroid.base.BaseActivity;
import com.study.zhai.playandroid.log.LogUtils;

public class WindowManagerActivity extends BaseActivity {

    private static final String TAG = "WindowManagerActivity";
    private   WindowManager.LayoutParams wmParams;
    private WindowManager mWindowManager;
    private Button mFloatView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_window_manager;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                LogUtils.d(TAG, "关闭");
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 1);
            } else {
                //TODO do something you need
                LogUtils.d(TAG, "开启");
            }
        }
    }

    public void startWin(View view) {
        createFloatView();
    }

    public void stopWin(View view) {
        mWindowManager.removeView(mFloatView);
    }

    private void createFloatView() {
        wmParams = new WindowManager.LayoutParams();
        //获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) this.getSystemService(WINDOW_SERVICE);
        //设置window type
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT; //TYPE_PHONE优先级更高
        }

        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 0;

        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

     /*// 设置悬浮窗口长宽数据
    wmParams.width = 200;
    wmParams.height = 80;*/

        mFloatView = new Button(getApplicationContext());
        mFloatView.setText("hello");
        mFloatView.setBackground(getResources().getDrawable(R.drawable.button_ble_press));
        mFloatView.setWidth(30);
        mFloatView.setHeight(15);
        //添加mFloatLayout
        mWindowManager.addView(mFloatView, wmParams);
    }
}
