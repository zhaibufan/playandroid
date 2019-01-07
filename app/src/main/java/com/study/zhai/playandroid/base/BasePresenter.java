package com.study.zhai.playandroid.base;


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
