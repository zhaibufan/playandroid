package com.study.zhai.playandroid.presenter;

import com.google.gson.JsonObject;
import com.study.zhai.playandroid.api.ApiService;
import com.study.zhai.playandroid.api.ApiStore;
import com.study.zhai.playandroid.contract.HomeListContract;
import com.study.zhai.playandroid.log.LogUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ArticleListPre extends CommonPresenter<HomeListContract.View> implements HomeListContract.Presenter{

    private int pager;
    private static final String TAG = "ArticleListPre";

    @Override
    public void getArticleList(int num) {

        ApiStore.createApi(ApiService.class)
                .getArticleList(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtils.d("onSubscribe");
                    }
                    @Override
                    public void onNext(JsonObject jsonObject) {
                        String result = jsonObject.toString();
                        if (isAttachView()) {
                            mView.getDemoResultOK(result);
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("error = " + e.getMessage());
                        mView.getDemoResultOK(e.toString());
                    }
                    @Override
                    public void onComplete() {

                    }
                });
    }

}
