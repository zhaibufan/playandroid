package com.study.zhai.playandroid.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.study.zhai.playandroid.log.LogUtils;

import org.java_websocket.WebSocket;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import ua.naiksoftware.stomp.LifecycleEvent;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompMessage;

public class IOTService extends Service {

    private static final String TAG = "IOTService";
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private StompClient mStompClient;
    private String WS_URI = "ws://iot.dev.lsctl.com:28083/mqtt1";
    private boolean mNeedConnect;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startConnect();
    }

    private void startConnect() {
        connect();
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                mStompClient = null;
                connect();
            }
        }, 30 * 1000, 30 * 1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void connect() {
        mStompClient = Stomp.over(WebSocket.class, WS_URI);
        mStompClient.connect();
        mStompClient.lifecycle().subscribe(new Action1<LifecycleEvent>() {
            @Override
            public void call(LifecycleEvent lifecycleEvent) {
                //关注lifecycleEvent的回调来决定是否重连
                switch (lifecycleEvent.getType()) {
                    case OPENED:
                        mNeedConnect = false;
                        LogUtils.d(TAG, "forlan debug stomp connection opened");
                        break;
                    case ERROR:
                        mNeedConnect = true;
                        LogUtils.d(TAG, "forlan debug stomp connection error is ", lifecycleEvent.getException());
                        break;
                    case CLOSED:
                        mNeedConnect = true;
                        LogUtils.d(TAG, "forlan debug stomp connection closed");
                        break;
                }
            }
        });
//        registerStompTopic();
    }

    //点对点订阅，根据用户名来推送消息
    private void registerStompTopic() {
        mStompClient.topic("").subscribe(new Action1<StompMessage>() {
            @Override
            public void call(StompMessage stompMessage) {
                Log.d(TAG, "forlan debug msg is " + stompMessage.getPayload());
            }
        });
    }
}
