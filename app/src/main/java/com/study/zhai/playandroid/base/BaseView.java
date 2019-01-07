package com.study.zhai.playandroid.base;

public interface BaseView {

    /**
     * Hide loading 隐藏加载动画 显示正常布局
     */
    void showNormal();

    /**
     * Show error 当请求数据失败时的错误布局
     */
    void showError(String err);

    /**
     * Show loading 显示加载动画
     */
    void showLoading();

    /**
     * Show empty 当没有数据时显示空布局
     */
    void showEmpty();

    /**
     * Reload 当加载出错或超时等情况下重新加载
     */
    void reload();
}
