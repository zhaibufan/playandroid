package com.study.zhai.playandroid.contract;

public class HomeListContract {

    public interface Presenter extends CommonContract.Presenter<View> {
        void getArticleList(int num);
    }

    public interface View extends CommonContract.View {
        void getDemoResultOK(String result);

        void getDemoResultErr(String info);
    }
}
