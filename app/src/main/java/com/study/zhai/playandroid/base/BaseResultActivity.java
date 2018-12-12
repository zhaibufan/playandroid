package com.study.zhai.playandroid.base;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.study.zhai.playandroid.R;

public abstract class BaseResultActivity extends BaseActivity implements BaseView{

    private static final String TAG = "BaseResultActivity";
    private static final int NORMAL_STATE = 0;
    private static final int LOADING_STATE = 1;
    public static final int ERROR_STATE = 2;
    public static final int EMPTY_STATE = 3;
    private int currentState = LOADING_STATE;

    private View mErrorView;
    private View mLoadingView;
    private View mEmptyView;
    private ViewGroup mNormalView;
    private TextView tvErrMsg;

    @Override
    public void initView() {
        if(activity == null){
            throw new IllegalStateException("Activity cannot be empty");
        }
        mNormalView = findViewById(R.id.normal_view);
        if(mNormalView  == null){
            throw new IllegalStateException("There must be no mNormalView in the activity");
        }
        if(!(mNormalView.getParent() instanceof ViewGroup)){
            throw new IllegalStateException("The parent layout of mNormalView must belong to the viewgroup");
        }
        ViewGroup parent = (ViewGroup) mNormalView.getParent();
        View.inflate(activity, R.layout.view_loading, parent);
        View.inflate(activity, R.layout.view_error, parent);
        View.inflate(activity, R.layout.view_empty, parent);

        mLoadingView = parent.findViewById(R.id.loading_group);
        mErrorView = parent.findViewById(R.id.error_group);
        mEmptyView = parent.findViewById(R.id.empty_group);
        tvErrMsg = parent.findViewById(R.id.tv_err_msg);
        mErrorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload();
            }
        });
        mErrorView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.VISIBLE);
        mNormalView.setVisibility(View.GONE);
    }

    @Override
    public void showNormal() {
        cancelLoading();
        if(currentState == NORMAL_STATE){
            return;
        }
        hideCurrentView();
        currentState = NORMAL_STATE;
        mNormalView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String err) {
        cancelLoading();
        if(currentState == ERROR_STATE){
            return;
        }
        hideCurrentView();
        currentState = ERROR_STATE;
        mErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading() {
        Log.d(TAG , "mLoadingView2 = " +mLoadingView);
        if(currentState == LOADING_STATE){
            return;
        }
        hideCurrentView();
        currentState = LOADING_STATE;
        mLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmpty() {
        cancelLoading();
        if(currentState == EMPTY_STATE){
            return;
        }
        hideCurrentView();
        currentState = EMPTY_STATE;
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void cancelLoading() {

    }

    @Override
    public void reload() {

    }

    private void hideCurrentView() {
        switch (currentState) {
            case NORMAL_STATE:
                if (mNormalView == null) {
                    return;
                }
                mNormalView.setVisibility(View.GONE);
                break;
            case LOADING_STATE:
                mLoadingView.setVisibility(View.GONE);
                break;
            case ERROR_STATE:
                mErrorView.setVisibility(View.GONE);
                break;
            case EMPTY_STATE:
                mEmptyView.setVisibility(View.GONE);
            default:
                break;
        }
    }
}
