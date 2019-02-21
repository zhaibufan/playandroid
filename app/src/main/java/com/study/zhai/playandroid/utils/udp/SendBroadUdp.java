package com.study.zhai.playandroid.utils.udp;

import android.util.Log;

import com.study.zhai.playandroid.utils.ConstantUtil;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SendBroadUdp extends Thread {

    private static final String TAG = "SendBroadUdp";
    private byte[] data;

    public SendBroadUdp(String dataString) {
        data = dataString.getBytes();
    }

    @Override
    public void run() {
        super.run();
        DatagramSocket datagramSocket = null;
        try {
            Log.e(TAG, "准备发送：" + data.toString());
            datagramSocket = new DatagramSocket();
            datagramSocket.setBroadcast(true);
            InetAddress address = InetAddress.getByName(ConstantUtil.IP_BROCAST_UDP);
            DatagramPacket datagramPacket = new DatagramPacket(data, data.length, address, ConstantUtil.SEND_UDP_PORT);
            datagramSocket.send(datagramPacket);
        } catch (Exception e) {
            Log.e(TAG,e.toString());
        } finally {
            if (datagramSocket != null) {
                datagramSocket.close();
            }
        }
    }
}
