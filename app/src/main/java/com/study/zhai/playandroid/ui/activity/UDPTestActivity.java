package com.study.zhai.playandroid.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.study.zhai.playandroid.R;
import com.study.zhai.playandroid.base.BaseActivity;
import com.study.zhai.playandroid.utils.udp.SendBroadUdp;
import com.study.zhai.playandroid.utils.udp.SendGroupUdp;
import com.study.zhai.playandroid.utils.udp.TcpReceive;
import com.study.zhai.playandroid.utils.udp.UdpReceiverService;

public class UDPTestActivity extends BaseActivity {

    private TextView tvContent;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String content = (String) msg.obj;
            if (TextUtils.isEmpty(content)) {
                Toast.makeText(UDPTestActivity.this, "接收数据为空", Toast.LENGTH_SHORT).show();
                return;
            }
            switch (msg.what) {
                case 0X001:
                    tvContent.setText(content);
                    break;
                case 0X002:
                    tvContent.setText(content);
                    break;
                case 0X003:
                    tvContent.setText(content);
                    break;
                case 0X004:
                    tvContent.setText(content);
                    break;
            }
        }
    };


    @Override
    public int getLayoutId() {
        return R.layout.activity_ud_test;
    }

    @Override
    public void initView() {
        tvContent = findViewById(R.id.tv_content);
    }

    @Override
    public void initData() {
//        new ReceiveSingleUdp(mHandler, this).start();
//        new ReceiveBroadUdp(this, mHandler).start();
//        new ReceiveGroupUdp(mHandler, this).start();

//        new ReceiveUdpAndConnectTcp(mHandler, this).start();
//        new TcpReceive(mHandler).start();

        new TcpReceive(mHandler).start();
    }

    /**udp单播的发送与接收*/
    public void udp1(View view) {
//        new SendSingleUdp("单播").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                UdpReceiverService.sendMsg("第二条消息");
            }
        }).start();
    }

    /**udp单播的发送与接收*/
    public void udp2(View view) {
        new SendBroadUdp("广播").start();
    }

    /**udp组播的发送与接收*/
    public void udp3(View view) {
        new SendGroupUdp("组播").start();
    }

    public void tcp(View view) {
        new SendGroupUdp("组播").start();
    }

    public void sendMsg(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UdpReceiverService.sendMsg("hello tcp");
            }
        }).start();
    }
}
