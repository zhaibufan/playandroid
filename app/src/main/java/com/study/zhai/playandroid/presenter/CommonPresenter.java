package com.study.zhai.playandroid.presenter;

import com.study.zhai.playandroid.base.BasePresenter;
import com.study.zhai.playandroid.contract.CommonContract;


/**
 * Presenter的基类 请求公共的数据
 * @param <V>
 */
public class CommonPresenter<V extends CommonContract.View> extends BasePresenter<V> implements CommonContract.Presenter<V> {

    @Override
    public void getCommonData() {

    }
}
