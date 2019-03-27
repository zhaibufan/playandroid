package com.study.zhai.playandroid.contract;


import com.study.zhai.playandroid.base.BasePre;
import com.study.zhai.playandroid.base.BaseView;

/**
 * Contract的基类
 */
public class CommonContract {

    public interface View extends BaseView {
        void showCommonView();
    }

    public interface Presenter<V extends View> extends BasePre<V> {
        void getCommonData();
    }
}
