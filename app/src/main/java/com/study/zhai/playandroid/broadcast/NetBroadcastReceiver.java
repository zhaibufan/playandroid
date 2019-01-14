package com.study.zhai.playandroid.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.study.zhai.playandroid.utils.NetUtils;

public class NetBroadcastReceiver extends BroadcastReceiver {

    private NetChangeListener mListener;

    public NetBroadcastReceiver(NetChangeListener listener) {
        super();
        this.mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int netWorkState = NetUtils.getNetWorkState(context);
            // 当网络发生变化，判断当前网络状态，并通过NetEvent回调当前网络状态]
            if (mListener != null) {
                mListener.onNetChange(netWorkState);
            }
        }
    }

    public interface NetChangeListener {
        void onNetChange(int netStatus);
    }
}
