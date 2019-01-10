package com.study.zhai.playandroid.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 沉浸式状态栏的Utils
 */
public class StatusBarUtils {

    /**
     * 最外层布局为 DrawerLayout 的状态栏颜色
     *
     * @param activity
     * @param color  颜色
     * @param contentId  内容视图的id
     */
    public static void setDrawerLayoutStatusViewColor(Activity activity, int color, int contentId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //要在内容布局增加状态栏，否则会盖在侧滑菜单上
            ViewGroup rootView = activity.findViewById(android.R.id.content);
            //DrawerLayout 则需要在第一个子视图即内容试图中添加padding
            View parentView = rootView.getChildAt(0);
            LinearLayout linearLayout = new LinearLayout(activity);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            View statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity));
                statusBarView.setBackgroundColor(color);
            //添加占位状态栏到线性布局中
            linearLayout.addView(statusBarView, lp);
            //侧滑菜单
            DrawerLayout drawer = (DrawerLayout) parentView;
            //内容视图
            View content = activity.findViewById(contentId);
            //将内容视图从 DrawerLayout 中移除
            drawer.removeView(content);
            //添加内容视图
            linearLayout.addView(content, content.getLayoutParams());
            //将带有占位状态栏的新的内容视图设置给 DrawerLayout
            drawer.addView(linearLayout, 0);
        }
    }

    /**
     * 最外层布局为 DrawerLayout 的状态栏颜色
     *
     * @param activity
     * @param resId 可以是图片资源文件
     * @param contentId 内容视图的id
     */
    public static void setDrawerLayoutStatusViewResource(Activity activity, int resId, int contentId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //要在内容布局增加状态栏，否则会盖在侧滑菜单上
            ViewGroup rootView = activity.findViewById(android.R.id.content);
            //DrawerLayout 则需要在第一个子视图即内容试图中添加padding
            View parentView = rootView.getChildAt(0);
            LinearLayout linearLayout = new LinearLayout(activity);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            View statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity));
            statusBarView.setBackgroundResource(resId);
            //添加占位状态栏到线性布局中
            linearLayout.addView(statusBarView, lp);
            //侧滑菜单
            DrawerLayout drawer = (DrawerLayout) parentView;
            //内容视图
            View content = activity.findViewById(contentId);
            //将内容视图从 DrawerLayout 中移除
            drawer.removeView(content);
            //添加内容视图
            linearLayout.addView(content, content.getLayoutParams());
            //将带有占位状态栏的新的内容视图设置给 DrawerLayout
            drawer.addView(linearLayout, 0);
        }
    }

    /**
     * 最外层布局不为 DrawerLayout 的状态栏颜色
     *
     * @param activity
     * @param color 颜色资源 this.getResource().getColor()的形式
     */
    public static void setStatusViewColor(Activity activity, int color) {
        ViewGroup rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.setPadding(0, getStatusBarHeight(activity), 0, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //直接设置状态栏颜色
            activity.getWindow().setStatusBarColor(color);
        } else {
            //增加占位状态栏
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            View statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity));
            statusBarView.setBackgroundColor(color);
            decorView.addView(statusBarView, lp);
        }
    }

    /**
     * 最外层布局不为 DrawerLayout 的状态栏颜色
     *
     * @param activity
     * @param resId 图片资源
     */
    public static void setStatusViewResource(Activity activity, int resId) {
        //增加占位状态栏
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View statusBarView = new View(activity);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(activity));
        statusBarView.setBackgroundResource(resId);
        decorView.addView(statusBarView, lp);
    }

    /**
     * 最外层布局不为 DrawerLayout 的状态栏颜色
     *
     * @param activity
     * @param resId 图片资源
     */
    public static void setStatusViewResource2(Activity activity, int resId) {
        ViewGroup rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.setPadding(0, getStatusBarHeight(activity), 0, 0);
        //增加占位状态栏
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View statusBarView = new View(activity);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(activity));
        statusBarView.setBackgroundResource(resId);
        decorView.addView(statusBarView, lp);
    }

    /**
     * 全屏且状态栏透明
     *
     * @param activity
     */
    public static void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                attributes.flags |= flagTranslucentStatus;
                window.setAttributes(attributes);
            }
        }
    }

    /**
     * 状态栏亮色模式，设置状态栏黑色文字、图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    public static int statusBarLightMode(Activity activity){
        int result=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if(MIUISetStatusBarLightMode(activity, true)){
                result=1;
            }else if(FlymeSetStatusBarLightMode(activity.getWindow(), true)){
                result=2;
            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result=3;
            }
        }
        return result;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     * @param window 需要设置的窗口
     * @param dark 是否把状态栏文字及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 利用反射获取状态栏高度
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 需要MIUIV6以上
     * @param activity
     * @param dark 是否把状态栏文字及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    public static boolean MIUISetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        Window window=activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if(dark){
                    extraFlagField.invoke(window,darkModeFlag,darkModeFlag);//状态栏透明且黑色字体
                }else{
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result=true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if(dark){
                        activity.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    }else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            }catch (Exception e){

            }
        }
        return result;
    }
}
