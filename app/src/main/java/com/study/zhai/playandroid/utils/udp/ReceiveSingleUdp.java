package com.study.zhai.playandroid.utils.udp;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.study.zhai.playandroid.utils.ConstantUtil;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * 接收单播的信息
 *      和接收广播代码一样
 */
public class ReceiveSingleUdp extends Thread {

    private static final String TAG = "ReceiveSingleUdp";
    private Handler mHandle;
    private Context mContext;
    private DatagramSocket socket;
    private DatagramPacket packet;

    public ReceiveSingleUdp(Handler handler, Context context) {
        this.mContext = context;
        this.mHandle = handler;
    }

    @Override
    public void run() {
        super.run();
        byte[] buffer = new byte[1024];
        packet = new DatagramPacket(buffer, buffer.length);
        try {
            while (true) {
                Log.e(TAG, "接收UDP单播");
                socket = new DatagramSocket(ConstantUtil.SEND_UDP_PORT);
                Thread.sleep(1000);
                socket.receive(packet);
                socket.setSoTimeout(2000);
                String ip = packet.getAddress().getHostAddress();
                String data = new String(packet.getData(),0,packet.getLength());

                Log.e(TAG, "ip = " + ip);

                Message obtain = Message.obtain();
                obtain.obj = "ip -> " + ip +" \n接收的数据 -> " + data;
                obtain.what = 0X001;
                mHandle.sendMessage(obtain);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "error --- " +e.getMessage());
        }

    }
}
