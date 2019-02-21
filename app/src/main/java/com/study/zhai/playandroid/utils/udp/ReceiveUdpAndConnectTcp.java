package com.study.zhai.playandroid.utils.udp;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;

import com.study.zhai.playandroid.utils.ConstantUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;

import static android.content.Context.WIFI_SERVICE;

public class ReceiveUdpAndConnectTcp extends Thread {

    WifiManager.MulticastLock lock;
    private static final String TAG = "ReceiveGroupUdp";
    Socket socket = null;
    MulticastSocket ms = null;
    DatagramPacket dp;
    Handler handler;
    Context context;

    public ReceiveUdpAndConnectTcp(Handler handler, Context context) {
        this.handler = handler;
        this.context = context;

        WifiManager manager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        lock = manager.createMulticastLock("udpservice");
    }
    @Override
    public void run() {

        byte[] data = new byte[1024];
        try {
            InetAddress groupAddress = InetAddress.getByName(ConstantUtil.IP_GROUP_UDP);
            ms = new MulticastSocket(ConstantUtil.SEND_UDP_PORT);
            ms.joinGroup(groupAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {

            /**接收Udp消息*/
            Log.e(TAG, "接收组播");
            try {
                dp = new DatagramPacket(data, data.length);
                if (ms != null) {
                    lock.acquire();
                    ms.receive(dp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            /**建立tcp链接并发送消息*/
            if (dp.getAddress() != null) {
                try {
                    final String target_ip = dp.getAddress().toString().substring(1);
                    if (socket == null) {
                        socket = new Socket(target_ip, ConstantUtil.SEND_UDP_PORT);
                    }
                    OutputStream outputStream = socket.getOutputStream();
                    PrintWriter pw = new PrintWriter(outputStream);
                    pw.print("发送tcp消息");
                    pw.flush();
                    socket.shutdownOutput();

                    lock.release();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (socket != null)
                            socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
