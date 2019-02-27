package com.study.zhai.playandroid.ui.activity;

import android.content.Intent;
import android.view.View;

import com.study.zhai.playandroid.R;
import com.study.zhai.playandroid.base.BaseActivity;
import com.study.zhai.playandroid.log.LogUtils;
import com.study.zhai.playandroid.service.WebSocketService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WebSocketActivity extends BaseActivity {

    private static final String TAG = "WebSocketActivity";

    @Override
    public int getLayoutId() {
        return R.layout.activity_websocket;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        Intent intent = new Intent(this, WebSocketService.class);
        startService(intent);
    }

    public void request(View view) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("request", 1);
            jsonObject.put("msgtype", 83);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String s = jsonObject.toString();
        LogUtils.d(TAG, "result = "+s);
        if (WebSocketService.webSocketConnection.isConnected()) {
            WebSocketService.webSocketConnection.sendTextMessage(s);
        }
    }

    public void request2(View view) {
        JSONObject object = new JSONObject();
        try {
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("login", 866964266);
            jsonObject.put("password", "qqqq0000");
            jsonObject.put("Server", "1002");
            jsonArray.put(jsonObject);

            object.put("AccountCredentials", jsonArray);
            object.put("NoOfRecords", 1);
            object.put("msgtype", 61);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String s = object.toString();
        LogUtils.d(TAG, "result = "+s);
        if (WebSocketService.webSocketConnection.isConnected()) {
            WebSocketService.webSocketConnection.sendTextMessage(s);
        }
    }
}
