package com.study.zhai.playandroid.base;

/**
 * @packageName: cn.white.ymc.wanandroidmaster.base.contract
 * @fileName: BasePresenter
 * @date: 2018/8/6  14:25
 * @author: ymc
 * @QQ:745612618
 */

public class BasePresenter<T extends BaseView> implements BasePre<T> {

    protected T mView;

    @Override
    public void attachView(T view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public boolean isAttachView() {
        return mView != null;
    }

}
