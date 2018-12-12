package com.study.zhai.playandroid.base;

public interface BasePre<T> {
    /**
     * 注入View
     *
     * @param view view
     */
    void attachView(T view);

    /**
     * 回收View
     */
    void detachView();

    /**
     * view 是否被回收我们
     *
     * @return true没回收
     */
    boolean isAttachView();

}
