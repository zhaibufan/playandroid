package com.study.zhai.playandroid.utils.udp;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.study.zhai.playandroid.utils.ConstantUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ReceiveBroadUdp extends Thread {

    private static final String TAG = "ReceiveBroadUdp";
    private Handler mHandle;
    private Context mContext;

    private DatagramSocket socket;
    private DatagramPacket packet;

    public ReceiveBroadUdp(Context context, Handler handler) {
        this.mContext = context;
        this.mHandle = handler;
    }

    @Override
    public void run() {
        super.run();
        {
            byte[] by = new byte[1024];
            packet = new DatagramPacket(by, by.length);
            while (true) {
                Log.e(TAG, "接收UDP广播");
                try {
                    if (socket == null) {
                        socket = new DatagramSocket(ConstantUtil.SEND_UDP_PORT);
                    }
                    Thread.sleep(1000);
                    socket.receive(packet);
                    socket.setSoTimeout(2000);
                    String data = new String(packet.getData(), 0, packet.getLength());
                    String ip = packet.getAddress().toString().trim();
                    Log.e(TAG, "data =" + data +"  quest_ip="+ip);

                    Message obtain = Message.obtain();
                    obtain.obj = "ip -> " + ip +" \n接收的数据 -> " + data;
                    obtain.what = 0X002;
                    mHandle.sendMessage(obtain);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
