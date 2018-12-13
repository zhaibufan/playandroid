package com.study.zhai.playandroid.contract;

import com.study.zhai.playandroid.base.BasePre;
import com.study.zhai.playandroid.base.BaseView;

public class DownloadContract {

    public interface View extends BaseView {

        void onStartDown();

        void onProgress(int currentLength);

        void onFinish(String localPath);

        void onFailure(String erroInfo);
    }

    public interface Presenter extends BasePre<View> {
        void downloadFile(String url);
    }
}
