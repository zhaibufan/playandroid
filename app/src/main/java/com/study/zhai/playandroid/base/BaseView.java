package com.study.zhai.playandroid.base;

public interface BaseView {

    void showNormal();

    /**
     * Show error
     */
    void showError(String err);

    /**
     * Show loading
     */
    void showLoading();

    /**
     * cancelLoading
     */
    void cancelLoading();

    /**
     * Show empty
     */
    void showEmpty();

    /**
     * Reload
     */
    void reload();
}
