package com.study.zhai.playandroid.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.study.zhai.playandroid.log.LogUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import de.tavendo.autobahn.WebSocketOptions;

public class WebSocketService extends Service {

    private static final String TAG = "WebSocketService";

    public static WebSocketConnection webSocketConnection;
    private WebSocketOptions options = new WebSocketOptions();
    private MyWebSocketHandler socketHandler;
    private String mSocketHost = "ws://10.8.23.2:8088/websocket/jiji"; //ws://message.aoliliya.com/messages/websocket
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        connect();
        return super.onStartCommand(intent, flags, startId);
    }

    private void init() {
        LogUtils.e(TAG, "init");
        webSocketConnection = new WebSocketConnection();
        socketHandler = new MyWebSocketHandler();
    }

    private void connect() {
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                LogUtils.e(TAG, "connect");
                if (!webSocketConnection.isConnected()) {
                    startWebSocketConnect();
                }
            }
        }, 0 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);
    }


    private void startWebSocketConnect() {
        LogUtils.e(TAG, "startWebSocketConnect");
        try {
            webSocketConnection.connect(mSocketHost, socketHandler, options);
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }

    private class MyWebSocketHandler extends WebSocketHandler {

        /**
         * webSocket启动时候的回调
         */
        @Override
        public void onOpen() {
            LogUtils.e(TAG, "open");
        }

        /**
         * webSocket接收到消息后的回调
         */
        @Override
        public void onTextMessage(String payload) {
            LogUtils.e(TAG, "payload = " + payload);
        }

        /**
         * webSocket关闭时候的回调
         */
        @Override
        public void onClose(int code, String reason) {
            LogUtils.e(TAG, "code = " + code + " reason = " + reason);
            switch (code) {
                case 1:
                    break;
                case 2:
                    break;
                case 3://手动断开连接
//                    if (!isExitApp) {
//                       startWebSocketConnect();
//                    }
                    break;
                case 4:
                    break;
                case 5://网络断开连接
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webSocketConnection.isConnected()) {
            webSocketConnection.disconnect();
        }
        stopSelf();
    }
}
