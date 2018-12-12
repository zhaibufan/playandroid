package com.study.zhai.playandroid.contract;

import com.study.zhai.playandroid.base.BasePre;
import com.study.zhai.playandroid.base.BaseView;

import java.util.List;

public class HomeListContract {

    public interface Presenter extends BasePre<View> {
        void getArticleList(int num);
    }

    public interface View extends BaseView {
        void getDemoResultOK(String result);

        void getDemoResultErr(String info);
    }
}
